package com.dummyc0m.pylon.datakit.database

import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
data class UserData(val onlineUUID: UUID,
                    val offlineUUID: UUID,
                    var state: State,
                    val data: String)

enum class State {
    LOADING, //received load packet, loading data
    AWAITING_QUERY, //loaded data, awaiting query (switch to this after leaving one server)
    CONNECTED, //connected to a server
    AWAITING_SAVE //disconnected but have not received save packet yet
}