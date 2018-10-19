package com.jelly.thor.example

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jelly.thor.example.adapter.Adapter
import com.jelly.thor.myrecyclerview.base.clicklistener.OnClickListener
import com.jelly.thor.myrecyclerview.base.clicklistener.OnLongClickListener
import com.jelly.thor.myrecyclerview.base.refreshinterface.RefreshListener
import com.jelly.thor.myrecyclerview.itemdecoration.UniversalItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener {
    val mData = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)

    val ll by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(this)
    }
    val gl by lazy {
        androidx.recyclerview.widget.GridLayoutManager(this, 2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "MyRecyclerView"
        //设置toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setHomeButtonEnabled(true)  //设置返回键可用
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //创建返回键，并实现打开关/闭监听

        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        val drawerToggle = ActionBarDrawerToggle(this, dl, toolbar, R.string.nav_open, R.string.nav_close)
        dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()//初始化状态


        nv.setNavigationItemSelectedListener(this)

        initRv()
    }

    private fun initRv() {
        ll.orientation = LinearLayout.HORIZONTAL
        mRv.layoutManager = ll
        val myAdapter = Adapter(this, mData)

        mRv.adapter = myAdapter
        mRv.addItemDecoration(UniversalItemDecoration(ColorDrawable(Color.RED), 2))
        myAdapter.setOnClickListener(object : OnClickListener<Int> {
            override fun onClick(position: Int, data: Int) {
                Log.d("123===", "点击了：position=$position，data=$data")
                mData.remove(data)
                myAdapter.notifyItemRemoved(position)
            }
        })

        myAdapter.setOnLongClickListener(object : OnLongClickListener<Int> {
            override fun onLongClick(position: Int, data: Int): Boolean {
                Log.d("123===", "长按点击了：position=$position，data=$data")
                return true
            }
        })


        mRv.addRefreshListener(object : RefreshListener {
            override fun getRefreshViewStart(context: Context): View? {
                val textView = TextView(context)
                textView.height = 200
                textView.width = 200
                textView.text = "这是下拉开始"
                return textView
            }

            override fun getRefreshViewing(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是下拉ing"
                return textView
            }

            override fun getRefreshViewSuccess(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是下拉成功"
                return textView
            }

            override fun getRefreshViewFailure(context: Context): View? {
                val textView = TextView(context)
                textView.text = "这是下拉失败"
                return textView
            }
        })


        //initHeaderAndFooter()
    }

    fun initHeaderAndFooter() {
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_launcher)
        mRv.addHeaderView(imageView)

        val imageView1 = ImageView(this)
        imageView1.setImageResource(R.mipmap.ic_launcher_round)
        mRv.addHeaderView(imageView1)

        val imageView2 = ImageView(this)
        imageView2.setImageResource(R.mipmap.ic_launcher)
        mRv.addFooterView(imageView2)

        val imageView3 = ImageView(this)
        imageView3.setImageResource(R.mipmap.ic_launcher_round)
        mRv.addFooterView(imageView3)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        Log.d("123===", "onNavigationItemSelected: $id")
        when (id) {
            R.id.nav_camera -> Log.d("123===", "onNavigationItemSelected: nav_camera")
            R.id.nav_gallery -> Log.d("123===", "onNavigationItemSelected: nav_gallery")
            R.id.nav_slideshow -> Log.d("123===", "onNavigationItemSelected: nav_slideshow")
            R.id.nav_manage -> Log.d("123===", "onNavigationItemSelected: nav_manage")
            R.id.nav_camera2 -> Log.d("123===", "onNavigationItemSelected: nav_camera2")
            R.id.nav_gallery2 -> Log.d("123===", "onNavigationItemSelected: nav_gallery2")
            R.id.nav_slideshow2 -> Log.d("123===", "onNavigationItemSelected: nav_slideshow2")
            R.id.nav_manage2 -> Log.d("123===", "onNavigationItemSelected: nav_manage2")
            R.id.nav_share -> Log.d("123===", "onNavigationItemSelected: nav_share")
            R.id.nav_send -> Log.d("123===", "onNavigationItemSelected: nav_send")
        }
        //dl.closeDrawers();
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        Log.d("123===", "onOptionsItemSelected: $itemId")
        when (itemId) {
            R.id.grid_view -> {
                Log.d("123===", "onOptionsItemSelected: " + "grid_view")
                gl.orientation = LinearLayout.VERTICAL
                mRv.layoutManager = gl
                return true
            }
            R.id.list_view -> {
                Log.d("123===", "onOptionsItemSelected: " + "list_view")
                ll.orientation = LinearLayout.VERTICAL
                mRv.layoutManager = ll
                return true
            }
            R.id.out_view -> {
                Log.d("123===", "onOptionsItemSelected: " + "out_view")
                gl.orientation = LinearLayout.HORIZONTAL
                mRv.layoutManager = gl
                return true
            }
            R.id.home -> {
                Log.d("123===", "onOptionsItemSelected: " + "home")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
