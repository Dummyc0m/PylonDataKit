package com.dummyc0m.pylon.datakit

/**
 * Created by Dummy on 6/13/16.
 */
class Configuration {
    var ip = "0.0.0.0"

    var port = 8341

    var key = "defaultKey"

    var tablePrefic = "pylon_"

    val dbType = "mysql"

    val dbUrl = "databaseUrl"

    val dbUsername = "username"

    val dbPassword = "password"

    val estimateLoad = 20

    val saveInterval = 600

    val timeout = 10
}