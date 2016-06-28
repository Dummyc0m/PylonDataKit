package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.*
import com.dummyc0m.pylon.datakit.packet.handlers.*
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import io.netty.util.concurrent.DefaultEventExecutorGroup

class DataKit(val ip: String, val port: Int, val key: String, val dbType: String, val dbUrl: String, val dbUsername: String, val dbPassword: String, val estimateLoad: Int) {
    private val packetManager: PacketManager = PacketManager()

    fun run() {
        val connectionFactory = DBConnectionFactory(dbType, dbUrl, dbUsername, dbPassword)
        val dataHandler = NioDataHandler(connectionFactory, estimateLoad)
        registerPackets(dataHandler)

        val ssc = SelfSignedCertificate()
        val sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()

        val bossGroup = NioEventLoopGroup(1)
        val workerGroup = NioEventLoopGroup()
        val eventGroup = DefaultEventExecutorGroup(16)
        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .handler(LoggingHandler(LogLevel.INFO))
                    .childHandler(DataKitServerInitializer(sslCtx, eventGroup, key, packetManager))

            b.bind(ip, port).sync().channel().closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
            eventGroup.shutdownGracefully()
        }
    }

    private fun registerPackets(dataHandler: NioDataHandler) {
        packetManager.registerPacket(DataLoadPacket::class.java, DataLoadPacketHandler(dataHandler))
        packetManager.registerPacket(DataUnloadPacket::class.java, DataUnloadPacketHandler(dataHandler))
        packetManager.registerPacket(DataUnloadAllPacket::class.java, DataUnloadAllPacketHandler(dataHandler))
        packetManager.registerPacket(DataQueryPacket::class.java, DataQueryPacketHandler(dataHandler))
        packetManager.registerPacket(DataSavePacket::class.java, DataSavePacketHandler(dataHandler))
        packetManager.registerPacket(DataSaveAllPacket::class.java, DataSaveAllPacketHandler(dataHandler))
    }
}
