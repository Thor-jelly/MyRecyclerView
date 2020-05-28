package com.jelly.thor.myrecyclerview.itemdecoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 类描述：RecyclerView万能分割线,现在只有2个LayoutManager(线性和表格)<br/>
 * 当前项目的不更新了，如果使用请查看CommontUtils中的最新版万能分割线[https://github.com/Thor-jelly/CommontUtils]
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/7/24 16:48 <br/>
 */
private const val MODE_UN_KNOW = -1
private const val MODE_LINEAR_HORIZONTAL = 0
private const val MODE_LINEAR_VERTICAL = 1
private const val MODE_GRID_HORIZONTAL = 2
private const val MODE_GRID_VERTICAL = 3
class UniversalItemDecoration(private val divider: Drawable = ColorDrawable(Color.WHITE),
                              private val width: Int = 1) : RecyclerView.ItemDecoration() {
    /**
     * rv manager模式
     */
    private var mMode = MODE_UN_KNOW

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //首先获取manager方式
        initManagerMode(parent)
        //获取当前view的position
        val nowViewPosition = parent.getChildAdapterPosition(view)
        //根据模式设置divider
        when (mMode) {
            MODE_LINEAR_HORIZONTAL -> {
                //如果是线性水平布局
                if (nowViewPosition != parent.adapter!!.itemCount - 1) {
                    //如果是最后一行则不显示
                    outRect.right = width
                } else {
                    outRect.right = 0
                }
            }
            MODE_LINEAR_VERTICAL -> {
                //如果是线性垂直布局
                if (nowViewPosition != parent.adapter!!.itemCount - 1) {
                    //如果是最后一行则不显示
                    outRect.bottom = width
                } else {
                    outRect.bottom = 0
                }
            }
            MODE_GRID_HORIZONTAL -> {
                //表格布局水平方向
                val nowGridLM = parent.layoutManager as GridLayoutManager
                //列数
                val spanCount = nowGridLM.spanCount
                //总item个数
                val itemCount = parent.adapter!!.itemCount
                //总余数
                val totalRemainder = itemCount % spanCount
                //行数
                val rows = itemCount / spanCount + if (itemCount > spanCount && totalRemainder > 0) 1 else 0
                when (rows) {
                    0, 1 -> {
                        //如果只有一行数据，则除了最后一个item没有右距离，其他都添加
                        val remainder = nowViewPosition % spanCount
                        when (remainder) {
                            spanCount - 1 -> outRect.bottom = 0
                            else -> outRect.bottom = width
                        }
                    }
                    else -> {
                        //如果有多行数据，则判断当前位置是在这一行中的第几个item
                        val remainder = nowViewPosition % spanCount
                        when (remainder) {
                            spanCount - 1 -> {
                                //如果是最后一数列的则没有有边距
                                //如果总余数为0，表示item个数正好可以绘制满，如果不为0表示最后一行会少几个item
                                val i = if (totalRemainder == 0) spanCount else totalRemainder
                                //如果当前位置余数为0，表示是这一行中的第一个item，如果不为0，表示是这一行的后面的item
                                val j = if (remainder == 0) i else remainder
                                //如果item位置对应最后一行中对应item位置
                                val b = nowViewPosition == itemCount - j
                                outRect.right = if (b) 0 else width
                            }
                            else -> {
                                //其他都有下边距
                                outRect.bottom = width
                                val i = if (totalRemainder == 0) spanCount else totalRemainder
                                val j = if (remainder == 0) i else remainder
                                val b = nowViewPosition == itemCount - j
                                outRect.right = if (b) 0 else width
                            }
                        }
                    }
                }
            }
            MODE_GRID_VERTICAL -> {
                //表格布局垂直方向
                val nowGridLM = parent.layoutManager as GridLayoutManager
                //列数
                val spanCount = nowGridLM.spanCount
                //总item个数
                val itemCount = parent.adapter!!.itemCount
                //总余数
                val totalRemainder = itemCount % spanCount
                //行数
                val rows = itemCount / spanCount + if (itemCount > spanCount && totalRemainder > 0) 1 else 0
                when (rows) {
                    0, 1 -> {
                        //如果只有一行数据，则除了最后一个item没有右距离，其他都添加
                        val remainder = nowViewPosition % spanCount
                        when (remainder) {
                            spanCount - 1 -> outRect.right = 0
                            else -> outRect.right = width
                        }
                    }

                    else -> {
                        //如果有多行数据，则判断当前位置是在这一行中的第几个item
                        val remainder = nowViewPosition % spanCount
                        when (remainder) {
                            spanCount - 1 -> {
                                //如果是最后一数列的则没有有边距
                                //如果总余数为0，表示item个数正好可以绘制满，如果不为0表示最后一行会少几个item
                                val i = if (totalRemainder == 0) spanCount else totalRemainder
                                //如果当前位置余数为0，表示是这一行中的第一个item，如果不为0，表示是这一行的后面的item
                                val j = if (remainder == 0) i else remainder
                                //如果item位置对应最后一行中对应item位置
                                val b = nowViewPosition == itemCount - j
                                outRect.bottom = if (b) 0 else width
                            }
                            else -> {
                                //其他都有右边距
                                outRect.right = width
                                val i = if (totalRemainder == 0) spanCount else totalRemainder
                                val j = if (remainder == 0) i else remainder
                                val b = nowViewPosition == itemCount - j
                                outRect.bottom = if (b) 0 else width
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化manager模式
     */
    private fun initManagerMode(parent: RecyclerView) {
        val nowManager = parent.layoutManager
        mMode = when (nowManager) {
            is GridLayoutManager -> {
                val nowOrientation = nowManager.orientation
                when (nowOrientation) {
                    LinearLayout.HORIZONTAL -> MODE_GRID_HORIZONTAL
                    else -> MODE_GRID_VERTICAL
                }
            }
            is LinearLayoutManager -> {
                val nowOrientation = nowManager.orientation
                when (nowOrientation) {
                    LinearLayout.HORIZONTAL -> MODE_LINEAR_HORIZONTAL
                    else -> MODE_LINEAR_VERTICAL
                }
            }
            else -> MODE_UN_KNOW
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        //根据模式设置divider
        when (mMode) {
            MODE_LINEAR_HORIZONTAL -> {
                //如果是线性水平布局
                drawVertical(canvas, parent)
            }
            MODE_LINEAR_VERTICAL -> {
                //如果是线性垂直布局
                drawHorizontal(canvas, parent)
            }
            MODE_GRID_HORIZONTAL, MODE_GRID_VERTICAL -> {
                //表格布局
                drawHorizontal(canvas, parent)
                drawVertical(canvas, parent)
            }
        }
    }


    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            var top = childView.top
            var bottom = childView.bottom

            //顶部recyclerView padding
            val pair = topRecyclerViewPadding(top, parent, bottom)
            top = pair.first
            bottom = pair.second
            //底部recyclerView padding
            val pair1 = bottomRecyclerViewPadding(bottom, parent, top)
            bottom = pair1.first
            top = pair1.second

            var left = childView.right
            var right = left + width

            //左侧recyclerView padding
            val pair2 = leftRecyclerViewPadding(left, parent, right)
            left = pair2.first
            right = pair2.second
            //右侧recyclerView padding
            val pair3 = rightRecyclerViewPadding(right, parent, left)
            left = pair3.first
            right = pair3.second

            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i);
//            val params = child.layoutParams as RecyclerView.LayoutParams
            var top = child.bottom
            var bottom = top + width

            //顶部recyclerView padding
            val pair = topRecyclerViewPadding(top, parent, bottom)
            top = pair.first
            bottom = pair.second
            //底部recyclerView padding
            val pair1 = bottomRecyclerViewPadding(bottom, parent, top)
            bottom = pair1.first
            top = pair1.second

            var left = child.left
            var right = child.right + width

            //左侧recyclerView padding
            val pair2 = leftRecyclerViewPadding(left, parent, right)
            left = pair2.first
            right = pair2.second
            //右侧recyclerView padding
            val pair3 = rightRecyclerViewPadding(right, parent, left)
            left = pair3.first
            right = pair3.second

            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }

    private fun rightRecyclerViewPadding(right: Int, parent: RecyclerView, left: Int): Pair<Int, Int> {
        var right1 = right
        var left1 = left
        if (right1 > parent.width - parent.paddingRight) {
            right1 = parent.width - parent.paddingRight
        }
        if (left1 > parent.width - parent.paddingRight) {
            left1 = 0
            right1 = 0
        }
        return Pair(left1, right1)
    }

    private fun leftRecyclerViewPadding(left: Int, parent: RecyclerView, right: Int): Pair<Int, Int> {
        var left1 = left
        var right1 = right
        if (left1 < parent.paddingLeft) {
            left1 = parent.paddingLeft
        }
        if (right1 < parent.paddingLeft) {
            left1 = 0
            right1 = 0
        }
        return Pair(left1, right1)
    }

    private fun bottomRecyclerViewPadding(bottom: Int, parent: RecyclerView, top: Int): Pair<Int, Int> {
        var bottom1 = bottom
        var top1 = top
        if (bottom1 > parent.height - parent.paddingBottom) {
            bottom1 = parent.height - parent.paddingBottom
        }
        if (top1 > parent.height - parent.paddingBottom) {
            top1 = 0
            bottom1 = 0
        }
        return Pair(bottom1, top1)
    }

    private fun topRecyclerViewPadding(top: Int, parent: RecyclerView, bottom: Int): Pair<Int, Int> {
        var top1 = top
        var bottom1 = bottom
        if (top1 < parent.paddingTop) {
            top1 = parent.paddingTop
        }
        if (bottom1 < parent.paddingTop) {
            top1 = 0
            bottom1 = 0
        }
        return Pair(top1, bottom1)
    }
}
