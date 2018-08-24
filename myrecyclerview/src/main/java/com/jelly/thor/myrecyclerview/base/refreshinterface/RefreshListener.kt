package com.jelly.thor.myrecyclerview.base.refreshinterface

import com.jelly.thor.myrecyclerview.annotation.RefreshConst
import org.jetbrains.annotations.Contract

/**
 * 类描述：刷新监听<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/20 16:27 <br/>
 */
interface RefreshListener : DropDownListener, PullUpListener {
    /**
     * 拖拽距离
     *
     * @param dragHeight 拖拽距离
     * @param currentStatus 当前状态[-1,8]
     */
    fun onDragDistance(dragHeight: Int, @RefreshConst currentStatus: Int) {}
}