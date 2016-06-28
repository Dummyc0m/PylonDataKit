package com.dummyc0m.pylon.datakit.packet

import java.util.*

/**
 * Created by Dummy on 6/13/16.
 */
data class DataLoadPacket(val onlineUUID: UUID, val offlineUUID: UUID): Packet

data class DataUnloadPacket(val onlineUUID: UUID): Packet

data class DataUnloadAllPacket(val onlineUUIDs: List<UUID>): Packet

/**
 * Client -> Server, query data to load
 * Server -> Client, query data to save
 */
data class DataQueryPacket(val offlineUUID: UUID): Packet

data class DataSavePacket(val offlineUUID: UUID, val data: String): Packet

data class DataSaveAllPacket(val dataMap: Map<UUID, String>): Packet

data class DataPacket(val offlineUUID: UUID, val data: String): Packet

data class DataFailurePacket(val onlineUUID: UUID, val offlineUUID: UUID): Packet