package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.packet.PacketManager
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import io.netty.handler.ssl.SslContext

/**
 * Created by Dummy on 6/13/16.
 */
class DataClientInitializer(val sslContext: SslContext, val host: String, val port: Int, val packetManager: PacketManager): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslContext.newHandler(ch.alloc(), host, port))

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast("framedecoder", LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
        pipeline.addLast("frameencoder", LengthFieldPrepender(2))
        pipeline.addLast("decoder", ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(DataClientInitializer::class.java.classLoader)))
        pipeline.addLast("encoder", ObjectEncoder())

        // and then business logic.
        pipeline.addLast("handler", DataClientHandler(packetManager))

    }

}