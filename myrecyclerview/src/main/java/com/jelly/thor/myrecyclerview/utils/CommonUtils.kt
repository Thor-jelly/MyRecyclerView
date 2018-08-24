package com.jelly.thor.myrecyclerview.utils

import android.view.View
import com.jelly.thor.myrecyclerview.annotation.Visibility

/**
 * 类描述：通用工具类 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/22 10:23 <br/>
 */
object CommonUtils {
    fun setShowModel(view: View?, @Visibility showStyle: Int) {
        if (view == null) {
            return
        }
        view.takeIf {
            it.visibility != showStyle
        }?.apply {
            this.visibility = showStyle
        }
    }
}