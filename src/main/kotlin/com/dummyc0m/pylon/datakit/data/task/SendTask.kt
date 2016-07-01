package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.data.DataStore
import com.dummyc0m.pylon.datakit.network.MessageManager
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class SendTask(private val offlineUUID: UUID,
               private val store: DataStore,
               private val serverId: String,
               private val messageManager: MessageManager) : Runnable {
    override fun run() {
        //todo reference the data
    }
}