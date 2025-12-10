package com.pislabs.mqtt.odapp.feature.common.component.qrcode

import android.graphics.Bitmap
import androidx.core.util.Consumer
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

/**
 * Zxing扫码处理
 */
class ZxingQrcodeAdapter(private val reader: MultiFormatReader = MultiFormatReader()) : QrcodeAdapter {
    override fun onScan(bitmap: Bitmap, result: Consumer<String>) {
        IntArray(bitmap.width * bitmap.height).let { pixels ->
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            runCatching {
                reader.decode(
                    BinaryBitmap(
                        HybridBinarizer(
                            RGBLuminanceSource(
                                bitmap.width,
                                bitmap.height,
                                pixels
                            )
                        )
                    )
                ).also {
                    if (it.text.isNotEmpty()) {
                        result.accept("zxing scan result is ${it.text}")
                    }
                }
            }
        }
    }
}
