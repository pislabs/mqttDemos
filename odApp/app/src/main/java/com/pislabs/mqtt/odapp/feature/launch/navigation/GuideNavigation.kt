package com.pislabs.mqtt.odapp.feature.launch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pislabs.mqtt.odapp.feature.launch.view.GuideRoute
import com.pislabs.mqtt.odapp.navigation.routes.LaunchRoutes

/**
 * 引导页面导航（类型安全版本）
 *
 * @author Joker.X
 */
fun NavGraphBuilder.guideScreen() {
    composable<LaunchRoutes.Guide> {
        GuideRoute()
    }
}
