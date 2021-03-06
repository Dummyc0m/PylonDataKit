package com.dummyc0m.pylon.datakit

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by Dummy on 6/13/16.
 */
object DataKitLog {
    lateinit var logger: Logger
    var debug: Boolean = false

    fun info(msg: String) {
        logger.info(msg)
    }

    fun debug(msg: String) {
        if (debug) {
            logger.info(msg)
        }
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