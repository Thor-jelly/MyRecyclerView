package com.jelly.thor.myrecyclerview.base

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jelly.thor.myrecyclerview.base.adapter.HeaderAndFooterAdapter
import com.jelly.thor.myrecyclerview.utils.CommonUtils

/**
 * 类描述：自定义带有头部和底部RV<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/16 18:54 <br/>
 */
open class HeaderAndFooterRV : RecyclerView {
    /**
     * 空布局
     */
    private var mEmptyView: View? = null

    constructor(@NonNull context: Context) : super(context)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is GridLayoutManager) {
            val spanSize = layout.spanCount
            layout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                //返回position对应的条目所占的size
                override fun getSpanSize(position: Int): Int {
                    return if (adapter is HeaderAndFooterAdapter) {
                        val headerAndFooterAdapter = adapter as HeaderAndFooterAdapter
                        val viewType = headerAndFooterAdapter.getItemViewType(position)
                        if (headerAndFooterAdapter.mHeaders.indexOfKey(viewType) >= 0
                                || headerAndFooterAdapter.mFooters.indexOfKey(viewType) >= 0) {
                            spanSize
                        } else {
                            changeSpanSize(position - headerAndFooterAdapter.mHeaders.size())
                        }
                    } else {
                        changeSpanSize(position)
                    }
                }
            }
        }
        super.setLayoutManager(layout)
    }

    /**
     * 改变GridLayoutManager 一行显示几个方法
     */
    open fun changeSpanSize(position: Int): Int {
        return 1
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (adapter == null) {
            Toast.makeText(context, "填入非null的adapter！", Toast.LENGTH_LONG).show()
            return
        }

        val newAdapter = if (adapter is HeaderAndFooterAdapter) {
            val dataObserver = object : AdapterDataObserver() {
                override fun onChanged() {
                    checkIsEmpty(adapter)
                    super.onChanged()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    checkIsEmpty(adapter)
                    super.onItemRangeRemoved(positionStart, itemCount)
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    checkIsEmpty(adapter)
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    checkIsEmpty(adapter)
                    super.onItemRangeInserted(positionStart, itemCount)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    checkIsEmpty(adapter)
                    super.onItemRangeChanged(positionStart, itemCount)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    checkIsEmpty(adapter)
                    super.onItemRangeChanged(positionStart, itemCount, payload)
                }
            }
            adapter.registerAdapterDataObserver(dataObserver)
            adapter
        } else {
            val headerAndFooterAdapter = HeaderAndFooterAdapter(adapter)
            /*
                1.下面为什么添加观察者。
                通过不是实现我自己的HeaderAndFooterAdapter，也就是所有都在这里走(HeaderAndFooterAdapter不对外提供)
                在更新数据时候，刷新不完全。
                那是因为我们刷新的adapter是给HeaderAndFooterAdapter设置的adapter，不是HeaderAndFooterAdapter
                所以我们需要添加一个观察者如果adapter更新的时候我们再跟新HeaderAndFooterAdapter。
                2.在调用remove和insert并不会导致item的position更新
                3.添加刷新数据后检测是否为空，如果为空显示数据为空的样式
             */
            val dataObserver = object : AdapterDataObserver() {
                override fun onChanged() {
                    checkIsEmpty(headerAndFooterAdapter)
                    headerAndFooterAdapter.notifyDataSetChanged()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    checkIsEmpty(headerAndFooterAdapter)
                    val newPositionStart = headerAndFooterAdapter.mHeaders.size() + positionStart
                    headerAndFooterAdapter.notifyItemRangeRemoved(newPositionStart, itemCount)
                    if (headerAndFooterAdapter.mHeaders.size() + headerAndFooterAdapter.mDataAdapter.itemCount != newPositionStart) {
                        headerAndFooterAdapter.notifyItemRangeChanged(newPositionStart, itemCount)
                    }
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    checkIsEmpty(headerAndFooterAdapter)
                    headerAndFooterAdapter.notifyItemMoved(headerAndFooterAdapter.mHeaders.size() + fromPosition, headerAndFooterAdapter.mHeaders.size() + toPosition)
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    checkIsEmpty(headerAndFooterAdapter)
                    val newPositionStart = headerAndFooterAdapter.mHeaders.size() + positionStart
                    headerAndFooterAdapter.notifyItemRangeInserted(newPositionStart, itemCount)
                    if (headerAndFooterAdapter.mHeaders.size() + headerAndFooterAdapter.mDataAdapter.itemCount != newPositionStart) {
                        headerAndFooterAdapter.notifyItemRangeChanged(newPositionStart, itemCount)
                    }
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    checkIsEmpty(headerAndFooterAdapter)
                    headerAndFooterAdapter.notifyItemRangeChanged(headerAndFooterAdapter.mHeaders.size() + positionStart, itemCount)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    checkIsEmpty(headerAndFooterAdapter)
                    headerAndFooterAdapter.notifyItemRangeChanged(headerAndFooterAdapter.mHeaders.size() + positionStart, itemCount, payload)
                }
            }
            adapter.registerAdapterDataObserver(dataObserver)
            dataObserver.onChanged()
            headerAndFooterAdapter
        }
        super.setAdapter(newAdapter)
    }

    /**
     * 添加空布局
     */
    fun addEmptyView(view: View){
        mEmptyView = view
        //把空布局添加进主布局
        mEmptyView?.let {
            (this.rootView as ViewGroup).addView(mEmptyView)
        }

        if (adapter == null) {
            return
        }

        if (adapter is HeaderAndFooterAdapter) {
            checkIsEmpty(adapter as HeaderAndFooterAdapter)
        }
    }

    /**
     * 检测是否为空
     */
    private fun checkIsEmpty(adapter: HeaderAndFooterAdapter){
        if (adapter.mDataAdapter.itemCount == 0) {
            CommonUtils.setShowModel(mEmptyView, View.VISIBLE)
            CommonUtils.setShowModel(this@HeaderAndFooterRV, View.GONE)
        } else {
            CommonUtils.setShowModel(mEmptyView, View.GONE)
            CommonUtils.setShowModel(this@HeaderAndFooterRV, View.VISIBLE)
        }
    }

    /**
     * 添加头部方法
     */
    fun addHeaderView(view: View) {
        if (adapter == null) {
            Toast.makeText(context, "请现调用setAdapter方法", Toast.LENGTH_LONG).show()
            return
        }
        //如果view添加过了就不再添加了
        if (adapter is HeaderAndFooterAdapter) {
            (adapter as HeaderAndFooterAdapter).addHeaderView(view)
        }
    }

    /**
     * 移除头部方法
     */
    fun removeHeaderView(view: View) {
        if (adapter == null) {
            Toast.makeText(context, "请现调用setAdapter方法", Toast.LENGTH_LONG).show()
            return
        }
        //如果view添加过了就不再添加了
        if (adapter is HeaderAndFooterAdapter) {
            (adapter as HeaderAndFooterAdapter).removeHeaderView(view)
        }
    }

    /**
     * 添加底部方法
     */
    fun addFooterView(view: View) {
        if (adapter == null) {
            Toast.makeText(context, "请现调用setAdapter方法", Toast.LENGTH_LONG).show()
            return
        }
        //如果view添加过了就不再添加了
        if (adapter is HeaderAndFooterAdapter) {
            (adapter as HeaderAndFooterAdapter).addFooterView(view)
        }
    }

    /**
     * 移除底部方法
     */
    fun removeFooterView(view: View) {
        if (adapter == null) {
            Toast.makeText(context, "请现调用setAdapter方法", Toast.LENGTH_LONG).show()
            return
        }
        //如果view添加过了就不再添加了
        if (adapter is HeaderAndFooterAdapter) {
            (adapter as HeaderAndFooterAdapter).removeFooterView(view)
        }
    }
}