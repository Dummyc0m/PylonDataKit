package com.dummyc0m.pylon.datakit.bungee

import com.dummyc0m.pylon.datakit.DataKit
import com.dummyc0m.pylon.datakit.DataKitBootstrap
import com.dummyc0m.pylon.datakit.DataKitLog
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin

/**
 * Created by Dummy on 7/3/16.
 */
class DataKitBungee : Plugin() {
    private lateinit var dataKit: DataKit
    override fun onEnable() {
        DataKitLog.logger = logger
        val bootstrap = DataKitBootstrap(executorService)
        DataKitLog.debug = bootstrap.config.debug
        DataKitLog.info("Starting")
        dataKit = bootstrap.start()

        //start bungee
        proxy.pluginManager.registerListener(this, PluginListener(dataKit.dataHandler, TextComponent(bootstrap.config.disconnection)))
    }

    override fun onDisable() {
        DataKitLog.info("Bye")
        dataKit.shutdown()
    }
}