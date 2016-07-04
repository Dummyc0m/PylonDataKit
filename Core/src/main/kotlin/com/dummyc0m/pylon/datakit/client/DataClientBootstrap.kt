package com.dummyc0m.pylon.datakit.client

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.network.MessageHandler
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import com.dummyc0m.pylon.pyloncore.ConfigFile
import java.io.File

/**
 * Created by Dummy on 7/3/16.
 */
class DataClientBootstrap(val dataMessageHandler: MessageHandler<DataMessage>) {
    private val configFile: ConfigFile<ClientConfig>
    val config: ClientConfig

    init {
        val workingDir = File(System.getProperty("user.dir"))
        configFile = ConfigFile<ClientConfig>(workingDir,
                "dataKit.json",
                ClientConfig::class.java)

        config = configFile.config
    }

    fun start(): DataClient {
        if (config.key.equals("defaultKey")) {
            DataKitLog.info("default key detected, please edit the config")
        }

        return DataClient(config.ip,
                config.port,
                config.key,
                config.serverId,
                dataMessageHandler).start()
    }
}