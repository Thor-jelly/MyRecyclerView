package com.jelly.thor.myrecyclerview.base.clicklistener

/**
 * 类描述：item点击事件 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/2 13:53 <br/>
 */
interface OnClickListener<T> {
    fun onClick(position: Int, data: T)
}