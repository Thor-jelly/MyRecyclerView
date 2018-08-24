package com.jelly.thor.myrecyclerview.base

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.jelly.thor.myrecyclerview.annotation.RefreshConst
import com.jelly.thor.myrecyclerview.base.adapter.HeaderAndFooterAdapter
import com.jelly.thor.myrecyclerview.base.refreshinterface.RefreshListener
import com.jelly.thor.myrecyclerview.utils.CommonUtils

/**
 * 类描述：上拉刷新和下拉加载的RV<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/20 16:20 <br/>
 */
class RefreshRV : HeaderAndFooterRV {
    companion object {
        /**
         * 刷新默认状态
         */
        const val REFRESH_DEFAULT = -1
        /**
         * 刚开始下拉
         */
        const val DROP_DOWN_REFRESH_START = 0
        /**
         * 下拉正在刷新
         */
        const val DROP_DOWN_REFRESH_ING = 1
        /**
         * 下拉刷新成功
         */
        const val DROP_DOWN_REFRESH_SUCCESS = 2
        /**
         * 下拉刷新失败
         */
        const val DROP_DOWN_REFRESH_FAILURE = 3

        /**
         * 刚开始上拉
         */
        const val PULL_UP_REFRESH_START = 4
        /**
         * 上拉正在刷新
         */
        const val PULL_UP_REFRESH_ING = 5
        /**
         * 上拉刷新成功
         */
        const val PULL_UP_REFRESH_SUCCESS = 6
        /**
         * 上拉刷新失败
         */
        const val PULL_UP_REFRESH_FAILURE = 7
        /**
         * 上拉刷新没有更多数据
         */
        const val PULL_UP_REFRESH_END = 8
    }

    private lateinit var mRefreshListener: RefreshListener
    /**
     * 当前下拉刷新状态
     */
    @RefreshConst
    private var mCurrentHeaderStatus = REFRESH_DEFAULT

    /**
     * 当前上拉刷新状态
     */
    @RefreshConst
    private var mCurrentFooterStatus = REFRESH_DEFAULT

    /**
     * 下拉刷新头部View
     */
    private val mHeaderView by lazy {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout
    }

    /**
     * 下拉刷新头部View
     */
    private val mFooterView by lazy {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout
    }

    ////////////////////头部刷新的view/////////////////////
    private var mRefreshViewStart: View? = null
    private var mRefreshViewing: View? = null
    private var mRefreshViewSuccess: View? = null
    private var mRefreshViewFailure: View? = null

    ////////////////////底部刷新的view/////////////////////
    private var mLoadViewStart: View? = null
    private var mLoadViewing: View? = null
    private var mLoadViewSuccess: View? = null
    private var mLoadViewFailure: View? = null
    private var mLoadViewEnd: View? = null

    /**
     * 手指按下Y位置
     */
    private var mFingerDownY: Int = 0

    /**
     * 手指拖拽阻力指数
     */
    private var mDragIndex = 0.35F

    /**
     * 是否正在拖动
     */
    private var mCurrentDrag: Boolean = false

    /**
     * 是否下拉刷新
     */
    private var mIsHasRefresh: Boolean = true

    /**
     * 是否有上拉刷新
     */
    private var mIsHasLoad: Boolean = true


    constructor(@NonNull context: Context) : super(context)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    /**
     * 添加头部刷新监听
     */
    fun addRefreshListener(refreshListener: RefreshListener, isHashRefresh: Boolean = true, isHashLoad: Boolean = true) {
        mRefreshListener = refreshListener
        mIsHasRefresh = isHashRefresh
        mIsHasLoad = isHashLoad

        if (mIsHasRefresh) {
            addRefreshView()
        }

        if (mIsHasLoad) {
            addLoadView()
        }
    }

