package com.dummyc0m.pylon.datakit

/**
 * Created by Dummy on 6/13/16.
 */
class DataKitConfig {
    var ip = "0.0.0.0"

    var port = 8341

    var key = "defaultKey"

    val dbType = "mysql"

    val dbUrl = "jdbc:mysql://host:port/database"

    val dbUsername = "username"

    val dbPassword = "password"

    val estimateLoad = 20

    val disconnection = "Connection throttled, please try again later."

    val debug = false
}