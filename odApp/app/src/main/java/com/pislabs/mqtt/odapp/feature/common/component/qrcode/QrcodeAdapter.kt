package com.pislabs.mqtt.odapp.feature.common.component.qrcode

import android.graphics.Bitmap
import androidx.core.util.Consumer

interface QrcodeAdapter {
    fun onScan(bitmap: Bitmap, result: Consumer<String>)
}