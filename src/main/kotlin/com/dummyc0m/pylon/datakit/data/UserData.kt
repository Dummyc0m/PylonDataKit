package com.dummyc0m.pylon.datakit.data

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.NullNode
import java.util.*

/**
 * Created by Dummy on 6/14/16.
 */
class UserData(val onlineUUID: UUID,
               val offlineUUID: UUID,
               var state: State) {
    internal var data: JsonNode = UserData.emptyData
    private var references = 0

    fun patch(data: JsonNode) {
        //TODO
        synchronized(this) {

        }
    }

    fun reference() {
        synchronized(references) {
            references++
        }
    }

    fun dereference() {
        synchronized(references) {
            references--
        }
    }

    companion object {
        val emptyData : NullNode = JsonNodeFactory(false).nullNode()
    }
}

enum class State {
    LOADING, //received load network, loading data
    LOADED, //loaded data, awaiting query (switch to this after leaving one server)
    CONNECTED, //connected to a server
    AWAITING_SAVE //disconnected but have not received save network yet
}