package com.dummyc0m.pylon.datakitbukkit

import com.dummyc0m.pylon.datakit.client.DataClient
import com.dummyc0m.pylon.datakit.client.DataClientBootstrap
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import com.dummyc0m.pylon.datakitbungee.Log
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by Dummy on 7/3/16.
 */
class DataKitBukkit : JavaPlugin() {
    private lateinit var dataClient: DataClient

    override fun onEnable() {
        Log.logger = logger
        dataClient = DataClientBootstrap(DataMessageHandler(server.pluginManager, server)).start()
    }

    override fun onDisable() {
        dataClient.shutdown()
    }

    fun send(deltaMessage: DeltaMessage) {
        dataClient.send(deltaMessage)
    }
}