package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.data.DataStore
import com.fasterxml.jackson.databind.JsonNode
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class FeedbackTask(val offlineUUID: UUID, val data: JsonNode, val store: DataStore): Runnable {
    override fun run() {
        //todo dereference the data
    }
}