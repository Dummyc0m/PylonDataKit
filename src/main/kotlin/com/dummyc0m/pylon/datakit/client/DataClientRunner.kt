package com.dummyc0m.pylon.datakit.client

/**
 * Created by Dummy on 6/13/16.
 */
fun main(args: Array<String>) {
    val dataClient = DataClient("127.0.0.1", 8341, "defaultKey")
    dataClient.start()
}