package com.jelly.thor.myrecyclerview.base.clicklistener

/**
 * 类描述：item长按点击事件 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/2 13:53 <br/>
 */
interface OnLongClickListener<T> {
    fun onLongClick(position: Int, data: T): Boolean
}