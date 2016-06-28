package com.dummyc0m.pylon.datakit.database.task

import com.dummyc0m.pylon.datakit.database.UserData
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class QueryTask(val offlineUUID: UUID, val callback:(UserData) -> Unit) : Runnable {
    override fun run() {

    }
}