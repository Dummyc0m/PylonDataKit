package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.datakit.packet.AuthMessage
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * Created by Dummy on 6/13/16.
 */
class AuthenticationHandler(val key: String): SimpleChannelInboundHandler<Any>(Any::class.java) {
    override fun channelRead0(p0: ChannelHandlerContext, p1: Any) {
        if(p1 is AuthMessage && p1.key.equals(key)) {
            Log.info(p0.name() + " Authenticated")
            p0.pipeline().remove(this)
        } else {
            Log.info(p0.name() + " Not Authenticated")
            p0.disconnect()
            p0.close()
        }
    }
}