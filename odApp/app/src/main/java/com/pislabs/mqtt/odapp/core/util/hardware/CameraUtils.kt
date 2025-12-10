package com.pislabs.mqtt.odapp.core.util.hardware

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.pislabs.mqtt.odapp.core.common.manager.AppManager
import com.pislabs.mqtt.odapp.core.util.permission.PermissionUtils


object CameraUtils {
    /**
     * 获取摄像头管理器
     */
    fun getCameraManager(): CameraManager? {
        var manager: CameraManager? = null

        PermissionUtils.requestCameraPermission(AppManager.application) {
            manager = AppManager.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        }

        return manager
    }

    fun openCameraTorch() {
        val manager = getCameraManager()
//        manager.setTorchMode()
    }

    /**
     * 获取前置摄像头Id
     * type: 0
     */
    fun getCameraIdByType(type: Int): String {
        val manager = getCameraManager()

        if (manager == null || manager.cameraIdList.size == 0) {
            return ""
        }

        // 查找前置摄像头
        val cameraId = manager.cameraIdList.find { it -> type.toString() == it }

        if (cameraId.isNullOrEmpty()) {
            return ""
        }

        return cameraId
    }

    /**
     * 获取默认摄像头Id
     */
    fun getDefaultCameraId(): String {
        val manager = getCameraManager()

        if (manager == null || manager.cameraIdList.size == 0) {
            return ""
        }

        // 查找前置摄像头
        var cameraId = manager.cameraIdList.find { it -> CameraCharacteristics.LENS_FACING_FRONT.toString() == it }

        if (cameraId.isNullOrEmpty()) {
            return ""
        }


        val chara = manager.getCameraCharacteristics(cameraId)


        return cameraId
    }
}