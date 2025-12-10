package com.pislabs.mqtt.odapp.feature.main.model

import android.content.Context

/**
 * 顶级AppBar活动
 *
 * @param label action 标签
 * @param value action 值
 * @param action 活动方法
 */
enum class AppBarAction (
    val label: String,
    val value: Int,
    val action: (context: Context) -> Unit
) {
    SCAN("扫一扫", 1, {})
}