    /**
     * 下拉刷新成功
     */
    fun refreshHeaderSuccess() {
        mCurrentHeaderStatus = DROP_DOWN_REFRESH_SUCCESS
        setRefreshHeaderShowStyle(mCurrentHeaderStatus)

        val topMargin = (mHeaderView.layoutParams as MarginLayoutParams).topMargin
        val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), topMargin, -mHeaderView.height + 1)
        valueAnimator.duration = 300
        valueAnimator.startDelay = 1000
        valueAnimator.start()
        valueAnimator.addUpdateListener {
            //设置刷新View的marginTop
            val currentTopMargin = it.animatedValue as Int
            setRefreshViewMarginTop(currentTopMargin, mCurrentHeaderStatus)

            if (currentTopMargin == -mHeaderView.height + 1
                    || mHeaderView.height == 0) {
                mCurrentHeaderStatus = REFRESH_DEFAULT
                setRefreshHeaderShowStyle(mCurrentHeaderStatus)
                setRefreshViewMarginTop(1, mCurrentHeaderStatus)
            }

            mHeaderView.requestLayout()
        }
    }

    /**
     * 上拉刷新成功
     */
    fun refreshFooterSuccess() {
        mCurrentFooterStatus = PULL_UP_REFRESH_SUCCESS
        setRefreshFooterShowStyle(mCurrentFooterStatus)

        val bottomMargin = (mFooterView.layoutParams as MarginLayoutParams).bottomMargin
        val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), bottomMargin, -mFooterView.height + 1)
        valueAnimator.duration = 300
        valueAnimator.startDelay = 1000
        valueAnimator.start()
        valueAnimator.addUpdateListener {
            //设置刷新View的marginTop
            val currentBottomMargin = it.animatedValue as Int
            setRefreshViewMarginBottom(currentBottomMargin, mCurrentFooterStatus)

            if (currentBottomMargin == -mFooterView.height + 1
                    || mFooterView.height == 0) {
                mCurrentFooterStatus = REFRESH_DEFAULT
                setRefreshFooterShowStyle(mCurrentFooterStatus)
                setRefreshViewMarginBottom(1, mCurrentFooterStatus)
            }

            mFooterView.requestLayout()
        }
    }

    /**
     * 下拉刷新失败
     */
    fun refreshHeaderFailure() {
        mCurrentHeaderStatus = DROP_DOWN_REFRESH_FAILURE
        setRefreshHeaderShowStyle(mCurrentHeaderStatus)

        val topMargin = (mHeaderView.layoutParams as MarginLayoutParams).topMargin
        val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), topMargin, -mHeaderView.height + 1)
        valueAnimator.duration = 300
        valueAnimator.startDelay = 1000
        valueAnimator.start()
        valueAnimator.addUpdateListener {
            //设置刷新View的marginTop
            val currentTopMargin = it.animatedValue as Int
            setRefreshViewMarginTop(currentTopMargin, mCurrentHeaderStatus)


            if (currentTopMargin == -mHeaderView.height + 1
                    || mHeaderView.height == 0) {
                mCurrentHeaderStatus = REFRESH_DEFAULT
                setRefreshHeaderShowStyle(mCurrentHeaderStatus)
                setRefreshViewMarginTop(1, mCurrentHeaderStatus)
            }

            mHeaderView.requestLayout()
        }
    }

    /**
     * 下拉刷新失败
     */
    fun refreshFooterFailure() {
        mCurrentFooterStatus = PULL_UP_REFRESH_FAILURE
        setRefreshFooterShowStyle(mCurrentFooterStatus)

        val bottomMargin = (mFooterView.layoutParams as MarginLayoutParams).bottomMargin
        val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), bottomMargin, -mFooterView.height + 1)
        valueAnimator.duration = 300
        valueAnimator.startDelay = 1000
        valueAnimator.start()
        valueAnimator.addUpdateListener {
            //设置刷新View的marginTop
            val currentTopMargin = it.animatedValue as Int
            setRefreshViewMarginBottom(currentTopMargin, mCurrentFooterStatus)


            if (currentTopMargin == -mFooterView.height + 1
                    || mFooterView.height == 0) {
                mCurrentFooterStatus = REFRESH_DEFAULT
                setRefreshFooterShowStyle(mCurrentFooterStatus)
                setRefreshViewMarginBottom(1, mCurrentFooterStatus)
            }

            mFooterView.requestLayout()
        }
    }

    /**
     * 没有过多数据
     */
    fun refreshFooterEnd(){
        mCurrentFooterStatus = PULL_UP_REFRESH_END
        setRefreshFooterShowStyle(mCurrentFooterStatus)
        setRefreshViewMarginBottom(1, mCurrentFooterStatus)
        mFooterView.requestLayout()
    }

    /**
     * 添加下拉刷新view
     */
    private fun addRefreshView() {
        if (adapter == null) {
            Toast.makeText(context, "请先设置setAdapter！", Toast.LENGTH_LONG).show()
            return
        }
        if (!::mRefreshListener.isInitialized) {
            Toast.makeText(context, "请先设置addRefreshListener！", Toast.LENGTH_LONG).show()
            return
        }

        //添加下拉刷新各种样式
        addRefreshHeaderStyle()

        //多留出1px防止无法判断是不是滚动到头部问题
        setRefreshViewMarginTop(-mHeaderView.height + 1, REFRESH_DEFAULT)

        if (adapter is HeaderAndFooterAdapter) {
            (adapter as HeaderAndFooterAdapter).addRefreshHeader(mHeaderView)
        }
    }

    /**
     * 添加上拉刷新
     */
    private fun addLoadView() {
        if (adapter == null) {
            Toast.makeText(context, "请先设置setAdapter！", Toast.LENGTH_LONG).show()
            return
        }
        if (!::mRefreshListener.isInitialized) {
            Toast.makeText(context, "请先设置addRefreshListener！", Toast.LENGTH_LONG).show()
            return
        }

        //添加下拉刷新各种样式
        addRefreshFooterStyle()

        //多留出1px防止无法判断是不是滚动到底部问题
        setRefreshViewMarginBottom(1, REFRESH_DEFAULT)

        if (adapter is HeaderAndFooterAdapter) {
            (adapter as HeaderAndFooterAdapter).addRefreshFooterView(mFooterView)
        }
    }

    /**
     * 添加下拉刷新各种样式
     */
    private fun addRefreshHeaderStyle() {
        mRefreshViewStart = mRefreshListener.getRefreshViewStart(context)
        mRefreshViewing = mRefreshListener.getRefreshViewing(context)
        mRefreshViewSuccess = mRefreshListener.getRefreshViewSuccess(context)
        mRefreshViewFailure = mRefreshListener.getRefreshViewFailure(context)
        mRefreshViewStart?.let {
            mHeaderView.addView(mRefreshViewStart)
            it.visibility = View.GONE
        }

        mRefreshViewing?.let {
            mHeaderView.addView(mRefreshViewing)
            it.visibility = View.GONE
        }

        mRefreshViewSuccess?.let {
            mHeaderView.addView(mRefreshViewSuccess)
            it.visibility = View.GONE
        }

        mRefreshViewFailure?.let {
            mHeaderView.addView(mRefreshViewFailure)
            it.visibility = View.GONE
        }
    }

    /**
     * 添加上拉刷新各种样式
     */
    private fun addRefreshFooterStyle() {
        mLoadViewStart = mRefreshListener.getLoadViewStart(context)
        mLoadViewing = mRefreshListener.getLoadViewing(context)
        mLoadViewSuccess = mRefreshListener.getLoadViewSuccess(context)
        mLoadViewFailure = mRefreshListener.getLoadViewFailure(context)
        mLoadViewEnd = mRefreshListener.getLoadViewEnd(context)
        mLoadViewStart?.let {
            mFooterView.addView(mLoadViewStart)
            it.visibility = View.GONE
        }

        mLoadViewing?.let {
            mFooterView.addView(mLoadViewing)
            it.visibility = View.GONE
        }

        mLoadViewSuccess?.let {
            mFooterView.addView(mLoadViewSuccess)
            it.visibility = View.GONE
        }

        mLoadViewFailure?.let {
            mFooterView.addView(mLoadViewFailure)
            it.visibility = View.GONE
        }

        mLoadViewEnd?.let {
            mFooterView.addView(mLoadViewEnd)
            it.visibility = View.GONE
        }
    }

    /**
     * 设置下拉刷新头样式,四种样式（下拉样式，刷新样式，刷新成功样式，刷新失败样式）
     * @param showStyle REFRESH_DEFAULT, DROP_DOWN_REFRESH_START, DROP_DOWN_REFRESH_ING, DROP_DOWN_REFRESH_SUCCESS, DROP_DOWN_REFRESH_FAILURE
     */
    private fun setRefreshHeaderShowStyle(showStyle: Int) {
        mRefreshViewStart?.takeIf {
            showStyle == DROP_DOWN_REFRESH_START
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mRefreshViewStart, View.GONE)

        mRefreshViewing?.takeIf {
            showStyle == DROP_DOWN_REFRESH_ING
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mRefreshViewing, View.GONE)

        mRefreshViewSuccess?.takeIf {
            showStyle == DROP_DOWN_REFRESH_SUCCESS
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mRefreshViewSuccess, View.GONE)

        mRefreshViewFailure?.takeIf {
            showStyle == DROP_DOWN_REFRESH_FAILURE
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mRefreshViewFailure, View.GONE)
    }

    /**
     * 设置上拉刷新头样式,四种样式（下拉样式，刷新样式，刷新成功样式，刷新失败样式）
     * @param showStyle REFRESH_DEFAULT, PULL_UP_REFRESH_START, PULL_UP_REFRESH_ING, PULL_UP_REFRESH_SUCCESS, PULL_UP_REFRESH_FAILURE
     */
    private fun setRefreshFooterShowStyle(showStyle: Int) {
        mLoadViewStart?.takeIf {
            showStyle == PULL_UP_REFRESH_START
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mLoadViewStart, View.GONE)

        mLoadViewing?.takeIf {
            showStyle == PULL_UP_REFRESH_ING
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mLoadViewing, View.GONE)

        mLoadViewSuccess?.takeIf {
            showStyle == PULL_UP_REFRESH_SUCCESS
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mLoadViewSuccess, View.GONE)

        mLoadViewFailure?.takeIf {
            showStyle == PULL_UP_REFRESH_FAILURE
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mLoadViewFailure, View.GONE)

        mLoadViewEnd?.takeIf {
            showStyle == PULL_UP_REFRESH_END
        }?.apply {
            CommonUtils.setShowModel(this, View.VISIBLE)
        } ?: CommonUtils.setShowModel(mLoadViewEnd, View.GONE)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (BuildConfig.DEBUG) {
//            Log.d("123===", "dispatchTouchEvent=${ev.action}")
//        }
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = ev.y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                if (mCurrentDrag) {
                    //重置当前刷新状态状态
                    restoreRefreshView()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
//        if (BuildConfig.DEBUG) {
//            Log.d("123===", "onTouchEvent=${e.action}")
//        }
        val action = e.action
        when (action) {
            MotionEvent.ACTION_MOVE -> {
                if (!canScrollDropDown()
                        && (mCurrentHeaderStatus == DROP_DOWN_REFRESH_START
                                || mCurrentHeaderStatus == REFRESH_DEFAULT)
                        && (mCurrentFooterStatus == REFRESH_DEFAULT
                                || mCurrentFooterStatus == PULL_UP_REFRESH_END)) {
                    //如果是在最顶部才处理，否则不需要处理

                    //解决下拉刷新自动滚动问题
                    if (mCurrentDrag) {
                        scrollToPosition(0)
                    }

                    //获取手指触摸拖拽距离
                    val distanceY = (e.y - mFingerDownY) * mDragIndex

                    if (distanceY > 0) {
                        mCurrentHeaderStatus = DROP_DOWN_REFRESH_START
                        setRefreshHeaderShowStyle(mCurrentHeaderStatus)

                        val marginTop = (distanceY - mHeaderView.measuredHeight).toInt()
                        setRefreshViewMarginTop(marginTop, mCurrentHeaderStatus)
                        mCurrentDrag = true
                        return false
                    } else {
                        mCurrentHeaderStatus = REFRESH_DEFAULT
                        setRefreshHeaderShowStyle(mCurrentHeaderStatus)
                        setRefreshViewMarginTop(1, mCurrentHeaderStatus)
                    }
                } else if (!canScrollPullUp()
                        && (mCurrentFooterStatus == REFRESH_DEFAULT
                                || mCurrentFooterStatus == PULL_UP_REFRESH_START)
                        && (mCurrentHeaderStatus == REFRESH_DEFAULT)) {
                    //如果是在最底部才处理，否则不需要处理

                    //解决下拉刷新自动滚动问题
                    if (mCurrentDrag) {
                        val itemCount = adapter?.itemCount ?: -1
                        if (itemCount >= 0) {
                            val count = if (itemCount == 0) 0 else itemCount - 1
                            scrollToPosition(count)
                        }
                    }

                    //获取手指触摸拖拽距离
                    val distanceY = (e.y - mFingerDownY) * mDragIndex

                    if (distanceY < 0) {
                        mCurrentFooterStatus = PULL_UP_REFRESH_START
                        setRefreshFooterShowStyle(mCurrentFooterStatus)

                        val marginBottom = (-distanceY - mFooterView.height).toInt()
                        setRefreshViewMarginBottom(marginBottom, mCurrentFooterStatus)
                        mCurrentDrag = true
                        return false
                    } else {
                        mCurrentFooterStatus = REFRESH_DEFAULT
                        setRefreshFooterShowStyle(mCurrentFooterStatus)
                        setRefreshViewMarginBottom(1, mCurrentFooterStatus)
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }

//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        if (BuildConfig.DEBUG) {
//            Log.d("123===", "onLayout=$changed=$l=$t=$r=$b")
//        }
//        super.onLayout(changed, l, t, r, b)
////        if (changed) {
////            if (mRefreshViewHeight <= 0) {
////                //获取头部view的高度
////                mRefreshViewHeight = mHeaderView.measuredHeight
////                if (mRefreshViewHeight > 0) {
////                    // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
////                    setRefreshViewMarginTop(-mRefreshViewHeight /*+ 1*/)
////                }
////            }
////        }
//    }

    /**
     * 是否可以移动下拉
     */
    private fun canScrollDropDown(): Boolean {
//        if (BuildConfig.DEBUG) {
//            Log.d("123===", "是否可以下拉${canScrollVertically(-1)}")
//        }
        return canScrollVertically(-1)
    }

    /**
     * 是否可以移动下拉
     */
    private fun canScrollPullUp(): Boolean {
//        if (BuildConfig.DEBUG) {
//            Log.d("123===", "是否可以上拉${canScrollVertically(1)}")
//        }
        return canScrollVertically(1)
    }

    /**
     * 重置当前刷新状态状态
     */
    private fun restoreRefreshView() {
        if (mCurrentHeaderStatus == DROP_DOWN_REFRESH_START) {
            val topMargin = (mHeaderView.layoutParams as MarginLayoutParams).topMargin
            if (topMargin >= 0) {
                val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), topMargin, 1)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener {
                    //设置刷新View的marginTop
                    val currentTopMargin = it.animatedValue as Int
                    setRefreshViewMarginTop(currentTopMargin, mCurrentHeaderStatus)
                    mHeaderView.requestLayout()
                }
                valueAnimator.start()
                mCurrentHeaderStatus = DROP_DOWN_REFRESH_ING
                setRefreshHeaderShowStyle(mCurrentHeaderStatus)
                mRefreshListener.onRefreshing()
            } else {
                val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), topMargin, -mHeaderView.height + 1)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener {
                    //设置刷新View的marginTop
                    val currentTopMargin = it.animatedValue as Int
                    setRefreshViewMarginTop(currentTopMargin, mCurrentHeaderStatus)
                    mHeaderView.requestLayout()
                }
                valueAnimator.start()
                mCurrentHeaderStatus = REFRESH_DEFAULT
                setRefreshHeaderShowStyle(mCurrentHeaderStatus)
            }
        } else if (mCurrentFooterStatus == PULL_UP_REFRESH_START) {
            val bottomMargin = (mFooterView.layoutParams as MarginLayoutParams).bottomMargin
            if (bottomMargin >= 0) {
                val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), bottomMargin, 1)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener {
                    //设置刷新View的marginBottom
                    val currentBottomMargin = it.animatedValue as Int
                    setRefreshViewMarginBottom(currentBottomMargin, mCurrentFooterStatus)
                    mFooterView.requestLayout()
                }
                valueAnimator.start()
                mCurrentFooterStatus = PULL_UP_REFRESH_ING
                setRefreshFooterShowStyle(mCurrentFooterStatus)
                mRefreshListener.onLoading()
            } else {
                val valueAnimator = ValueAnimator.ofObject(IntEvaluator(), bottomMargin, -mFooterView.height + 1)
                valueAnimator.duration = 300
                valueAnimator.addUpdateListener {
                    //设置刷新View的marginBottom
                    val currentBottomMargin = it.animatedValue as Int
                    setRefreshViewMarginBottom(currentBottomMargin, mCurrentFooterStatus)
                    mFooterView.requestLayout()
                }
                valueAnimator.start()
                mCurrentFooterStatus = REFRESH_DEFAULT
                setRefreshFooterShowStyle(mCurrentFooterStatus)
            }
        }
        mCurrentDrag = false
    }

    /**
     * 下拉刷新设置刷新View的marginTop
     */
    private fun setRefreshViewMarginTop(currentTopMargin: Int, currentStatus: Int) {
        var topMargin = currentTopMargin
        val marginLayoutParams = mHeaderView.layoutParams as ViewGroup.MarginLayoutParams
        val finalTopMargin = -mHeaderView.height + 1
        if (topMargin < finalTopMargin) {
            topMargin = finalTopMargin
        }
        marginLayoutParams.topMargin = topMargin
        mRefreshListener.onDragDistance(topMargin, currentStatus)
    }

    /**
     * 上拉设置刷新View的marginTop
     */
    private fun setRefreshViewMarginBottom(currentBottomMargin: Int, currentStatus: Int) {
        var bottomMargin = currentBottomMargin
        val marginLayoutParams = mFooterView.layoutParams as ViewGroup.MarginLayoutParams
        val finalBottomMargin = -mFooterView.height + 1
        if (bottomMargin < finalBottomMargin) {
            bottomMargin = finalBottomMargin
        }
        marginLayoutParams.bottomMargin = bottomMargin
        mRefreshListener.onDragDistance(bottomMargin, currentStatus)
    }
}

