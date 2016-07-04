package com.dummyc0m.pylon.datakit.bukkit

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.client.DataClient
import com.dummyc0m.pylon.datakit.client.DataClientBootstrap
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by Dummy on 7/3/16.
 */
class DataKitBukkit : JavaPlugin() {
    private lateinit var dataClient: DataClient

    override fun onEnable() {
        Log.logger = logger
        DataKitLog.logger = logger
        Log.info("Starting")
        val handler = DataMessageHandler(server.pluginManager, server)
        dataClient = DataClientBootstrap(handler).start()
        handler.dataClient = dataClient
        instance = this
    }

    override fun onDisable() {
        Log.info("Bye")
        dataClient.shutdown()
    }

    fun send(deltaMessage: DeltaMessage) {
        Log.debug(deltaMessage.toString())
        dataClient.send(deltaMessage)
    }

    companion object {
        @JvmStatic lateinit var instance: DataKitBukkit
    }
}