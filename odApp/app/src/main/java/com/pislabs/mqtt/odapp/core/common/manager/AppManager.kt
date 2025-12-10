package com.pislabs.mqtt.odapp.core.common.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.pislabs.mqtt.odapp.core.util.log.LogUtils
import java.util.Collections
import java.util.LinkedList

/**
 * 应用管理器，获取系统应用相关服务
 */
object AppManager {
    private const val TAG = "AppManager"

    private lateinit var mApplication: Application

    /**
     * 维护Activity 的list
     */
    private val mActivityList = Collections.synchronizedList<Activity>(LinkedList())

    /**
     * 当前应用
     */
    val application: Application
        get() {
            return this.mApplication
        }

    /**
     * 初始化
     */
    fun init(application: Application) {
        this.mApplication = application

        registerActivityListener()
    }

    /**
     * @return 获取当前最顶部activity的实例
     */
    fun getTopActivity(): Activity? {
        synchronized(mActivityList) {
            if (mActivityList.isNullOrEmpty()) {
                return null
            }

            return mActivityList.last()
        }
    }

    /**
     * 获取顶部激活Activity
     */
    fun getTopActiveActivity(): Activity? {
        synchronized(mActivityList) {
            if (mActivityList.isEmpty()) {
                return null
            }

            val topActivity = mActivityList.findLast { it -> !it.isFinishing }

            return topActivity
        }
    }

    /**
     * 判断指定Activity是否已加载
     */
    fun isValidActivity(activityClassName: String?): Boolean {
        return findValidActivity(activityClassName) == null
    }

    /**
     * 查找当前有效的Activity
     */
    fun findValidActivity(activityClassName: String?): Activity? {
        if (activityClassName.isNullOrEmpty()) {
            return null
        }

        val targetActivity = mActivityList.find { it -> activityClassName == it.javaClass.simpleName }
        return targetActivity
    }

    /**
     * 注册Activity Listener
     */
    private fun registerActivityListener() {
        this.mApplication.registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                //监听到 Activity创建事件 将该 Activity 加入list
                pushActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                LogUtils.d(TAG,  "$activity onActivityStarted")
            }

            override fun onActivityDestroyed(activity: Activity) {
                LogUtils.d(TAG,  "$activity onActivityDestroyed")

                if (mActivityList.isNullOrEmpty()) {
                    return
                }

                if (mActivityList.contains(activity)) {
                    //监听到 Activity销毁事件 将该Activity 从list中移除
                    removeActivity(activity)
                }
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle
            ) {}

            override fun onActivityStopped(activity: Activity) {}
        })
    }


    /**
     * @param activity 添加一个activity到管理里
     */
    fun pushActivity(activity: Activity) {
        mActivityList.add(activity)
    }

    /**
     * @param activity 删除一个activity在管理里
     */
    fun removeActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    /**
     * 获取系统服务
     */
    fun getSystemService(name: String): Any {
        return application.getSystemService(name)
    }
}