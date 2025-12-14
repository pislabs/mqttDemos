package com.pislabs.mqtt.odapp.core.app.ui

import androidx.compose.runtime.Composable

interface UIModule {
    /**
     * 设置状态栏颜色
     */
    @Composable
    fun setStatusBarColor(isLightMode: Boolean)
}