package com.jelly.thor.example

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jelly.thor.example.adapter.Adapter
import com.jelly.thor.myrecyclerview.base.clicklistener.OnClickListener
import com.jelly.thor.myrecyclerview.base.clicklistener.OnLongClickListener
import com.jelly.thor.myrecyclerview.base.refreshinterface.RefreshListener
import com.jelly.thor.myrecyclerview.itemdecoration.UniversalItemDecoration
import kotlinx.android.synthetic.main.activity_rv.*

/**
 * 类描述：测试RV的activity<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/8/22 16:41 <br/>
 */
class RVActivity : AppCompatActivity() {
    val mData = mutableListOf(1, 2, 3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)

        initRv()
    }

    private fun initRv() {
        val ll = LinearLayoutManager(this)
        ll.orientation = RecyclerView.VERTICAL
        mRv.layoutManager = ll
        val myAdapter = Adapter(this, mData)

        mRv.adapter = myAdapter
        mRv.addItemDecoration(UniversalItemDecoration(ColorDrawable(Color.RED), 2))
        myAdapter.setOnClickListener(object : OnClickListener<Int> {
            override fun onClick(position: Int, data: Int) {
                Log.d("123===", "点击了：position=$position，data=$data")
                mData.remove(data)
                myAdapter.notifyItemRemoved(position)
//                myAdapter.notifyDataSetChanged()
            }
        })

        myAdapter.setOnLongClickListener(object : OnLongClickListener<Int> {
            override fun onLongClick(position: Int, data: Int): Boolean {
                Log.d("123===", "长按点击了：position=$position，data=$data")
                return true
            }
        })

        val emptyView = TextView(this)
        emptyView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        emptyView.gravity = Gravity.CENTER
        emptyView.text = "当前没有数据"
        emptyView.setOnClickListener {
            mData.add(1)
            myAdapter.notifyItemInserted(0)
        }
        mRv.addEmptyView(emptyView)

        mRv.addRefreshListener(object : RefreshListener {
            override fun getRefreshViewStart(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是下拉开始"
                return textView
            }

            override fun getRefreshViewing(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是下拉ing"
                return textView
            }

//            override fun getRefreshViewSuccess(context: Context): View? {
//                val textView = TextView(context)
//                textView.text = "这是下拉成功"
//                return textView
//            }

            override fun getRefreshViewFailure(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是下拉失败"
                return textView
            }

            override fun getLoadViewStart(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是上拉开始"
                return textView
            }

            override fun getLoadViewing(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是上拉ing"
                return textView
            }

            override fun getLoadViewSuccess(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是上拉成功"
                return textView
            }

            override fun getLoadViewFailure(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是上拉失败"
                return textView
            }

            override fun getLoadViewEnd(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是上拉没有更多数据"
                return textView
            }

            override fun onRefreshing() {
                Thread(object : Runnable {
                    override fun run() {
                        Thread.sleep(3000)
                        this@RVActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                mRv.refreshHeaderSuccess()
                            }
                        })
                    }
                }).start()
            }

            override fun onLoading() {
                Thread(object : Runnable {
                    override fun run() {
                        Thread.sleep(3000)
                        this@RVActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                mRv.refreshFooterEnd()
                            }
                        })
                    }
                }).start()
            }
        })


        //initHeaderAndFooter()


        //参考
        //[RecyclerView的拖动和滑动 第一部分 ：基本的ItemTouchHelper示例](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0630/3123.html)
        //[RecyclerView的拖动和滑动 第二部分 ：拖块，Grid以及自定义动画](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0724/3219.html)
        val a = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                /*
                    你需要重写getMovementFlags()方法来指定可以支持的拖放和滑动的方向。
                    使用helperItemTouchHelper.makeMovementFlags(int, int)来构造返回的flag。
                    这里我们启用了上下左右两种方向。注：上下为拖动（drag），左右为滑动（swipe）。
                 */
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

                return makeMovementFlags(dragFlags, swipeFlags)
            }

            //看自己需求重不重写这个方法
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (viewHolder == null) {
                    return
                }
                super.onSelectedChanged(viewHolder, actionState)
                //actionState状态是否改变，  正常状态ItemTouchHelper#ACTION_STATE_IDLE，
                //                         侧滑状态ItemTouchHelper#ACTION_STATE_SWIPE，
                //                         拖动状态ItemTouchHelper#ACTION_STATE_DRAG
                //viewHolder.itemView.setBackgroundColor(Color.YELLOW)
            }

            //同上面一个方法成对出现
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                //在一个view被拖拽然后被放开的时候被调用，同时也会在滑动被取消或者完成ACTION_STATE_IDLE)的时候被调用。
                //这里是恢复item view idle状态的典型地方。
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                /*
                    移动完成后执行这个方法
                 */
                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition
                //更新移动位置信息 调用 更改数据(遍历开始位置到结束位置，两两切换位置。千万不要只调用一次Collections.swap(data，startPosition，endPosition))
                //                   和 adapter.notifyItemMoved(startPosition, endPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                /*
                    侧滑删除完成执行这个方法
                 */
                //获得删除的位置信息
                val position = viewHolder.adapterPosition
                //更新数据 调用 更改数据 和 adapter.notifyItemRemoved()
            }

            //开始拖动两种方法：1。长按拖动->重写方法isLongPressDragEnabled()方法中返回true；2。调用ItemTouchHelper.startDrag(RecyclerView.ViewHolder) 方法来开始一个拖动
            //滑动也是两种：1。重写方法sItemViewSwipeEnabled()中返回true；2。ItemTouchHelper.startSwipe(RecyclerView.ViewHolder)
        })

        //必需通过attachToRecyclerView把itemTouchHelp 附加到 Rv上去
        a.attachToRecyclerView(mRv)
    }
}