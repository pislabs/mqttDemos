package com.pislabs.mqtt.odapp.core.ui.extension

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

//Modifier 的扩展方法支持防抖功能
fun Modifier.debouncedClickable(enabled: Boolean = true, ripple: Boolean = true, delay: Long = 300, onClick: () -> Unit) =
    composed {
        var clicked by remember {
            mutableStateOf(!enabled)
        }

        LaunchedEffect(key1 = clicked, block = {
            if (clicked) {
                delay(delay)
                clicked = !clicked
            }
        })

        Modifier.clickable(
            indication = if (ripple) LocalIndication.current else null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = if (enabled) !clicked else false
        ) {
            clicked = !clicked
            onClick()
        }
    }