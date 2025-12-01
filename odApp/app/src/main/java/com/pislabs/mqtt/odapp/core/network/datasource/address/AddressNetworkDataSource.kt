package com.pislabs.mqtt.odapp.core.network.datasource.address

import com.pislabs.mqtt.odapp.core.model.common.Id
import com.pislabs.mqtt.odapp.core.model.common.Ids
import com.pislabs.mqtt.odapp.core.model.entity.Address
import com.pislabs.mqtt.odapp.core.model.request.PageRequest
import com.pislabs.mqtt.odapp.core.model.response.NetworkPageData
import com.pislabs.mqtt.odapp.core.model.response.NetworkResponse

/**
 * 用户地址相关数据源接口
 *
 * @author Joker.X
 */
interface AddressNetworkDataSource {

    /**
     * 修改地址
     *
     * @param params 地址信息
     * @return 修改结果响应
     * @author Joker.X
     */
    suspend fun updateAddress(params: Address): NetworkResponse<Unit>

    /**
     * 分页查询地址
     *
     * @param params 分页请求参数
     * @return 地址分页数据响应
     * @author Joker.X
     */
    suspend fun getAddressPage(params: PageRequest): NetworkResponse<NetworkPageData<Address>>

    /**
     * 查询地址列表
     *
     * @return 地址列表响应
     * @author Joker.X
     */
    suspend fun getAddressList(): NetworkResponse<List<Address>>

    /**
     * 删除地址
     *
     * @param params 地址ID列表
     * @return 删除结果响应
     * @author Joker.X
     */
    suspend fun deleteAddress(params: Ids): NetworkResponse<Unit>

    /**
     * 新增地址
     *
     * @param params 地址信息
     * @return 新增地址ID响应
     * @author Joker.X
     */
    suspend fun addAddress(params: Address): NetworkResponse<Id>

    /**
     * 获取地址信息
     *
     * @param id 地址ID
     * @return 地址信息响应
     * @author Joker.X
     */
    suspend fun getAddressInfo(id: Long): NetworkResponse<Address>

    /**
     * 获取默认地址
     *
     * @return 默认地址响应
     * @author Joker.X
     */
    suspend fun getDefaultAddress(): NetworkResponse<Address?>
} 