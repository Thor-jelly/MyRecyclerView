package com.jelly.thor.myrecyclerview.base.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 * 类描述：有头有尾adapter,不对外提供<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/1 14:55 <br/>
 */
internal class HeaderAndFooterAdapter(internal val mDataAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val REFRESH_HEADER_KEY = 3_000_000
        const val REFRESH_FOOTER_KEY = 5_000_000
    }
    /**
     * 头部开始key
     */
    private var mHeaderKye = 3_000_001
    /**
     * 底部开始key
     */
    private var mFooterKye = 4_000_000

    /**
     * 头部
     */
    internal val mHeaders: SparseArray<View> = SparseArray()
    /**
     * 底部
     */
    internal val mFooters: SparseArray<View> = SparseArray()

    /**
     * 添加头部方法
     */
    fun addHeaderView(view: View) {
        //如果view添加过了就不再添加了
        mHeaders.takeIf {
            it.indexOfValue(view) == -1
        }?.apply {
            put(mHeaderKye++, view)
            notifyItemInserted(0)
        }
    }

    /**
     * 添加刷新头部方法
     */
    fun addRefreshHeader(view: View) {
        //如果view添加过了就不再添加了
        mHeaders.takeIf {
            it.indexOfValue(view) == -1
        }?.apply {
            put(REFRESH_HEADER_KEY, view)
            notifyItemInserted(0)
        }
    }

    /**
     * 移除头部方法
     */
    fun removeHeaderView(view: View) {
        //如果view添加过了就不再添加了
        mHeaders.takeIf {
            it.indexOfValue(view) != -1
        }?.apply {
            val indexOfValue = mHeaders.indexOfValue(view)
            removeAt(indexOfValue)
            notifyItemRemoved(indexOfValue)
        }
    }

    /**
     * 添加底部方法
     */
    fun addFooterView(view: View) {
        //如果view添加过了就不再添加了
        mFooters.takeIf {
            it.indexOfValue(view) == -1
        }?.apply {
            put(mFooterKye++, view)
            notifyItemInserted(itemCount - 1)
        }
    }

    /**
     * 添加底部方法
     */
    fun addRefreshFooterView(view: View) {
        //如果view添加过了就不再添加了
        mFooters.takeIf {
            it.indexOfValue(view) == -1
        }?.apply {
            put(REFRESH_FOOTER_KEY, view)
            notifyItemInserted(itemCount - 1)
        }
    }

    /**
     * 移除底部方法
     */
    fun removeFooterView(view: View) {
        //如果view添加过了就不再添加了
        mFooters.takeIf {
            it.indexOfValue(view) != -1
        }?.apply {
            val indexOfValue = mFooters.indexOfValue(view)
            notifyItemRemoved(indexOfValue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //viewType,可能是头，底部，中间数据
        return when {
            mHeaders.indexOfKey(viewType) >= 0 -> //头部
                onCreateHeaderFooterViewHolder(mHeaders[viewType])
            mFooters.indexOfKey(viewType) >= 0 -> //底部
                onCreateHeaderFooterViewHolder(mFooters[viewType])
            else -> mDataAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    /**
     * 创建头部和底部的ViewHolder
     */
    private fun onCreateHeaderFooterViewHolder(view: View): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return mDataAdapter.itemCount + mHeaders.size() + mFooters.size()
    }

    /**
     * 请使用getItemType
     */
    override fun getItemViewType(position: Int): Int {
        val headerSize = mHeaders.size()
        if (position < headerSize) {
            //如果是头部位置
            return mHeaders.keyAt(position)
        }

        //数据开始位置
        val newPosition = position - headerSize
        val dataSize = mDataAdapter.itemCount
        if (newPosition < dataSize) {
            //如果是数据部分返回原来用户定义的itemType
            return mDataAdapter.getItemViewType(newPosition)
        }

        //如果底部位置
        val newFooterPosition = newPosition - dataSize
        return mFooters.keyAt(newFooterPosition)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val headerSize = mHeaders.size()
        if (position < headerSize) {
            //如果是头部位置, 不需要绑定数据
            return
        }

        //数据开始位置
        val newPosition = position - headerSize
        val dataSize = mDataAdapter.itemCount
        if (newPosition < dataSize) {
            //如果是数据部分,才需要绑定数据
            mDataAdapter.onBindViewHolder(viewHolder, newPosition)
            return
        }

        //如果底部位置
        //val newFooterPosition = newPosition - dataSize
        //return
    }
}