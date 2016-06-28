package com.dummyc0m.pylon.datakit

import com.dummyc0m.pylon.pyloncore.ConfigFile
import com.dummyc0m.pylon.pyloncore.argument.ArgumentParser
import java.io.File

/**
 * Created by Dummy on 6/11/16.
 */
fun main(args: Array<String>) {
    val parser = ArgumentParser()
    parser.option("port").mapResult

    val workingDir = File(System.getProperty("user.dir"))

    val configFile = ConfigFile<Configuration>(workingDir, "config.json", Configuration::class.java)
    val config = configFile.config;
    val ip = config.ip
    var port = config.port
    val key = config.key

    if(key.equals("defaultKey")) {
        Log.info("default key detected, generating a new key")
        config.key =
    }

    val result = parser.parse(args)
    if(result.isFlagged("port")) {
        port = result.getValue("port").toInt()
        Log.info("ignoring port value in config in favor of the cli argument")
    }

    DataKit(ip, port, key).run()
}