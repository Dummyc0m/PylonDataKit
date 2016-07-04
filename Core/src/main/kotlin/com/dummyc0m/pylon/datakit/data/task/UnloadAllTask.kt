package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.data.DataStore
import com.dummyc0m.pylon.datakit.data.NioDataHandler
import com.dummyc0m.pylon.datakit.data.State

/**
 * Created by Dummy on 6/14/16.
 */
class UnloadAllTask(private val store: DataStore,
                    private val dataHandler: NioDataHandler) : Runnable {
    override fun run() {
        DataKitLog.debug("Forcibly unloading all data")
        for((uuid, data) in store.dataMap) {
            if(data.state != State.LOADING) {
                dataHandler.unload(data.onlineUUID)
            }
        }
    }
}