package com.pislabs.mqtt.odapp.core.network.datasource.feedback

import com.pislabs.mqtt.odapp.core.model.entity.Feedback
import com.pislabs.mqtt.odapp.core.model.request.FeedbackSubmitRequest
import com.pislabs.mqtt.odapp.core.model.request.PageRequest
import com.pislabs.mqtt.odapp.core.model.response.NetworkPageData
import com.pislabs.mqtt.odapp.core.model.response.NetworkResponse
import com.pislabs.mqtt.odapp.core.network.base.BaseNetworkDataSource
import com.pislabs.mqtt.odapp.core.network.service.FeedbackService
import javax.inject.Inject

/**
 * 意见反馈相关数据源实现类
 * 负责处理所有与意见反馈相关的网络请求
 *
 * @param feedbackService 意见反馈服务接口，用于发起实际的网络请求
 * @author Joker.X
 */
class FeedbackNetworkDataSourceImpl @Inject constructor(
    private val feedbackService: FeedbackService
) : BaseNetworkDataSource(), FeedbackNetworkDataSource {

    /**
     * 提交意见反馈
     *
     * @param params 提交反馈的请求参数
     * @return 提交结果响应数据
     * @author Joker.X
     */
    override suspend fun submitFeedback(params: FeedbackSubmitRequest): NetworkResponse<Boolean> {
        return feedbackService.submitFeedback(params)
    }

    /**
     * 分页查询意见反馈列表
     *
     * @param params 分页查询参数，可包含页码、页大小等
     * @return 意见反馈列表响应数据
     * @author Joker.X
     */
    override suspend fun getFeedbackPage(params: PageRequest): NetworkResponse<NetworkPageData<Feedback>> {
        return feedbackService.getFeedbackPage(params)
    }
}
