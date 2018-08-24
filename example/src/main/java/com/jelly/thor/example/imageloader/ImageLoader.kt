package com.jelly.thor.example.imageloader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jelly.thor.myrecyclerview.base.viewholder.BaseViewHolder

/**
 * 类描述：图片加载 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/2 11:08 <br/>
 */
class ImageLoader(private val context: Context, pathStr: String) : BaseViewHolder.BaseImageLoader(pathStr) {
    override fun loadImage(imageView: ImageView) {
            Glide.with(context).load(pathStr).into(imageView)
    }
}