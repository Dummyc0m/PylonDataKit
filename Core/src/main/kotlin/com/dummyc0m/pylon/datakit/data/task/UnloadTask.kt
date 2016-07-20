package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.DataKitLog
import com.dummyc0m.pylon.datakit.data.DataStore
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import java.sql.SQLException
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 * only call when reference is 0
 */
class UnloadTask(private val onlineUUID: UUID,
                 private val store: DataStore,
                 private val factory: DBConnectionFactory) : Runnable {
    override fun run() {
        try {
            val connection = factory.create()
            val statement = connection.prepareStatement(SAVE)
            val userData = store.getUserDataOnline(onlineUUID)
            if (userData != null) {
                statement.setString(1, userData.data.toString())
                statement.setString(2, onlineUUID.toString())
                statement.execute()
                store.deleteUserOnline(onlineUUID)
                DataKitLog.debug("Successfully unloaded (onlineUUID) $onlineUUID")
            } else {
                DataKitLog.debug("(onlineUUID) $onlineUUID does not exist when unloading")
            }
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            DataKitLog.wtf("Failed to unload and save user data (OnlineUUID) $onlineUUID", e)
        }
    }

    companion object {
        private val SAVE = "UPDATE `PylonDataKit` SET `Data` = ? WHERE `Uuid` = ?"
    }
}