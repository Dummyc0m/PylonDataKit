package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.packet.Packet
import com.dummyc0m.pylon.datakit.packet.PacketManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * Created by Dummy on 6/13/16.
 */
class DataClientHandler(val packetManager: PacketManager) : SimpleChannelInboundHandler<Packet>(Packet::class.java) {
    override fun channelRead0(p0: ChannelHandlerContext, p1: Packet) {
        packetManager.handlePacket(p0, p1)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        //ctx.close()
    }
}