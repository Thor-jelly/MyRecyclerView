package com.jelly.thor.myrecyclerview.base.refreshinterface

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * 类描述：上拉加载 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/20 18:26 <br/>
 */
interface PullUpListener {
    /**
     * 获取上拉加载更多的开始View
     */
    fun getLoadViewStart(context: Context): View? {
        return null
    }

    /**
     * 获取上拉加载更多的正在加载View
     */
    fun getLoadViewing(context: Context): View? {
        return null
    }

    /**
     * 获取上拉加载更多的加载成功View
     */
    fun getLoadViewSuccess(context: Context): View? {
        return null
    }

    /**
     * 获取上拉加载更多的加载失败View
     */
    fun getLoadViewFailure(context: Context): View? {
        return null
    }

    /**
     * 获取上拉加载更多的没有更多数据的View
     */
    fun getLoadViewEnd(context: Context): View? {
        return null
    }

    /**
     * 正在加载中
     */
    fun onLoading() {}
}