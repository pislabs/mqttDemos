package com.pislabs.mqtt.odapp.core.network.datasource.goods

import com.pislabs.mqtt.odapp.core.model.entity.Category
import com.pislabs.mqtt.odapp.core.model.entity.Comment
import com.pislabs.mqtt.odapp.core.model.entity.Goods
import com.pislabs.mqtt.odapp.core.model.entity.GoodsSearchKeyword
import com.pislabs.mqtt.odapp.core.model.entity.GoodsSpec
import com.pislabs.mqtt.odapp.core.model.request.GoodsCommentPageRequest
import com.pislabs.mqtt.odapp.core.model.request.GoodsCommentSubmitRequest
import com.pislabs.mqtt.odapp.core.model.request.GoodsSearchRequest
import com.pislabs.mqtt.odapp.core.model.response.NetworkPageData
import com.pislabs.mqtt.odapp.core.model.response.NetworkResponse

/**
 * 商品相关数据源接口
 *
 * @author Joker.X
 */
interface GoodsNetworkDataSource {

    /**
     * 查询商品分类列表
     *
     * @return 商品分类列表响应
     * @author Joker.X
     */
    suspend fun getGoodsTypeList(): NetworkResponse<List<Category>>

    /**
     * 查询商品规格列表
     *
     * @param params 查询参数
     * @return 商品规格列表响应
     * @author Joker.X
     */
    suspend fun getGoodsSpecList(params: Map<String, Long>): NetworkResponse<List<GoodsSpec>>

    /**
     * 查询搜索关键词列表
     *
     * @return 搜索关键词列表响应
     * @author Joker.X
     */
    suspend fun getSearchKeywordList(): NetworkResponse<List<GoodsSearchKeyword>>

    /**
     * 分页查询商品
     *
     * @param params 商品搜索请求参数
     * @return 商品分页数据响应
     * @author Joker.X
     */
    suspend fun getGoodsPage(params: GoodsSearchRequest): NetworkResponse<NetworkPageData<Goods>>

    /**
     * 获取商品信息
     *
     * @param id 商品ID
     * @return 商品信息响应
     * @author Joker.X
     */
    suspend fun getGoodsInfo(id: String): NetworkResponse<Goods>

    /**
     * 提交商品评论
     *
     * @param params 评论提交请求参数
     * @return 提交结果响应
     * @author Joker.X
     */
    suspend fun submitGoodsComment(params: GoodsCommentSubmitRequest): NetworkResponse<Boolean>

    /**
     * 分页查询商品评论
     *
     * @param params 评论分页请求参数
     * @return 评论分页数据响应
     * @author Joker.X
     */
    suspend fun getGoodsCommentPage(params: GoodsCommentPageRequest): NetworkResponse<NetworkPageData<Comment>>
}
