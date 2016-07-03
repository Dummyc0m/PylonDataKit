package com.dummyc0m.pylon.datakit.network.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 6/30/16.
 */
class DeltaMessage : Message {
    val nodeDataMap : Map<String, JsonNode>
        get() = _nodeDataMap
    private val _nodeDataMap : MutableMap<String, JsonNode> = ConcurrentHashMap()
    var offlineUUID : UUID = defaultUUID
    var dereference : Boolean = false

    fun setDelta(key: String, value: JsonNode) {
        _nodeDataMap.put(key, value)
    }

    override fun fromJson(json: JsonNode) {
        val mapNode = json.get("map")
        for((key, value) in mapNode.fields()) {
            _nodeDataMap.put(key, value)
        }
        dereference = json.get("deref").booleanValue()
        offlineUUID = UUID.fromString(json.get("uuid").textValue())
    }

    override fun toJson(): JsonNode {
        val rootNode = mapper.createObjectNode()
        val mapNode = mapper.createObjectNode()
        for((key, value) in _nodeDataMap) {
            mapNode.set(key, value)
        }
        rootNode.set("map", mapNode)
        rootNode.put("deref", dereference)
        rootNode.put("uuid", offlineUUID.toString())
        return rootNode
    }

    companion object {
        private val defaultUUID = UUID.randomUUID()
        private val mapper = ObjectMapper()
    }
}