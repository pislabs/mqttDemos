package com.pislabs.mqtt.odapp.feature.launch.viewmodel

import com.pislabs.mqtt.odapp.core.common.base.viewmodel.BaseViewModel
import com.pislabs.mqtt.odapp.core.data.state.AppState
import com.pislabs.mqtt.odapp.core.util.storage.MMKVUtils
import com.pislabs.mqtt.odapp.navigation.AppNavigator
import com.pislabs.mqtt.odapp.navigation.routes.LaunchRoutes
import com.pislabs.mqtt.odapp.navigation.routes.MainRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    navigator: AppNavigator,
    appState: AppState
) : BaseViewModel(navigator, appState) {

    companion object {
        /**
         * 引导页已显示标记的缓存键
         */
        private const val KEY_GUIDE_SHOWN = "guide_shown"
    }

    /**
     * 检查是否已显示过引导页，并根据结果跳转到相应页面
     *
     * @author Joker.X
     */
    fun checkGuideStatusAndNavigate() {
        if (isGuideShown()) {
            // 已显示过引导页，直接跳转到主页面
            toMainPage()
        } else {
            // 未显示过引导页，跳转到引导页
            toGuidePage()
        }
    }

    /**
     * 检查是否已显示过引导页
     *
     * @return true表示已显示过，false表示未显示过
     * @author Joker.X
     */
    private fun isGuideShown(): Boolean {
        return MMKVUtils.getBoolean(KEY_GUIDE_SHOWN, false)
    }

    /**
     * 跳转到引导页并关闭当前启动页
     *
     * @author Joker.X
     */
    private fun toGuidePage() {
        navigateAndCloseCurrent(
            route = LaunchRoutes.Guide(),
            currentRoute = LaunchRoutes.Splash
        )
    }

    /**
     * 跳转到主页并关闭当前启动页
     *
     * @author Joker.X
     */
    fun toMainPage() {
        navigateAndCloseCurrent(
            route = MainRoutes.Main,
            currentRoute = LaunchRoutes.Splash
        )
    }
}