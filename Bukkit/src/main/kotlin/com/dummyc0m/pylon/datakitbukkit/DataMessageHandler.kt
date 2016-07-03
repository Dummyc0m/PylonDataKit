package com.dummyc0m.pylon.datakitbukkit

import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import io.netty.channel.ChannelHandlerContext
import org.bukkit.Server
import org.bukkit.plugin.PluginManager

/**
 * Created by Dummy on 7/3/16.
 */
class DataMessageHandler(private val pluginManager: PluginManager,
                         private val server: Server) : MessageHandler<DataMessage> {
    override fun handle(ctx: ChannelHandlerContext, message: DataMessage) {
        val player = server.getPlayer(message.offlineUUID)
        if (player != null) {
            val receiveEvent = DataReceiveEvent(player, message.jsonData)
            pluginManager.callEvent(receiveEvent)
        }
    }
}