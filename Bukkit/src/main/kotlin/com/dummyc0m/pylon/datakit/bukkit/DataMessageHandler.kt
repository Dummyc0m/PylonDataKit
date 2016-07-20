package com.dummyc0m.pylon.datakit.bukkit

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.bukkit.protect.ProtectionHandler
import com.dummyc0m.pylon.datakit.client.DataClient
import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import io.netty.channel.ChannelHandlerContext
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by Dummy on 7/3/16.
 */
class DataMessageHandler(private val plugin: JavaPlugin,
                         private val protect: ProtectionHandler) : MessageHandler<DataMessage> {
    internal lateinit var dataClient: DataClient

    override fun handle(ctx: ChannelHandlerContext, message: DataMessage) {
        val player = plugin.server.getPlayer(message.offlineUUID)
        if (player != null) {
            DataKitLog.debug("Received data ${message.toString()}")
            Bukkit.getScheduler().runTaskAsynchronously(plugin) {
                try {
                    val receiveEvent = DataReceiveEvent(player, message.jsonData)
                    plugin.server.pluginManager.callEvent(receiveEvent)
                    protect.unlock(message.offlineUUID)
                } catch (e: Exception) {
                    val delta = DeltaMessage()
                    delta.offlineUUID = player.uniqueId
                    delta.dereference = true
                    dataClient.send(delta)
                    player.kickPlayer("Error parsing data, please contact an admin.")
                }
            }
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