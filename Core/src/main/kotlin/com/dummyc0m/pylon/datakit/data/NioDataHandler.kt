package com.dummyc0m.pylon.datakit.data

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.data.task.*
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by Dummy on 6/14/16.
 */
class NioDataHandler(private val factory: DBConnectionFactory,
                     estimateLoad: Int,
                     private val messageManager: MessageManager,
                     private val service: ExecutorService) {
    private val store: DataStore

    init {
        store = DataStore(estimateLoad)
    }

    internal  fun init() {
        val connection = factory.create()
        val statement = connection.createStatement()

        val sql = "CREATE TABLE IF NOT EXISTS PylonDataKit(" +
                "`Id` int NOT NULL AUTO_INCREMENT, " +
                "`Uuid` char(36) NULL, " +
                "`Data` text NULL, " +
                "PRIMARY KEY(`Id`), " +
                "INDEX `uuid_index` (`Uuid`));"
        statement.execute(sql)
        statement.closeOnCompletion()
        connection.close()
    }

    /**
     * callback, true if successfully loaded
     */
    fun load(onlineUUID: UUID, offlineUUID: UUID, callback: (Boolean) -> Unit) {
        DataKitLog.debug("Checking (onlineUUID) $onlineUUID")
        val prevData = store.getUserDataOnline(onlineUUID)
        if (prevData != null) {
            if (prevData.state == State.FEEDBACK && prevData.references !== 0) {
                prevData.state = State.LOADED
                DataKitLog.debug("(onlineUUID) $onlineUUID already loaded, using it")
                callback.invoke(true)
            } else {
                DataKitLog.warn("(onlineUUID) $onlineUUID beep beep, connection throttled")
                callback.invoke(false)
            }
        } else {
            DataKitLog.debug("Loading (onlineUUID) $onlineUUID")
            val userData = UserData(onlineUUID, offlineUUID, State.LOADING)
            store.storeUser(userData)
            service.submit(LoadTask(factory, onlineUUID, userData, callback))
        }
    }

    internal fun feedback(deltaMessage: DeltaMessage) {
        service.submit(FeedbackTask(deltaMessage, store, this))
    }

    fun unload(onlineUUID: UUID) {
        store.getUserDataOnline(onlineUUID)?.state = State.UNLOADING
        service.submit(UnloadTask(onlineUUID, store, factory))
    }

    fun offline(onlineUUID: UUID) {
        val data = store.getUserDataOnline(onlineUUID)
        if (data != null && data.state !== State.UNLOADING) {
            data.state = State.FEEDBACK
            if (data.references === 0) {
                unload(onlineUUID)
            }
        }
    }

    fun send(offlineUUID: UUID, onlineUUID: UUID, serverId: String) {
        DataKitLog.debug("Sending (onlineUUID) $onlineUUID to $serverId")
        service.submit(SendTask(offlineUUID, store.getUserDataOnline(onlineUUID), serverId, messageManager))
    }

    fun unloadAll() {
        service.submit(UnloadAllTask(store, this))
        service.awaitTermination(10, TimeUnit.SECONDS)
    }
}