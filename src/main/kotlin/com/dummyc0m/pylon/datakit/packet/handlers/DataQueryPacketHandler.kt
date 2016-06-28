package com.dummyc0m.pylon.datakit.packet.handlers

import com.dummyc0m.pylon.datakit.database.NioDataHandler
import com.dummyc0m.pylon.datakit.packet.DataPacket
import com.dummyc0m.pylon.datakit.packet.DataQueryPacket
import com.dummyc0m.pylon.datakit.packet.PacketHandler
import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/25/16.
 */
class DataQueryPacketHandler(val dataHandler: NioDataHandler) : PacketHandler<DataQueryPacket> {
    override fun handle(ctx: ChannelHandlerContext, t: DataQueryPacket) {
        dataHandler.queryData(t.offlineUUID,
                {data -> ctx.writeAndFlush(DataPacket(data.offlineUUID, data.data))})
    }
}