package com.dummyc0m.pylon.datakit.network.message

import com.fasterxml.jackson.databind.JsonNode

/**
 * Created by Dummy on 6/30/16.
 */
interface Message {
    fun fromJson(json: JsonNode)

    fun toJson() : JsonNode
}