package com.dummyc0m.pylon.datakit.packet.handlers

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.DataUnloadPacket
import com.dummyc0m.pylon.datakit.packet.PacketHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/25/16.
 */
class DataUnloadPacketHandler(val dataHandler: NioDataHandler) : PacketHandler<DataUnloadPacket> {
    override fun handle(ctx: ChannelHandlerContext, t: DataUnloadPacket) {
        dataHandler.unload(t.onlineUUID)
    }
}