package com.dummyc0m.pylon.datakit.netty

import com.dummyc0m.pylon.datakit.network.MessageManager
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext
import io.netty.util.CharsetUtil
import io.netty.util.concurrent.EventExecutorGroup

/**
 * Created by Dummy on 6/12/16.
 */
class DataKitServerInitializer(private val sslContext: SslContext,
                               private val eventExecutorGroup: EventExecutorGroup,
                               private val key: String,
                               private val messageManager: MessageManager): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        ch.config().setOption(ChannelOption.TCP_NODELAY, true)
        ch.config().setOption(ChannelOption.SO_KEEPALIVE, true)

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslContext.newHandler(ch.alloc()))

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast("framedecoder", LengthFieldBasedFrameDecoder(16777215, 0, 3, 0, 3))

        pipeline.addLast("stringdecoder", StringDecoder(CharsetUtil.UTF_8))

        pipeline.addLast("stringencoder", StringEncoder(CharsetUtil.UTF_8))

        pipeline.addLast("frameencoder", LengthFieldPrepender(3))
        // and then business logic.
        pipeline.addLast("authenticator", AuthenticationHandler(key, messageManager))
        pipeline.addLast(eventExecutorGroup, DataKitServerHandler(messageManager))
    }
}