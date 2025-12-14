package com.pislabs.mqtt.odapp.core.app.device

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.pislabs.mqtt.odapp.core.app.App
import com.pislabs.mqtt.odapp.core.app.module.BaseModule
import com.pislabs.mqtt.odapp.core.util.log.LogUtils
import java.util.Collections
import java.util.LinkedList

class DeviceModuleImpl(application: Application) : BaseModule<DeviceModule>(application), DeviceModule {
    private val TAG = "DeviceModuleImpl"

    /**
     * 初始化
     */
    override fun onInit() {
    }

    /**
     * 打开或关闭手电通
     */
    override fun openTorch(enabled: Boolean) {
        val cameraManager = App.getCameraManager()
        cameraManager.setTorchMode("0", enabled);
    }
}