package com.pislabs.mqtt.odapp.core.app

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import com.pislabs.mqtt.odapp.core.app.device.DeviceModule
import com.pislabs.mqtt.odapp.core.app.device.DeviceModuleImpl
import com.pislabs.mqtt.odapp.core.app.ui.UIModule
import com.pislabs.mqtt.odapp.core.app.ui.UIModuleImpl

/**
 * 应用管理器，获取系统应用相关服务
 */
object App {
    private const val TAG = "AppManager"

    private lateinit var mApplication: Application

    private lateinit var mUIModule: UIModule

    private lateinit var mDeviceModule: DeviceModule

    /**
     * 当前应用
     */
    val application: Application
        get() = this.mApplication

    /**
     * ui Manager
     */
    val ui: UIModule
        get() = this.mUIModule

    /**
     * ui Manager
     */
    val device: DeviceModule
        get() = this.mDeviceModule

    /**
     * 初始化
     */
    fun init(application: Application) {
        this.mApplication = application

        // 初始化ui模块
        this.mUIModule = UIModuleImpl(application).init()

        // 初始化设备模块
        this.mDeviceModule = DeviceModuleImpl(application).init()
    }

    /**
     * 获取系统服务
     */
    fun getSystemService(name: String): Any {
        return this.mApplication.getSystemService(name)
    }

    /**
     * 获取照相机管理器
     */
    fun getCameraManager(): CameraManager {
        return getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}