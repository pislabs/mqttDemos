package com.pislabs.mqtt.odapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pislabs.mqtt.odapp.app.AppNavHost
import com.pislabs.mqtt.odapp.core.common.config.ThemePreference
import com.pislabs.mqtt.odapp.core.common.manager.ThemePreferenceManager
import com.pislabs.mqtt.odapp.core.designsystem.theme.AppTheme
import com.pislabs.mqtt.odapp.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        // 启动页
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // 启用边缘到边缘的显示效果
        enableEdgeToEdge()
        // 设置Compose内容
        setContent {
            val themeMode by ThemePreferenceManager.themeMode.collectAsState()
            val themeColorOption by ThemePreferenceManager.themeColor.collectAsState()
            val isDarkTheme = when (themeMode) {
                ThemePreference.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
            }
            val primaryColor = remember(themeColorOption) {
                Color(themeColorOption.colorHex)
            }
            // 应用主题包装
            AppTheme(
                darkTheme = isDarkTheme,
                themeColor = primaryColor
            ) {
                // 设置应用的导航宿主，并传入导航管理器和路由注册器
                // 这样所有页面都可以通过导航管理器进行导航操作
                AppNavHost(navigator = navigator)
            }
        }

        // 不让启动界面一直显示
        splashScreen.setKeepOnScreenCondition {
            false
        }
    }
}