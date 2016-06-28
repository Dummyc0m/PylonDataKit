package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.datakit.packet.PacketManager
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder
import io.netty.handler.ssl.SslContext
import io.netty.util.concurrent.EventExecutorGroup

/**
 * Created by Dummy on 6/12/16.
 */
class DataKitServerInitializer(val sslContext: SslContext, val eventExecutorGroup: EventExecutorGroup, val key: String, val packetManager: PacketManager): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        ch.config().setOption(ChannelOption.TCP_NODELAY, true)

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslContext.newHandler(ch.alloc()))

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast("framedecoder", LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
        pipeline.addLast("frameencoder", LengthFieldPrepender(2))
        pipeline.addLast("decoder", ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(DataKitServerInitializer::class.java.classLoader)))
        pipeline.addLast("encoder", ObjectEncoder())
        // and then business logic.
        pipeline.addLast("authenticator", AuthenticationHandler(key))
        pipeline.addLast(eventExecutorGroup, DataKitServerHandler(packetManager))
    }
}