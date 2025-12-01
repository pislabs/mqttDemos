plugins {
    alias(libs.plugins.odapp.android.application.compose)
    alias(libs.plugins.odapp.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        // 仅包括中文和英文必要的语言资源
        androidResources {
            localeFilters += listOf("zh", "en")
        }
    }

    // ABI 分包配置 - 一次性打包多个架构版本
    splits {
        abi {
            // 启用 ABI 分包
            isEnable = true
            // 重置默认列表
            reset()
            // 包含的架构：32位和64位 ARM
            include("armeabi-v7a", "arm64-v8a")
            // 是否生成通用 APK（包含所有架构）
            // 设置为 true 会额外生成一个包含所有架构的 APK
            isUniversalApk = false
        }
    }

    signingConfigs {
        // 通用签名配置
        // 实际使用时请替换为自己的签名文件
//        create("common") {
//            // 哪个签名文件
//            storeFile = file("pislabs_open_key.keystore")
//            // 密钥别名
//            keyAlias = "pislabs_open_key"
//            // 密钥密码
//            keyPassword = "pislabs123456"
//            // 签名文件密码
//            storePassword = "pislabs123456"
//
//            // 启用所有签名方案以确保最大兼容性
//            enableV1Signing = true  // JAR 签名 (Android 1.0+)
//            enableV2Signing = true  // APK 签名 v2 (Android 7.0+)
//            enableV3Signing = true  // APK 签名 v3 (Android 9.0+)
//            enableV4Signing = true  // APK 签名 v4 (Android 11.0+)
//        }
    }

    buildTypes {
        debug {
            // debug 模式下也使用正式签名配置 - 方便调试支付以及三方登录等功能
//            signingConfig = signingConfigs["common"] // 没有签名请注释掉这行使用默认签名
            // debug 模式下包名后缀
            applicationIdSuffix = ".debug"
        }
        release {
//            signingConfig = signingConfigs["common"] // 没有签名请注释掉这行使用默认签名
            // 是否启用代码压缩
            isMinifyEnabled = true
            // 资源压缩
            isShrinkResources = true
            // 配置ProGuard规则文件
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }
}

dependencies {
    // region 依赖注入
    // https://developer.android.google.cn/training/dependency-injection/hilt-android?hl=zh-cn
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    //endregion

    compileOnly(libs.ksp.gradlePlugin)

    // 启动页
    implementation(libs.androidx.core.splashscreen)

    // LeakCanary - 内存泄漏检测工具（仅在debug构建中使用）
    // https://github.com/square/leakcanary
    debugImplementation(libs.leakcanary.android)

    // kotlin序列化
    implementation(libs.kotlinx.serialization.json)

    // 吐司框架：https://github.com/getActivity/Toaster
    implementation(libs.toaster)

    // 权限框架：https://github.com/getActivity/XXPermissions
    implementation(libs.xxpermissions)

    // 腾讯存储 https://github.com/Tencent/MMKV
    implementation(libs.mmkv)

    //日志框架
    // https://github.com/JakeWharton/timber
    implementation(libs.timber)

    // 网络相关
    implementation(libs.okhttp3)
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp.logging)

    // 通过OkHttp的拦截器机制
    // 实现在应用通知栏显示网络请求功能
    // https://github.com/ChuckerTeam/chucker
    // debug 下的依赖
    debugImplementation(libs.chucker)
    // prod 下的空依赖
    releaseImplementation(libs.chucker.no.op)
}