package com.dummyc0m.pylon.datakit.packet.handlers

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.DataSavePacket
import com.dummyc0m.pylon.datakit.packet.PacketHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/25/16.
 */
class DataSavePacketHandler(val dataHandler: NioDataHandler) : PacketHandler<DataSavePacket> {
    override fun handle(ctx: ChannelHandlerContext, t: DataSavePacket) {
        dataHandler.queueSave(t.offlineUUID, t.data)
    }
}