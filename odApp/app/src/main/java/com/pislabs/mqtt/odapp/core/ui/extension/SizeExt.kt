package com.pislabs.mqtt.odapp.core.ui.extension

import android.content.res.Resources
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Dp.toPx(): Float = this.value * Resources.getSystem().displayMetrics.density

@Stable
fun Int.pxToDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp

@Stable
fun Float.pxToDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp