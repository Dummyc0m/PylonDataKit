package com.dummyc0m.pylon.datakit.network

import com.dummyc0m.pylon.datakit.network.message.Message
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/13/16.
 */
interface MessageHandler<T: Message> {
    /**
     * Inbound
     */
    fun <T: Message> handle(ctx: ChannelHandlerContext, message: T)
}