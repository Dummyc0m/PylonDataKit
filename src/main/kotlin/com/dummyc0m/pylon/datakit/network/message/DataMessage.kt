package com.dummyc0m.pylon.datakit.network.message

import com.dummyc0m.pylon.datakit.data.UserData
import com.fasterxml.jackson.databind.JsonNode

/**
 * Created by Dummy on 6/30/16.
 */
class DataMessage : Message {
    private var _jsonData: JsonNode = UserData.emptyData
    val jsonData: JsonNode
        get() = _jsonData

    override fun fromJson(json: JsonNode) {
        _jsonData = json
    }

    override fun toJson(): JsonNode {
        return _jsonData
    }
}