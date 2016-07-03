package com.dummyc0m.pylon.datakit.data.task

import com.dummyc0m.pylon.datakit.data.State
import com.dummyc0m.pylon.datakit.data.UserData
import com.dummyc0m.pylon.pyloncore.DBConnectionFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class LoadTask(private val dbConnectionFactory: DBConnectionFactory,
               private val onlineUUID: UUID,
               private val userData: UserData,
               private val callback:() -> Unit): Runnable {
    override fun run() {
        val connection = dbConnectionFactory.create()
        val get = connection.prepareStatement(GET_DATA)
        get.setString(1, onlineUUID.toString())
        val resultSet = get.executeQuery()
        if(!resultSet.next()) {
            val new = connection.prepareStatement(INSERT_NEW)
            new.setString(1, onlineUUID.toString())
            new.execute()
            new.close()
            userData.data = mapper.createObjectNode()
        } else {
            userData.data = mapper.readTree(resultSet.getString(1)) as ObjectNode
        }
        userData.state = State.LOADED
        callback.invoke()
    }

    companion object {
        private val mapper = ObjectMapper()
        private val GET_DATA = "SELECT `Data` FROM `PylonDataKit` WHERE `Uuid` = ?"
        private val INSERT_NEW = "INSERT INTO `PylonDataKit`(`Uuid`) VALUES(?)"
    }
}