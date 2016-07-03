package com.dummyc0m.pylon.datakitbungee

import com.dummyc0m.pylon.datakit.DataKit
import com.dummyc0m.pylon.datakit.DataKitBootstrap
import net.md_5.bungee.api.plugin.Plugin

/**
 * Created by Dummy on 7/3/16.
 */
class DataKitBungee : Plugin() {
    private lateinit var dataKit: DataKit
    override fun onEnable() {
        Log.logger = logger
        Log.info("Starting")
        dataKit = DataKitBootstrap(executorService).start()

        //start bungee
        proxy.pluginManager.registerListener(this, PluginListener(dataKit.dataHandler))
    }

    override fun onDisable() {
        dataKit.shutdown()
    }
}