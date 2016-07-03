package com.dummyc0m.pylon.datakit.network.message

import com.dummyc0m.pylon.datakit.data.UserData
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*

/**
 * Created by Dummy on 6/30/16.
 */
class DataMessage : Message {
    var jsonData: ObjectNode = UserData.emptyData
    var offlineUUID = defaultUUID

    override fun fromJson(json: JsonNode) {
        jsonData = json.get("data") as ObjectNode
        offlineUUID = UUID.fromString(json.get("uuid").textValue())
    }

    override fun toJson(): JsonNode {
        val rootNode = mapper.createObjectNode()
        rootNode.set("data", jsonData)
        rootNode.put("uuid", offlineUUID.toString())
        return jsonData
    }

    companion object {
        private val mapper = ObjectMapper()
        private val defaultUUID = UUID.randomUUID()
    }
}