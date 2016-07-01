package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.pyloncore.ConfigFile
import com.dummyc0m.pylon.pyloncore.argument.ArgumentParser
import java.io.File
import java.util.*

/**
 * Created by Dummy on 6/11/16.
 */
class DataKitBootstrap {
    private val configFile: ConfigFile<Configuration>
    val config: Configuration

    init {
        val workingDir = File(System.getProperty("user.dir"))
        configFile = ConfigFile<Configuration>(workingDir, "config.json", Configuration::class.java)

        config = configFile.config;
    }

    fun start(): DataKit {
        if(config.key.equals("defaultKey")) {
            Log.info("default key detected, generating a new key")
            config.key = UUID.randomUUID().toString()
            configFile.save()
            Log.info("the new key is $config.key")
        }

        return DataKit(config.ip,
                config.port,
                config.key,
                config.dbType,
                config.dbUrl,
                config.dbUsername,
                config.dbPassword,
                config.estimateLoad).start()
    }
}