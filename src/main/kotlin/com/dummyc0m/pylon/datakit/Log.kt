package com.dummyc0m.pylon.datakit

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by Dummy on 6/13/16.
 */
object Log {
    private val logger = Logger.getLogger("DataKit")

    fun info(msg: String) {
        logger.info(msg)
    }

    fun warn(msg: String) {
        logger.warning(msg)
    }

    fun wtf(msg: String) {
        logger.severe(msg)
    }

    fun wtf(msg: String, thrown: Throwable) {
        logger.log(Level.SEVERE, msg, thrown)
    }
}