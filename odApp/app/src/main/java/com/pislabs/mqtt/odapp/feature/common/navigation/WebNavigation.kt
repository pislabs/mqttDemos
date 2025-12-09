package com.pislabs.mqtt.odapp.feature.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pislabs.mqtt.odapp.feature.common.view.WebRoute
import com.pislabs.mqtt.odapp.navigation.routes.CommonRoutes

/**
 * 网页页面导航
 *
 * @author Joker.X
 */
fun NavGraphBuilder.webScreen() {
    composable<CommonRoutes.Web> {
        WebRoute()
    }
}
