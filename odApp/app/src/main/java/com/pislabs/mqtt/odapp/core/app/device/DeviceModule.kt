package com.pislabs.mqtt.odapp.core.app.device

import androidx.compose.runtime.Composable

interface DeviceModule {
    fun openTorch(enabled: Boolean)
}