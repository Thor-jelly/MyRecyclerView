package com.jelly.thor.myrecyclerview.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jelly.thor.myrecyclerview.base.clicklistener.OnClickListener
import com.jelly.thor.myrecyclerview.base.clicklistener.OnLongClickListener
import com.jelly.thor.myrecyclerview.base.viewholder.BaseViewHolder

/**
 * 类描述：基础adapter<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/1 14:55 <br/>
 */
abstract class RVAdapter<DATA> constructor(protected val mContext: Context, protected val mData: MutableList<DATA>?) : RecyclerView.Adapter<BaseViewHolder>() {
    /**
     * item点击监听
     */
    private var mOnClickListener: OnClickListener<DATA>? = null
    /**
     * item长按监听
     */
    private var mOnLongClickListener: OnLongClickListener<DATA>? = null

    /**
     * item点击事件
     */
    fun setOnClickListener(onClickListener: OnClickListener<DATA>) {
        mOnClickListener = onClickListener
    }

    /**
     * item长按事件
     */
    fun setOnLongClickListener(onLongClickListener: OnLongClickListener<DATA>) {
        mOnLongClickListener = onLongClickListener
    }


    /**
     * 获取layoutId
     */
    abstract fun getLayoutId(viewType: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(mContext).inflate(getLayoutId(viewType), parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    /**
     * 请使用bindHolder
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindHolder(holder, mData!![position], position)
        //点击事件
        holder.itemView.setOnClickListener {
//            Log.d("123====", "1点击了：position=$position")
//            Log.d("123====", "2点击了：position=${holder.layoutPosition}")
//            Log.d("123====", "3点击了：position=${holder.adapterPosition}")
            mOnClickListener?.onClick(position, mData[position])
        }

        //长按点击事件
        holder.itemView.setOnLongClickListener {
            mOnLongClickListener?.onLongClick(position, mData[position]) ?: false
        }
    }

    /**
     * 新的绑定view方法
     */
    abstract fun bindHolder(holder: BaseViewHolder, data: DATA, position: Int)
}