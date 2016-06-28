package com.dummyc0m.pylon.datakit.packet.handlers

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.DataSaveAllPacket
import com.dummyc0m.pylon.datakit.packet.PacketHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/25/16.
 */
class DataSaveAllPacketHandler(val dataHandler: NioDataHandler) : PacketHandler<DataSaveAllPacket> {
    override fun handle(ctx: ChannelHandlerContext, t: DataSaveAllPacket) {
        for(entry in t.dataMap) {
            dataHandler.queueSave(entry.key, entry.value)
        }
    }
}