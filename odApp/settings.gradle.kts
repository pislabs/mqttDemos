// Gradle插件管理配置
pluginManagement {
    // 包含build-logic目录作为构建逻辑模块
    includeBuild("build-logic")
    // 配置插件仓库
    repositories {
        // Google的Maven仓库，用于Android相关依赖
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()  // Maven中央仓库
        gradlePluginPortal()    // Gradle插件门户
        // mqtt paho仓库
        maven {
            setUrl("https://repo.eclipse.org/content/repositories/paho-snapshots/")
        }
    }
}

// 依赖解析管理配置
dependencyResolutionManagement {
    // 设置仓库模式为严格模式，禁止在项目中单独配置仓库
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // 配置项目级依赖仓库
    repositories {
        google() // Google的Maven仓库
        mavenCentral() // Maven中央仓库
        // JitPack 远程仓库：https://jitpack.io
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "objectDetectionApp"

// 包含主应用模块
include(":app")

// JDK 版本检查：确保使用 JDK 17 或更高版本进行构建
check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    CoolMall 项目需要 JDK 17+ 但当前使用的是 JDK ${JavaVersion.current()}。
    Java Home: [${System.getProperty("java.home")}]
    请参考: https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}