package com.dummyc0m.pylon.datakit.network.handler

import com.dummyc0m.pylon.datakit.data.NioDataHandler
import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 7/1/16.
 */
class DeltaMessageHandler(val nioDataHandler: NioDataHandler) : MessageHandler<DeltaMessage> {
    override fun handle(ctx: ChannelHandlerContext, message: DeltaMessage) {
        nioDataHandler.feedback(message)
    }
}