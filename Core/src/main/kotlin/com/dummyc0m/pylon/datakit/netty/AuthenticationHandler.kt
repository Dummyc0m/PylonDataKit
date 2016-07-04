package com.dummyc0m.pylon.datakit.netty

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

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
                val id = json.get("id").textValue()
                DataKitLog.info("ServerId:$id authenticated")
                if(messageManager.contains(id)) {
                    DataKitLog.wtf(p0.name() + " Id Conflict")
                    p0.disconnect()
                    p0.close()
                } else {
                    messageManager.put(id, p0.channel())
                    p0.pipeline().remove(this)
                }
            } else {
                DataKitLog.debug(p0.name() + " Not Authenticated")
                p0.disconnect()
                p0.close()
            }
        } catch (exception: Exception) {
            DataKitLog.debug(p0.name() + " Not Authenticated")
            p0.disconnect()
            p0.close()
        }
    }
}