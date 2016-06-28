package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.packet.*
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
class DataClient(val host: String, val port: Int, val key: String,
                 val dataPacketHandler: PacketHandler<DataPacket>,
                 val dataFailureHandler: PacketHandler<DataFailurePacket>) {
    private lateinit var channel: Channel
    private lateinit var lastWriteFuture: ChannelFuture
    private lateinit var eventLoop: NioEventLoopGroup
    private val packetManager: PacketManager = PacketManager()

    fun start() {
        registerPackets()

        val sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        eventLoop = NioEventLoopGroup()
        val b = Bootstrap()
                .group(eventLoop)
                .channel(NioSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .handler(DataClientInitializer(sslCtx, host, port, packetManager))
        channel = b.connect(host, port).sync().channel()

        // Authenticate
        lastWriteFuture = channel.writeAndFlush(AuthMessage(key))

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

    private fun registerPackets() {
        packetManager.registerPacket(DataPacket::class.java, dataPacketHandler)
        packetManager.registerPacket(DataFailurePacket::class.java, dataFailureHandler)
    }

    fun send(packet: Packet) {
        lastWriteFuture = channel.writeAndFlush(packet)
    }

    fun shutdown() {
        lastWriteFuture.sync()
        channel.closeFuture().sync()
        eventLoop.shutdownGracefully()
    }
}