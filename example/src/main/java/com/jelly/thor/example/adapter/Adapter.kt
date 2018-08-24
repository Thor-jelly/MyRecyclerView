package com.jelly.thor.example.adapter

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.jelly.thor.example.R
import com.jelly.thor.myrecyclerview.base.adapter.RVAdapter
import com.jelly.thor.myrecyclerview.base.viewholder.BaseViewHolder

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/7/24 11:28 <br/>
 */
class Adapter(mContext: Context, mData: MutableList<Int>) : RVAdapter<Int>(mContext, mData) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_my_adpater
    }

    override fun bindHolder(holder: BaseViewHolder, data: Int, position: Int) {
        val mNameTV = holder.getView<TextView>(R.id.mNameTv)
        mNameTV.setBackgroundColor(Color.GREEN)
        holder.setText(R.id.mNameTv, "我是谁$data")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}