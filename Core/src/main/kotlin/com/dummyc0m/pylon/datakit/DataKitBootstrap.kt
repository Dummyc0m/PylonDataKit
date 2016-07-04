package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.pyloncore.ConfigFile
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService

/**
 * Created by Dummy on 6/11/16.
 */
class DataKitBootstrap(val executorService: ExecutorService) {
    private val configFile: ConfigFile<DataKitConfig>
    val config: DataKitConfig

    init {
        val workingDir = File(System.getProperty("user.dir"))
        configFile = ConfigFile<DataKitConfig>(workingDir,
                "dataKit.json",
                DataKitConfig::class.java)

        config = configFile.config
    }

    fun start(): DataKit {
        if(config.key.equals("defaultKey")) {
            DataKitLog.debug("default key detected, generating a new key")
            config.key = UUID.randomUUID().toString()
            configFile.save()
            DataKitLog.debug("the new key is $config.key")
        }

        return DataKit(config.ip,
                config.port,
                config.key,
                config.dbType,
                config.dbUrl,
                config.dbUsername,
                config.dbPassword,
                config.estimateLoad,
                executorService).start()
    }
}