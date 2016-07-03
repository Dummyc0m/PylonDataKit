package com.dummyc0m.pylon.datakit.data

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 6/14/16.
 */
class DataStore(estimateLoad: Int) {
    internal val dataMap: MutableMap<UUID, UserData>
    private val uuidMap: MutableMap<UUID, UUID>

    init {
        dataMap = ConcurrentHashMap(estimateLoad)
        uuidMap = ConcurrentHashMap(estimateLoad)
    }

    fun storeUser(data: UserData) {
        dataMap.put(data.onlineUUID, data)
        uuidMap.put(data.offlineUUID, data.onlineUUID)
    }

    fun deleteUserOnline(onlineUUID: UUID) {
        uuidMap.remove(dataMap.get(onlineUUID)?.offlineUUID)
        dataMap.remove(onlineUUID)
    }

    fun deleteUserOffline(offlineUUID: UUID) {
        dataMap.remove(uuidMap.get(offlineUUID))
        uuidMap.remove(offlineUUID)
    }

    fun getUserDataOnline(onlineUUID: UUID): UserData? {
        return dataMap.get(onlineUUID)
    }

    fun getUserDataOffline(offlineUUID: UUID): UserData? {
        return dataMap.get(uuidMap.get(offlineUUID))
    }

    fun getOnlineUUID(offlineUUID: UUID): UUID? {
        return uuidMap.get(offlineUUID)
    }

    fun getOfflineUUID(onlineUUID: UUID): UUID? {
        return dataMap.get(onlineUUID)?.offlineUUID
    }
}