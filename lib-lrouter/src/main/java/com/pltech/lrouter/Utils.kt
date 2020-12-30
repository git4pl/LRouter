package com.pltech.lrouter

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import dalvik.system.DexFile
import java.io.IOException
import java.util.*

/**
 *
 * Created by Pang Li on 2020/12/29
 */
object Utils {
    @JvmStatic
    fun getClassName(context: Context, packageName: String?): List<String>? {
        val classNameList: MutableList<String> = ArrayList()
        try {
            //获取路径
            val path = context.packageCodePath
            Log.w("Util", "path1==$path")
            val path2 = context.packageManager.getApplicationInfo(
                    context.packageName, 0).sourceDir
            Log.w("Util", "path2==$path2")
            //            DexFile df = new DexFile(path);//通过DexFile查找当前的APK中可执行文件
            val df = DexFile(path2) //通过DexFile查找当前的APK中可执行文件
            val enumeration = df.entries() //获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            var count = 0
            while (enumeration.hasMoreElements()) { //遍历
                val className = enumeration.nextElement()
                Log.d("Util", className)
                if (className.contains(packageName!!)) { //在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className)
                    count++
                }
            }
            Log.e("Util", "获取到的类个数:$count")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return classNameList
    }
}