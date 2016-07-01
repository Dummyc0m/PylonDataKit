package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.network.MessageManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * Created by Dummy on 6/13/16.
 */
class DataClientHandler(private val messageManager: MessageManager) : SimpleChannelInboundHandler<String>(String::class.java) {
    override fun channelRead0(p0: ChannelHandlerContext, p1: String) {
        messageManager.handle(p0, p1)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        //ctx.close()
    }
}