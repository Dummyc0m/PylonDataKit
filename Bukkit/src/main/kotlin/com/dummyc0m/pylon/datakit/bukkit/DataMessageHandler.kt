package com.dummyc0m.pylon.datakit.bukkit

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.bukkit.protect.ProtectionHandler
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
                         private val server: Server,
                         private val protect: ProtectionHandler) : MessageHandler<DataMessage> {
    internal lateinit var dataClient: DataClient

    override fun handle(ctx: ChannelHandlerContext, message: DataMessage) {
        val player = server.getPlayer(message.offlineUUID)
        if (player != null) {
            DataKitLog.debug("Received data for ${message.offlineUUID}")
            val receiveEvent = DataReceiveEvent(player, message.jsonData)
            pluginManager.callEvent(receiveEvent)
            protect.unlock(message.offlineUUID)
        } else {
            //dereference immediately
            DataKitLog.warn("Received data for ${message.offlineUUID} with player left, dereferencing immediately")
            val delta = DeltaMessage()
            delta.offlineUUID = message.offlineUUID
            delta.dereference = true
            dataClient.send(delta)
        }
    }
}