package com.dummyc0m.pylon.datakit.packet.handlers

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.DataUnloadAllPacket
import com.dummyc0m.pylon.datakit.packet.PacketHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/25/16.
 */
class DataUnloadAllPacketHandler(val dataHandler: NioDataHandler) : PacketHandler<DataUnloadAllPacket> {
    override fun handle(ctx: ChannelHandlerContext, t: DataUnloadAllPacket) {
        for(entry in t.onlineUUIDs) {
            dataHandler.unload(entry)
        }
    }
}