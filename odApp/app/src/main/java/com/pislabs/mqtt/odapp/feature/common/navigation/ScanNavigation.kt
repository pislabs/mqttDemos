package com.pislabs.mqtt.odapp.feature.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pislabs.mqtt.odapp.feature.common.view.ScanRoute
import com.pislabs.mqtt.odapp.navigation.routes.CommonRoutes

/**
 * 扫码页面导航
 *
 */
fun NavGraphBuilder.scanScreen() {
    composable<CommonRoutes.Scan> {
        ScanRoute()
    }
}
