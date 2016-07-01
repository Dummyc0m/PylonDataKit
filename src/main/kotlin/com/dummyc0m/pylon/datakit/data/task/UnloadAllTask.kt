package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.Log
import com.dummyc0m.pylon.datakit.data.DataStore

/**
 * Created by Dummy on 6/14/16.
 */
class UnloadAllTask(private val store: DataStore) : Runnable {
    override fun run() {
        Log.info("Waiting for users to unload")
        //TODO loop over references and check 0
    }
}