package com.pltech.lrouter

import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 *
 * Created by Pang Li on 2020/12/29
 */
object LRouter {
    private lateinit var context: Context
    private lateinit var routePathCache: MutableMap<String, Class<*>>

    @JvmStatic
    fun init(context: Context) {
        this.context = context
        routePathCache = HashMap()
        loadRoutePath()
    }

    private fun loadRoutePath() {
        val klassNames = Utils.getClassName(context, "com.pltech.lrouter.generated")
        if (klassNames != null) {
            for (klassName in klassNames) {
                val klass = Class.forName(klassName)
                if (IRoute::class.java.isAssignableFrom(klass)) {
                    val route = klass.newInstance() as IRoute
                    route.register()
                }
            }
        }
    }

    /**
     * 注册路由表
     */
    fun addRoutePath(key: String, activity: Class<*>) {
        routePathCache[key] = activity
    }

    /**
     * 执行Activity跳转，根据路由地址跳转，可携带参数
     */
    fun jumpTo(path: String, extras: Bundle?) {
        val activityClass = routePathCache[path]
        if (activityClass == null) {
            throw ClassNotFoundException("没有找到目标Activity类:$activityClass")
        }
        val intent = Intent()
        intent.setClass(context, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (extras != null) {
            intent.putExtras(extras)
        }
        context.startActivity(intent)
    }

}