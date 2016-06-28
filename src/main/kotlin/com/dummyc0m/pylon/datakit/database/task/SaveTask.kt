package com.dummyc0m.pylon.datakit.database.task

import com.dummyc0m.pylon.datakit.database.DataStore
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class SaveTask(val offlineUUID: UUID, val data: String, val store: DataStore): Runnable {
    override fun run() {

    }
}