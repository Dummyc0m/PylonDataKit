package com.dummyc0m.pylon.datakit.data

import com.dummyc0m.pylon.datakit.data.task.*
import com.dummyc0m.pylon.datakit.network.MessageManager
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import com.fasterxml.jackson.databind.JsonNode
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by Dummy on 6/14/16.
 */
class NioDataHandler(private val factory: DBConnectionFactory,
                     estimateLoad: Int,
                     private val messageManager: MessageManager) {
    private val store: DataStore
    private val service: ExecutorService

    init {
        store = DataStore(estimateLoad)
        service = ThreadPoolExecutor(4, Int.MAX_VALUE, 60000,
                TimeUnit.MILLISECONDS, SynchronousQueue())
    }

    internal  fun init() {
        val connection = factory.create()
        val statement = connection.createStatement()

        val sql = "CREATE TABLE IF NOT EXISTS PylonDataKit(" +
                "`Id` int NOT NULL AUTO_INCREMENT, " +
                "`Uuid` char(36) NULL, " +
                "`Data` text NULL, " +
                "PRIMARY KEY(`Id`), " +
                "INDEX `uuid_index` (`Uuid`));";
        statement.execute(sql)
        statement.closeOnCompletion()
        connection.close()
    }

    fun load(onlineUUID: UUID, offlineUUID: UUID, callback:() -> Unit) {
        store.storeUser(UserData(onlineUUID, offlineUUID, State.LOADING))
        service.submit(LoadTask(factory, onlineUUID, store, callback))
    }

    fun save(offlineUUID: UUID, data: JsonNode) {
        service.submit(FeedbackTask(offlineUUID, data, store))
    }

    fun unload(onlineUUID: UUID) {
        service.submit(UnloadTask(onlineUUID, store, factory))
    }

    fun send(offlineUUID: UUID, serverId: String) {
        service.submit(SendTask(offlineUUID, store, serverId, messageManager))
    }

    fun unloadAll() {
        service.submit(UnloadAllTask(store))
        service.awaitTermination(60, TimeUnit.SECONDS)
    }
}