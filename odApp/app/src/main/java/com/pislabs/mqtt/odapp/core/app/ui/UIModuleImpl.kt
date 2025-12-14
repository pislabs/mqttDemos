package com.pislabs.mqtt.odapp.core.app.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.pislabs.mqtt.odapp.core.app.module.BaseModule
import com.pislabs.mqtt.odapp.core.util.log.LogUtils
import java.util.Collections
import java.util.LinkedList

class UIModuleImpl(application: Application) : BaseModule<UIModule>(application), UIModule {
    private val TAG = "UIModuleImpl"

    /**
     * 维护Activity 的list
     */
    private val mActivityList = Collections.synchronizedList<Activity>(LinkedList())

    /**
     * 初始化
     */
    override fun onInit() {
        registerActivityListener()
    }

    // region Activity Stack

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
        this.mApplication.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
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

    // endregion

    /**
     * 设置状态栏颜色
     */
    @Composable
    override fun setStatusBarColor(isLightMode: Boolean) {
        ViewCompat.getWindowInsetsController(LocalView.current)?.isAppearanceLightStatusBars = !isLightMode
    }
}