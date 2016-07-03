package com.dummyc0m.pylon.datakitbungee

import com.dummyc0m.pylon.datakit.data.NioDataHandler
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.*

/**
 * Created by Dummy on 7/3/16.
 */
class PluginListener(private val dataHandler: NioDataHandler) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PostLoginEvent) {
        val player = event.player
        dataHandler.load(player.uniqueId, player.getOfflineUniqueId()) {
            if (player.isConnected && player.server != null) {
                dataHandler.send(player.getOfflineUniqueId(),
                        player.uniqueId, player.server.info.name)
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerDisconnectEvent) {
        dataHandler.unload(event.player.uniqueId)
    }

    @EventHandler
    fun onServerConnected(event: ServerConnectedEvent) {
        dataHandler.send(event.player.getOfflineUniqueId(),
                event.player.uniqueId, event.server.info.name)
    }

    fun ProxiedPlayer.getOfflineUniqueId(): UUID {
        return UUID.nameUUIDFromBytes("OfflinePlayer:${this.name}".toByteArray(Charsets.UTF_8))
    }
}