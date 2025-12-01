package com.pislabs.mqtt.odapp.core.network.service

import com.pislabs.mqtt.odapp.core.model.entity.Coupon
import com.pislabs.mqtt.odapp.core.model.request.PageRequest
import com.pislabs.mqtt.odapp.core.model.request.ReceiveCouponRequest
import com.pislabs.mqtt.odapp.core.model.response.NetworkPageData
import com.pislabs.mqtt.odapp.core.model.response.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 优惠券相关接口
 *
 * @author Joker.X
 */
interface CouponService {

    /**
     * 领取优惠券
     *
     * @param request 领取优惠券请求参数
     * @return 领取结果响应
     * @author Joker.X
     */
    @POST("market/coupon/user/receive")
    suspend fun receiveCoupon(@Body request: ReceiveCouponRequest): NetworkResponse<String>

    /**
     * 分页查询用户优惠券
     *
     * @param params 分页请求参数
     * @return 用户优惠券分页数据响应
     * @author Joker.X
     */
    @POST("market/coupon/user/page")
    suspend fun getUserCouponPage(@Body params: PageRequest): NetworkResponse<NetworkPageData<Coupon>>

    /**
     * 查询用户优惠券列表
     *
     * @param params 查询参数
     * @return 用户优惠券列表响应
     * @author Joker.X
     */
    @POST("market/coupon/user/list")
    suspend fun getUserCouponList(@Body params: Any): NetworkResponse<List<Coupon>>
}
