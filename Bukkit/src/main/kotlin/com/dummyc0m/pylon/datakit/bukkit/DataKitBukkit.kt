package com.dummyc0m.pylon.datakit.bukkit

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.bukkit.protect.ProtectionHandler
import com.dummyc0m.pylon.datakit.bukkit.protect.ProtectionListener
import com.dummyc0m.pylon.datakit.client.DataClient
import com.dummyc0m.pylon.datakit.client.DataClientBootstrap
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by Dummy on 7/3/16.
 */
class DataKitBukkit : JavaPlugin() {
    private lateinit var dataClient: DataClient

    override fun onEnable() {
        DataKitLog.logger = logger
        val protect = ProtectionHandler()
        val handler = DataMessageHandler(server.pluginManager, server, protect)
        server.pluginManager.registerEvents(ProtectionListener(protect), this)
        val bootstrap = DataClientBootstrap(handler)
        DataKitLog.debug = bootstrap.config.debug
        DataKitLog.info("Starting")
        dataClient = bootstrap.start()
        handler.dataClient = dataClient
        _instance = this
    }

    override fun onDisable() {
        DataKitLog.info("Bye")
        dataClient.shutdown()
    }

    fun send(deltaMessage: DeltaMessage) {
        DataKitLog.debug(deltaMessage.toString())
        dataClient.send(deltaMessage)
    }

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        return if ("reload".equals(label, true)) true else false
    }

    companion object {
        @JvmStatic val instance: DataKitBukkit
            get() = _instance
        private lateinit var _instance: DataKitBukkit
    }
}