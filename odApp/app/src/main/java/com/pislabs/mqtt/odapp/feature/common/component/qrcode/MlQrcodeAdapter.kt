package com.pislabs.mqtt.odapp.feature.common.component.qrcode

import android.graphics.Bitmap
import androidx.core.util.Consumer
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

/**
 * mlkit扫码处理
 */
class MlQrcodeAdapter(private val client: BarcodeScanner = BarcodeScanning.getClient()) : QrcodeAdapter {
    override fun onScan(bitmap: Bitmap, result: Consumer<String>) {
        client.process(InputImage.fromBitmap(bitmap, 0)).addOnSuccessListener { list ->
            if (list.isNotEmpty()) {
                list[0].rawValue?.takeUnless { it.isEmpty() }?.also {
                    result.accept(it)
                }
            }
        }
    }
}
