package com.jelly.thor.example

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.jelly.thor.example.adapter.Adapter
import com.jelly.thor.myrecyclerview.base.clicklistener.OnClickListener
import com.jelly.thor.myrecyclerview.base.clicklistener.OnLongClickListener
import com.jelly.thor.myrecyclerview.base.refreshinterface.RefreshListener
import com.jelly.thor.myrecyclerview.itemdecoration.UniversalItemDecoration
import kotlinx.android.synthetic.main.activity_rv.*
import kotlin.concurrent.thread

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
        ll.orientation = LinearLayout.VERTICAL
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
    }
}