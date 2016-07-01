package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.network.*
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import com.dummyc0m.pylon.datakit.network.message.Message
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory

/**
 * Created by Dummy on 6/13/16.
 */
class DataClient(val host: String,
                 val port: Int,
                 val key: String,
                 val serverId: String,
                 val dataMessageHandler: MessageHandler<DataMessage>) {
    private lateinit var channel: Channel
    private lateinit var eventLoop: NioEventLoopGroup
    private val messageManager: MessageManager = MessageManager()

    fun start() {
        val sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        eventLoop = NioEventLoopGroup()
        val b = Bootstrap()
                .group(eventLoop)
                .channel(NioSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .handler(DataClientInitializer(sslCtx, host, port, messageManager))
        channel = b.connect(host, port).sync().channel()

        // Authenticate
        channel.writeAndFlush(JsonNodeFactory(false)
                .objectNode()
                .put("key", key)
                .put("id", serverId)
                .toString())

//            val `in` = BufferedReader(InputStreamReader(System.`in`))
//            while (true) {
//                val line = `in`.readLine() ?: break
//
//                // Sends the received line to the server.
//                lastWriteFuture = channel.writeAndFlush(ConnectPacket())
//
//                // If user typed the 'bye' command, wait until the server closes
//                // the connection.
//                if ("bye" == line.toLowerCase()) {
//                    break
//                }
//            }
    }

    private fun registerHandlers() {
        messageManager.registerHandler(DataMessage::class.java, dataMessageHandler)
        messageManager.registerHandler(DeltaMessage::class.java, ClientDeltaMessageHandler())
    }

    fun send(message: Message) {
        messageManager.send(message, channel)
    }

    fun shutdown() {
        messageManager.shutdown()
        channel.closeFuture().sync()
        eventLoop.shutdownGracefully()
    }
}