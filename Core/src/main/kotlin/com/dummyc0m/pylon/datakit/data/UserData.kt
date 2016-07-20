package com.dummyc0m.pylon.datakit.data

import com.dummyc0m.pylon.datakit.DataKitLog
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
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
    private var _data: ObjectNode = emptyData
    private var _references : AtomicInteger = AtomicInteger()
    val references : Int
        get() = _references.get()

    fun patch(nodeDataMap: Map<String, JsonNode>) {
        DataKitLog.debug("Patching user data")
        synchronized(_data) {
            if (_data === emptyData) {
                DataKitLog.wtf("_data is null")
            }
            for((key, delta) in nodeDataMap) {
                val nodes = key.split(".")//.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (nodes.size > 1) {
                    var previousNode = _data
                    var currentNode = previousNode.get(nodes.first()) as ObjectNode?
                    for (i in 0..(nodes.size - 2)) {
                        currentNode = previousNode.get(nodes.get(i)) as ObjectNode?
                        if (currentNode === null) {
                            currentNode = ObjectNode(factory)
                            previousNode.set(nodes.get(i), currentNode)
                        }
                        previousNode = currentNode
                    }
                    if (currentNode !== null) {
                        val lastNode = currentNode.get(nodes.last())
                        if (lastNode !== null) {
                            when (lastNode) {
                                is IntNode -> {
                                    if (delta is IntNode) {
                                        currentNode.put(nodes.last(), lastNode.asInt() + delta.asInt())
                                    } else {
                                        currentNode.set(nodes.last(), delta)
                                    }
                                }
                                is LongNode -> {
                                    if (delta is LongNode) {
                                        currentNode.put(nodes.last(), lastNode.asLong() + delta.asLong())
                                    } else {
                                        currentNode.set(nodes.last(), delta)
                                    }
                                }
                                is DoubleNode -> {
                                    if (delta is DoubleNode) {
                                        currentNode.put(nodes.last(), lastNode.asDouble() + delta.asDouble())
                                    } else {
                                        currentNode.set(nodes.last(), delta)
                                    }
                                }
                                is FloatNode -> {
                                    if (delta is FloatNode) {
                                        currentNode.put(nodes.last(), lastNode.asDouble() + delta.asDouble())
                                    } else {
                                        currentNode.set(nodes.last(), delta)
                                    }
                                }
                                else -> currentNode.set(nodes.last(), delta)
                            }
                        } else {
                            currentNode.set(nodes.last(), delta)
                        }
                    }
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

    override fun toString(): String {
        return "UserData(onlineUUID=$onlineUUID, offlineUUID=$offlineUUID, state=$state, _data=$_data, _references=$_references)"
    }

    companion object {
        val factory = JsonNodeFactory(false)
        val emptyData = factory.objectNode()
    }
}

enum class State {
    LOADING, //received load network, loading data
    LOADED, //loaded data
    FEEDBACK, //disconnected but have not received save(feedback) network yet
    UNLOADING
}