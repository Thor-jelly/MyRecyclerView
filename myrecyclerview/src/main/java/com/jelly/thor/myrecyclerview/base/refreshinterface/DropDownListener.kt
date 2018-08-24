package com.jelly.thor.myrecyclerview.base.refreshinterface

import android.content.Context
import android.view.View

/**
 * 类描述：下拉刷新<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/20 18:25 <br/>
 */
interface DropDownListener {
    /**
     * 获取下拉加载更多的开始View
     */
    fun getRefreshViewStart(context: Context): View? {
        return null
    }

    /**
     * 获取下拉加载更多的正在加载View
     */
    fun getRefreshViewing(context: Context): View? {
        return null
    }

    /**
     * 获取下拉加载更多的加载成功View
     */
    fun getRefreshViewSuccess(context: Context): View? {
        return null
    }

    /**
     * 获取下拉加载更多的加载失败View
     */
    fun getRefreshViewFailure(context: Context): View? {
        return null
    }

    /**
     * 正在刷新
     */
    fun onRefreshing() {}
}