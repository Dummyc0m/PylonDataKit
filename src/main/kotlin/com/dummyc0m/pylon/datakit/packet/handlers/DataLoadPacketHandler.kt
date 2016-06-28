package com.dummyc0m.pylon.datakit.packet.handlers

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.DataLoadPacket
import com.dummyc0m.pylon.datakit.packet.PacketHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/25/16.
 */
class DataLoadPacketHandler(val dataHandler: NioDataHandler) : PacketHandler<DataLoadPacket> {
    override fun handle(ctx: ChannelHandlerContext, t: DataLoadPacket) {
        dataHandler.queueLoad(t.onlineUUID, t.offlineUUID)
    }
}