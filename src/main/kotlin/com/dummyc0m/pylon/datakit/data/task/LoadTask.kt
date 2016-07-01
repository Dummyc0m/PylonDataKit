package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.data.DataStore
import com.dummyc0m.pylon.datakit.data.UserData
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class LoadTask(private val dbConnectionFactory: DBConnectionFactory,
               private val onlineUUID: UUID,
               private val store: DataStore,
               private val callback:() -> Unit): Runnable {
    override fun run() {
        //todo
    }

    companion object {
        private val GET_DATA = "SELECT `Data` FROM `PylonDataKit` WHERE `Uuid` = ?"
        private val INSERT_NEW = "INSERT INTO `PylonDataKit`(`Uuid`) VALUES(?)"
    }
}