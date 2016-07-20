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
        val handler = DataMessageHandler(this, protect)
        val bootstrap = DataClientBootstrap(handler)
        //todo remove dev
        if (!bootstrap.config.devDisableLock) {
            server.pluginManager.registerEvents(ProtectionListener(protect), this)
        }
        //todo remove all debug log
        DataKitLog.debug = bootstrap.config.debug
        DataKitLog.info("Starting")
        //todo remove dev
        if (!bootstrap.config.devDisableClient) {
            dataClient = bootstrap.start()
            handler.dataClient = dataClient
        }
        _instance = this
    }

    override fun onDisable() {
        DataKitLog.info("Bye")
        dataClient.shutdown()
    }

    fun send(deltaMessage: DeltaMessage) {
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