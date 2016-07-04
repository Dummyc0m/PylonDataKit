package com.dummyc0m.pylon.datakit.bukkit

import com.dummyc0m.pylon.datakit.client.DataClient
import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import io.netty.channel.ChannelHandlerContext
import org.bukkit.Server
import org.bukkit.plugin.PluginManager

/**
 * Created by Dummy on 7/3/16.
 */
class DataMessageHandler(private val pluginManager: PluginManager,
                         private val server: Server) : MessageHandler<DataMessage> {
    internal lateinit var dataClient: DataClient

    override fun handle(ctx: ChannelHandlerContext, message: DataMessage) {
        val player = server.getPlayer(message.offlineUUID)
        if (player != null) {
            Log.info("Received data for ${message.offlineUUID}")
            val receiveEvent = DataReceiveEvent(player, message.jsonData)
            pluginManager.callEvent(receiveEvent)
        } else {
            //dereference immediately
            Log.warn("Received data for ${message.offlineUUID} with player left, dereferencing immediately")
            val delta = DeltaMessage()
            delta.offlineUUID = message.offlineUUID
            delta.dereference = true
            dataClient.send(delta)
        }
    }
}