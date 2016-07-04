package com.dummyc0m.pylon.datakit.network.handler

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 7/1/16.
 */
class DataKitDataMessageHandler: MessageHandler<DataMessage> {
    override fun handle(ctx: ChannelHandlerContext, message: DataMessage) {
        DataKitLog.wtf("Server received DataMessage")
    }
}
