package com.dummyc0m.pylon.datakit.network.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 6/30/16.
 */
class DeltaMessage : Message {
    val nodeDataMap : Map<String, JsonNode>
        get() = _nodeDataMap
    private val _nodeDataMap : MutableMap<String, JsonNode> = ConcurrentHashMap()

    override fun fromJson(json: JsonNode) {
        for((key, value) in json.fields()) {
            _nodeDataMap.put(key, value)
        }
    }

    override fun toJson(): JsonNode {
        val objectNode = mapper.createObjectNode()
        for((key, value) in _nodeDataMap) {
            objectNode.set(key, value)
        }
        return objectNode
    }

    companion object {
        private val mapper = ObjectMapper()
    }
}