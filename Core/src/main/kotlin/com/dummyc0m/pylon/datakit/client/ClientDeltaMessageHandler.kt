package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 7/1/16.
 */
class ClientDeltaMessageHandler : MessageHandler<DeltaMessage> {
    override fun handle(ctx: ChannelHandlerContext, message: DeltaMessage) {
        DataKitLog.wtf("Client received DeltaMessage")
    }
}