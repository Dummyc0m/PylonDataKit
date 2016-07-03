package com.dummyc0m.pylon.datakitbukkit

import com.fasterxml.jackson.databind.node.ObjectNode
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Created by Dummy on 7/3/16.
 */
class DataReceiveEvent(val player: Player, val jsonData: ObjectNode) : Event(true) {

    override fun getHandlers(): HandlerList {
        return DataReceiveEvent.handlerList
    }

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }
}