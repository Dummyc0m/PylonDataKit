package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.datakit.packet.Packet
import com.dummyc0m.pylon.datakit.packet.PacketManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.group.ChannelGroup
import io.netty.handler.ssl.SslHandler
import java.net.InetAddress

/**
 * Created by Dummy on 6/13/16.
 */
class DataKitServerHandler(val packetManager: PacketManager) : SimpleChannelInboundHandler<Packet>(Packet::class.java) {
    internal val channels: ChannelGroup = packetManager.channels

    override fun channelRead0(p0: ChannelHandlerContext, p1: Packet) {
        packetManager.handlePacket(p0, p1)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler::class.java).handshakeFuture().addListener {
            ctx.writeAndFlush(
                    "Welcome to " + InetAddress.getLocalHost().hostName + " secure chat service!\n")
            ctx.writeAndFlush(
                    "Your session is protected by " +
                            ctx.pipeline().get(SslHandler::class.java).engine().session.cipherSuite +
                            " cipher suite.\n")

            channels.add(ctx.channel())
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        ctx.pipeline().get(SslHandler::class.java).sslCloseFuture().addListener {
            channels.remove(ctx.channel())
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        //ctx.close()
    }
}