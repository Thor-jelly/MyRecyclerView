package com.jelly.thor.myrecyclerview.annotation

import android.support.annotation.IntDef
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.DROP_DOWN_REFRESH_FAILURE
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.DROP_DOWN_REFRESH_ING
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.DROP_DOWN_REFRESH_START
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.DROP_DOWN_REFRESH_SUCCESS
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.PULL_UP_REFRESH_END
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.PULL_UP_REFRESH_FAILURE
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.PULL_UP_REFRESH_ING
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.PULL_UP_REFRESH_START
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.PULL_UP_REFRESH_SUCCESS
import com.jelly.thor.myrecyclerview.base.RefreshRV.Companion.REFRESH_DEFAULT


/**
 * 类描述：刷新注解<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/20 16:36 <br/>
 */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@IntDef(REFRESH_DEFAULT,
        DROP_DOWN_REFRESH_START,
        DROP_DOWN_REFRESH_ING,
        DROP_DOWN_REFRESH_SUCCESS,
        DROP_DOWN_REFRESH_FAILURE,
        PULL_UP_REFRESH_START,
        PULL_UP_REFRESH_ING,
        PULL_UP_REFRESH_SUCCESS,
        PULL_UP_REFRESH_FAILURE,
        PULL_UP_REFRESH_END)
annotation class RefreshConst