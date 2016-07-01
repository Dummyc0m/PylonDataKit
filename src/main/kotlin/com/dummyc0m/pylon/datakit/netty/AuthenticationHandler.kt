package com.dummyc0m.pylon.datakit.netty

import com.dummyc0m.pylon.datakit.Log
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.ssl.SslHandler
import java.net.InetAddress

/**
 * Created by Dummy on 6/13/16.
 */
class AuthenticationHandler(private val key: String, private val messageManager: MessageManager):
        SimpleChannelInboundHandler<String>(String::class.java) {
    private val mapper = ObjectMapper()

    override fun channelRead0(p0: ChannelHandlerContext, p1: String) {
        try {
            val json = mapper.readTree(p1)
            if (key.equals(json.get("key")?.textValue()) && json.has("id")) {
                Log.info(p0.name() + " Authenticated")
                val id = json.get("id").textValue()
                if(messageManager.contains(id)) {
                    Log.wtf(p0.name() + " Id Conflict")
                    p0.disconnect()
                    p0.close()
                } else {
                    messageManager.put(id, p0.channel())
                    p0.pipeline().remove(this)
                }
            } else {
                Log.info(p0.name() + " Not Authenticated")
                p0.disconnect()
                p0.close()
            }
        } catch (exception: Exception) {
            Log.info(p0.name() + " Not Authenticated")
            p0.disconnect()
            p0.close()
        }
    }
}