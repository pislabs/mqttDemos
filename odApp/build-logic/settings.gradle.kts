// 依赖解析管理配置
dependencyResolutionManagement {
    // 配置依赖仓库
    repositories {
        google()
        mavenCentral()
    }
    // 配置版本目录
    versionCatalogs {
        // 创建一个名为"libs"的版本目录
        create("libs") {
            // 从项目根目录的gradle/libs.versions.toml文件中加载版本信息
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

// 设置build-logic项目的名称
rootProject.name = "build-logic"
include(":convention")
