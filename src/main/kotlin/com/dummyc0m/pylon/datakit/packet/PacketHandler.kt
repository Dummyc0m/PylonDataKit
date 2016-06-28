package com.dummyc0m.pylon.datakit.packet

import io.netty.channel.ChannelHandlerContext

/**
 * Created by Dummy on 6/13/16.
 */
interface PacketHandler<T: Packet> {
    /**
     * Inbound
     */
    fun handle(ctx: ChannelHandlerContext, t: T)
}