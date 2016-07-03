package com.dummyc0m.pylon.datakit.network

import com.dummyc0m.pylon.datakit.network.message.Message
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/13/16.
 */
interface MessageHandler<in T: Message> {
    /**
     * Inbound
     */
    fun handle(ctx: ChannelHandlerContext, message: T)
}