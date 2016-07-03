package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.Log
import com.dummyc0m.pylon.datakit.data.DataStore
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class SendTask(private val offlineUUID: UUID,
               private val store: DataStore,
               private val serverId: String,
               private val messageManager: MessageManager) : Runnable {
    override fun run() {
        val data = store.getUserDataOffline(offlineUUID)
        if (data != null) {
            data.reference()
            val dataMessage = DataMessage()
            dataMessage.offlineUUID = offlineUUID
            dataMessage.jsonData = data.data
            messageManager.send(dataMessage, serverId)
        } else {
            Log.wtf("data does not exist for (offlineUUID) $offlineUUID when sending to $serverId")
        }
    }
}