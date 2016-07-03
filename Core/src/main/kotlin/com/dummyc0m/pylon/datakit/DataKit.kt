package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.datakit.data.NioDataHandler
import com.dummyc0m.pylon.datakit.netty.DataKitServerInitializer
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.dummyc0m.pylon.datakit.network.handler.DataKitDataMessageHandler
import com.dummyc0m.pylon.datakit.network.handler.DeltaMessageHandler
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import io.netty.util.concurrent.DefaultEventExecutorGroup
import java.util.concurrent.ExecutorService

class DataKit internal constructor(private val ip: String,
                                   private val port: Int,
                                   private val key: String,
                                   dbType: String,
                                   dbUrl: String,
                                   dbUsername: String,
                                   dbPassword: String,
                                   estimateLoad: Int,
                                   executorService: ExecutorService) {
    private val connectionFactory = DBConnectionFactory(dbType,
            dbUrl,
            dbUsername,
            dbPassword)
    private val messageManager: MessageManager = MessageManager()
    val dataHandler = NioDataHandler(connectionFactory,
            estimateLoad,
            messageManager,
            executorService)

    private lateinit var channel: Channel
    private lateinit var bossGroup: NioEventLoopGroup
    private lateinit var workerGroup: NioEventLoopGroup
    private lateinit var eventGroup: DefaultEventExecutorGroup

    fun start(): DataKit {
        registerHandlers()
        val ssc = SelfSignedCertificate()
        val sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()

        bossGroup = NioEventLoopGroup(1)
        workerGroup = NioEventLoopGroup()
        eventGroup = DefaultEventExecutorGroup(16)
        val b = ServerBootstrap()

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(DataKitServerInitializer(sslCtx, eventGroup, key, messageManager))

        channel = b.bind(ip, port).sync().channel()
        dataHandler.init()
        Log.info("Started on $ip:$port")
        return this
    }

    private fun registerHandlers() {
        messageManager.registerHandler(DataMessage::class.java, DataKitDataMessageHandler())
        messageManager.registerHandler(DeltaMessage::class.java, DeltaMessageHandler(dataHandler))
    }

    fun shutdown() {
        Log.info("Shutting down")
        messageManager.shutdown()
        channel.closeFuture().sync()
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
        eventGroup.shutdownGracefully()
        dataHandler.unloadAll()
    }
}
