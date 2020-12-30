package com.pltech.lrouter_compiler

/**
 *
 * Created by Pang Li on 2020/12/29
 */
object Utils {
    @JvmStatic
    fun isStrEmpty(cs: CharSequence?): Boolean {
        return cs == null || cs.isEmpty()
    }

    @JvmStatic
    fun isMapEmpty(map: MutableMap<String, String>?): Boolean {
        return map == null || map.isEmpty()
    }

    @JvmStatic
    fun isCollectionEmpty(coll: Collection<Any>?): Boolean {
        return coll == null || coll.isEmpty();
    }
}