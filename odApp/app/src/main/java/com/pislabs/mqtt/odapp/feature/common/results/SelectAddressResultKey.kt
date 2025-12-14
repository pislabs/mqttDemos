package com.pislabs.mqtt.odapp.feature.common.results

import com.pislabs.mqtt.odapp.navigation.NavigationResultKey
import kotlinx.serialization.json.Json

/**
 * 扫码结果的类型安全 NavigationResultKey
 *
 * 用于在地址选择页面和调用页面之间传递扫码结果。
 *
 * 使用示例：
 * ```kotlin
 * // 1. 发起地址选择页面
 * navigate(CommonRoutes.Scan, null)
 *
 * // 2. 在地址列表页面中返回选中的地址
 * popBackStackWithResult(SelectAddressResultKey, selectedAddress)
 *
 * // 3. 在调用页面中接收地址结果
 * navController.collectResult(SelectAddressResultKey) { address ->
 *     viewModel.onAddressSelected(address)
 * }
 * ```
 *
 */
object ScanResultKey : NavigationResultKey<String>