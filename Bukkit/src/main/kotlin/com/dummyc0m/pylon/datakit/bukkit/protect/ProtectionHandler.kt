package com.dummyc0m.pylon.datakit.bukkit.protect

import java.util.*

/**
 * Created by Dummy on 7/5/16.
 */
class ProtectionHandler {
    private val lockSet: MutableSet<UUID> = HashSet()

    fun isLocked(uuid: UUID): Boolean {
        synchronized(lockSet) {
            return lockSet.contains(uuid)
        }
    }

    fun lock(uuid: UUID) {
        synchronized(lockSet) {
            lockSet.add(uuid)
        }
    }

    fun unlock(uuid: UUID) {
        synchronized(lockSet) {
            lockSet.remove(uuid)
        }
    }
}