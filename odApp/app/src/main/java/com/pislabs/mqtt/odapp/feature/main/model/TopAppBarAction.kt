package com.pislabs.mqtt.odapp.feature.main.model

import com.pislabs.mqtt.odapp.core.util.toast.ToastUtils

/**
 * 顶级AppBar活动
 *
 * @param label action 标签
 * @param value action 值
 * @param action 活动方法
 */
enum class TopAppBarAction (
    val label: String,
    val value: Int,
    val action: () -> Unit
) {
    SCAN("扫一扫", 1, {
        ToastUtils.show("调用扫一扫功能");
    })
}