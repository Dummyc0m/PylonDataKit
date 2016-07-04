package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.data.UserData
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.dummyc0m.pylon.datakit.network.message.DataMessage
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class SendTask(private val offlineUUID: UUID,
               private val data: UserData?,
               private val serverId: String,
               private val messageManager: MessageManager) : Runnable {
    override fun run() {
        if (data != null && data.data !== UserData.emptyData) {
            data.reference()
            val dataMessage = DataMessage()
            dataMessage.offlineUUID = offlineUUID
            dataMessage.jsonData = data.data
            messageManager.send(dataMessage, serverId)
        } else {
            DataKitLog.warn("data does not exist for (offlineUUID) $offlineUUID when sending to $serverId")
        }
    }
}