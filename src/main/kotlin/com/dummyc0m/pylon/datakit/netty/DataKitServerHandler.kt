package com.dummyc0m.pylon.datakit.netty

import com.dummyc0m.pylon.datakit.network.MessageManager
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.group.ChannelGroup
import io.netty.handler.ssl.SslHandler
import java.net.InetAddress

/**
 * Created by Dummy on 6/13/16.
 */
class DataKitServerHandler(private val messageManager: MessageManager) : SimpleChannelInboundHandler<String>(String::class.java) {

    override fun channelRead0(p0: ChannelHandlerContext, p1: String) {
        messageManager.handle(p0, p1)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        ctx.pipeline().get(SslHandler::class.java).sslCloseFuture().addListener {
            messageManager.remove(ctx.channel())
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        //ctx.close()
    }
}