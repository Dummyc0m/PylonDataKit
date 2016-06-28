package com.dummyc0m.pylon.datakit.packet

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 6/13/16.
 */
class PacketManager {
    internal val channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    private val handlerMap: MutableMap<Any, Any> = ConcurrentHashMap()

    fun <T: Packet> registerPacket(packetClass: Class<T>, handler: PacketHandler<T>) {
        handlerMap.put(packetClass, handler)
    }

    fun broadcastPacket(packet: Packet) {
        channels.writeAndFlush(packet)
    }

    fun sendPackeet(packet: Packet, channel: Channel) {
        channel.writeAndFlush(packet)
    }

    internal fun handlePacket(ctx: ChannelHandlerContext, packet: Packet) {
        val handler = handlerMap.get(packet.javaClass)
        if(handler !== null) {
            (handler as PacketHandler<Packet>).handle(ctx, packet)
        } else {
            throw RuntimeException("Incompatible Packet from " + ctx.name())
        }
    }
}