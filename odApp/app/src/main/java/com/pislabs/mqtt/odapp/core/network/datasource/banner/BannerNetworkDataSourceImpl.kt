package com.pislabs.mqtt.odapp.core.network.datasource.banner

import com.pislabs.mqtt.odapp.core.model.response.NetworkResponse
import com.pislabs.mqtt.odapp.core.network.base.BaseNetworkDataSource
import com.pislabs.mqtt.odapp.core.network.service.BannerService
import javax.inject.Inject

/**
 * 轮播图相关数据源实现类
 * 负责处理所有与轮播图相关的网络请求
 *
 * @param bannerService 轮播图服务接口，用于发起实际的网络请求
 * @author Joker.X
 */
class BannerNetworkDataSourceImpl @Inject constructor(
    private val bannerService: BannerService
) : BaseNetworkDataSource(), BannerNetworkDataSource {

    /**
     * 查询轮播图列表
     *
     * @param params 请求参数，可包含位置、类型等筛选条件
     * @return 轮播图列表响应数据
     * @author Joker.X
     */
    override suspend fun getBannerList(params: Any): NetworkResponse<Any> {
        return bannerService.getBannerList(params)
    }
} 