package com.jelly.thor.myrecyclerview.base.viewholder

import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jelly.thor.myrecyclerview.annotation.Visibility

/**
 * 类描述：基础ViewHolder<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/1 14:56 <br/>
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mOldViewSa: SparseArray<View> = SparseArray()

    /**
     * 获取View
     */
    fun <T : View> getView(@IdRes viewId: Int): T {
        val oldView = mOldViewSa.get(viewId)
        if (oldView == null) {
            val findView = itemView.findViewById<T>(viewId)
            mOldViewSa.put(viewId, findView)
            return findView
        }
        return oldView as T
    }

    /**
     * 设置TextView.text
     */
    fun setText(@IdRes viewId: Int, str: String): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.text = str
        return this
    }

    /**
     * 设置文字颜色
     */
    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.setTextColor(textColor)
        return this
    }

    /**
     * 设置背景
     */
    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * 设置背景
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt backgroundColor: Int): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.setBackgroundColor(backgroundColor)
        return this
    }

    /**
     * 设置显隐
     */
    fun setVisibility(@IdRes viewId: Int, @Visibility visibility: Int): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.visibility = visibility
        return this
    }

    /**
     * 设置imageView.setImageResource
     */
    fun setImageResource(@IdRes viewId: Int, resourceId: Int): BaseViewHolder {
        val imageView = getView<ImageView>(viewId)
        imageView.setImageResource(resourceId)
        return this
    }

    fun setPathImage(@IdRes viewId: Int, imageLoader: BaseImageLoader): BaseViewHolder {
        val imageView = getView<ImageView>(viewId)
        imageLoader.loadImage(imageView)
        return this
    }

    abstract class BaseImageLoader(protected val pathStr: String) {
        abstract fun loadImage(imageView: ImageView)
    }
}