package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.data.DataStore
import com.dummyc0m.pylon.datakit.data.NioDataHandler
import com.dummyc0m.pylon.datakit.data.State
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage

/**
 * Created by Dummy on 6/14/16.
 */
class FeedbackTask(private val deltaMessage: DeltaMessage,
                   private val store: DataStore,
                   private val dataHandler: NioDataHandler): Runnable {
    override fun run() {
        val data = store.getUserDataOffline(deltaMessage.offlineUUID)
        if(data != null) {
            data.patch(deltaMessage.nodeDataMap)
            if(deltaMessage.dereference) {
                data.dereference()
            }
            if(data.state === State.FEEDBACK && data.references === 0) {
                DataKitLog.debug("Unloading (onlineUUID) ${data.onlineUUID}")
                dataHandler.unload(data.onlineUUID)
            }
        }
    }
}