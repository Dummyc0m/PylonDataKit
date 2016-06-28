package com.dummyc0m.pylon.datakit.database

import com.dummyc0m.pylon.datakit.database.task.*
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by Dummy on 6/14/16.
 */
class NioDataHandler(val factory: DBConnectionFactory, estimateLoad: Int) {
    private val store: DataStore
    private val service: ExecutorService

    init {
        store = DataStore(estimateLoad)
        service = ThreadPoolExecutor(4, Int.MAX_VALUE, 60000,
                TimeUnit.MILLISECONDS, SynchronousQueue())
    }

    fun init() {
        val connection = factory.create()
        val statement = connection.createStatement()

        val sql = "CREATE TABLE IF NOT EXISTS DataKit(" +
                "`Id` int NOT NULL AUTO_INCREMENT, " +
                "`Uuid` char(36) NULL, " +
                "`Data` text NULL, " +
                "PRIMARY KEY(`Id`), " +
                "INDEX `uuid_index` (`Uuid`));";
        statement.execute(sql)
        statement.closeOnCompletion()
        connection.close()
    }

    fun queueLoad(onlineUUID: UUID, offlineUUID: UUID) {
        store.storeUser(UserData(onlineUUID, offlineUUID, State.LOADING, store.EMPTY))
        service.submit(LoadTask(onlineUUID, store))
    }

    fun queueSave(offlineUUID: UUID, data: String) {
        service.submit(SaveTask(offlineUUID, data, store))
    }

    fun unload(onlineUUID: UUID) {
        service.submit(UnloadTask(onlineUUID))
    }

    fun queryData(offlineUUID: UUID, callback:(UserData) -> Unit) {
        service.submit(QueryTask(offlineUUID, callback))
    }

    fun terminateSave() {
        service.submit(TerminateSaveTask(store))
        service.awaitTermination(60, TimeUnit.SECONDS)
    }
}