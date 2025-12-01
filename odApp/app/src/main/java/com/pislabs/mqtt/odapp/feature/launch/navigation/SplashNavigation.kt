package com.pislabs.mqtt.odapp.feature.launch.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pislabs.mqtt.odapp.feature.launch.view.SplashRoute
import com.pislabs.mqtt.odapp.navigation.routes.LaunchRoutes

/**
 * 启动页面导航
 *
 * @param sharedTransitionScope 共享转换作用域
 * @author Joker.X
 */
@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.splashScreen(sharedTransitionScope: SharedTransitionScope) {
    composable<LaunchRoutes.Splash> {
        SplashRoute(sharedTransitionScope, this@composable)
    }
}
