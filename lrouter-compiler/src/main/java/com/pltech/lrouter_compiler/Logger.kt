package com.pltech.lrouter_compiler

import com.pltech.lrouter_compiler.Constants.PREFIX_OF_LOGGER
import javax.annotation.processing.Messager
import javax.tools.Diagnostic

/**
 *
 * Created by Pang Li on 2020/12/29
 */
class Logger(private val msg: Messager?) {

    /**
     * Print info log.
     */
    fun info(info: CharSequence) {
        if (!Utils.isStrEmpty(info)) {
            msg!!.printMessage(Diagnostic.Kind.NOTE, PREFIX_OF_LOGGER + info)
        }
    }

    fun error(error: CharSequence) {
        if (!Utils.isStrEmpty(error)) {
            msg!!.printMessage(Diagnostic.Kind.ERROR, PREFIX_OF_LOGGER.toString() + "An exception is encountered, [" + error + "]")
        }
    }

    fun error(error: Throwable?) {
        if (null != error) {
            msg!!.printMessage(Diagnostic.Kind.ERROR, PREFIX_OF_LOGGER.toString() + "An exception is encountered, [" + error.message + "]" + "\n" + formatStackTrace(error.stackTrace))
        }
    }

    fun warning(warning: CharSequence) {
        if (!Utils.isStrEmpty(warning)) {
            msg!!.printMessage(Diagnostic.Kind.WARNING, PREFIX_OF_LOGGER + warning)
        }
    }

    private fun formatStackTrace(stackTrace: Array<StackTraceElement>): String {
        val sb = StringBuilder()
        for (element in stackTrace) {
            sb.append("    at ").append(element.toString())
            sb.append("\n")
        }
        return sb.toString()
    }

}