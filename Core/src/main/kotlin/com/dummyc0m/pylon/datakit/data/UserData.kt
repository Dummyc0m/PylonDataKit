package com.dummyc0m.pylon.datakit.data

import com.dummyc0m.pylon.datakit.Log
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Dummy on 6/14/16.
 */
class UserData(val onlineUUID: UUID,
               val offlineUUID: UUID,
               var state: State) {
    var data : ObjectNode
        get() = synchronized(_data) {
            return _data
        }
        set(value) = synchronized(_data) {
            _data = value
        }
    private var _data: ObjectNode = UserData.emptyData
    private var _references : AtomicInteger = AtomicInteger()
    val references : Int
        get() = _references.get()

    fun patch(nodeDataMap: Map<String, JsonNode>) {
        synchronized(_data) {
            if(!_data.isObject) {
                Log.wtf("_data is null")
            }
            for((key, delta) in nodeDataMap) {
                val nodes = key.split(".".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if(nodes.size > 0) {
                    var previousNode = _data
                    var currentNode = previousNode.get(nodes.first()) as ObjectNode?
                    for(i in 0..nodes.size - 1) {
                        currentNode = previousNode.get(nodes.get(i)) as ObjectNode?
                        if(currentNode == null ) {
                            currentNode = ObjectNode(factory)
                            previousNode.set(nodes.get(0), currentNode)
                        }
                        previousNode = currentNode
                    }
                    currentNode?.set(nodes.last(), delta)
                }
            }
        }
    }

    fun reference() {
        _references.incrementAndGet()
    }

    fun dereference() {
        _references.decrementAndGet()
    }

    companion object {
        val factory = JsonNodeFactory(false)
        val emptyData = factory.objectNode()
    }
}

enum class State {
    LOADING, //received load network, loading data
    LOADED, //loaded data, awaiting query (switch to this after leaving one server)
    CONNECTED, //connected to a server
    FEEDBACK //disconnected but have not received save(feedback) network yet
}