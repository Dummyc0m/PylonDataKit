package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.network.MessageManager
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext
import io.netty.util.CharsetUtil

/**
 * Created by Dummy on 6/13/16.
 */
class DataClientInitializer(private val sslContext: SslContext,
                            private val host: String,
                            private val port: Int,
                            private val messageManager: MessageManager): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslContext.newHandler(ch.alloc(), host, port))

        // On top of the SSL handler, add the text line codec.
        pipeline.addLast("framedecoder", LengthFieldBasedFrameDecoder(16777215, 0, 3, 0, 3))

        pipeline.addLast("stringdecoder", StringDecoder(CharsetUtil.UTF_8))

        pipeline.addLast("frameencoder", LengthFieldPrepender(3))

        pipeline.addLast("stringencoder", StringEncoder(CharsetUtil.UTF_8))

        // and then business logic.
        pipeline.addLast("handler", DataClientHandler(messageManager))

    }

}