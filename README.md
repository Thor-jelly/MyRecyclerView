# 自定义通用RecyclerView

# 通用adapter->RVAdapter

**必须重写的方法**

- 获取layoutId--->`getLayoutId(viewType: Int): Int`
- 新的绑定view方法--->`bindHolder(holder: BaseViewHolder, data: DATA, position: Int)`

**非必需方法**

- item点击事件--->`setOnClickListener(onClickListener: OnClickListener<DATA>)方法`
- item长按事件--->`setOnLongClickListener(onLongClickListener: OnLongClickListener<DATA>)方法`

# 基础ViewHolder->BaseViewHolder

- 获取View--->`fun <T : View> getView(viewId: Int): T`
- 设置TextView.text--->`fun setText(viewId: Int, str: String)`
- 设置imageView.setImageResource--->`setImageResource(viewId: Int, resourceId: Int)`
- 设置setPathImage--->`fun setPathImage(viewId: Int, imageLoader: BaseImageLoader)`

# 自定义带有头部和底部RV->HeaderAndFooterRV

**仿照ListView主要逻辑**

- 改变`GridLayoutManager` 一行显示几个方法--->重写`changeSpanSize(position: Int): Int`
- 添加头部方法--->`fun addHeaderView(view: View)`
- 移除头部方法--->`fun removeHeaderView(view: View)`
- 添加底部方法--->`fun addFooterView(view: View)`
- 移除底部方法--->`fun removeFooterView(view: View)`
- 添加空布局方法--->`fun addEmptyView(view: View)`

# 自定义上拉刷新和下拉加载的RV->RefreshRV

## 内部刷新状态标志

```
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
```

## 监听刷新方法

```
fun addRefreshListener(refreshListener: RefreshListener, isHashRefresh: Boolean = true, isHashLoad: Boolean = true)
```

**可重写的监听内部方法，来改变刷新时view的显示样式**

- 下拉刷新样式更改及网络请求位置

    ```
        /**
         * 获取下拉加载更多的开始View
         */
        fun getRefreshViewStart(context: Context): View? {
            return 开始View
        }
    
        /**
         * 获取下拉加载更多的正在加载View
         */
        fun getRefreshViewing(context: Context): View? {
            return 正在加载View
        }
    
        /**
         * 获取下拉加载更多的加载成功View
         */
        fun getRefreshViewSuccess(context: Context): View? {
            return 成功View
        }
    
        /**
         * 获取下拉加载更多的加载失败View
         */
        fun getRefreshViewFailure(context: Context): View? {
            return 失败View
        }
    
        /**
         * 正在刷新
         */
        fun onRefreshing() {
            //网络加载刷新数据
        }
    ```
    
- 上拉刷新样式更改及网络请求位置

    ```
        /**
         * 获取上拉加载更多的开始View
         */
        fun getLoadViewStart(context: Context): View? {
            return 开始View
        }
    
        /**
         * 获取上拉加载更多的正在加载View
         */
        fun getLoadViewing(context: Context): View? {
            return 正在加载View
        }
    
        /**
         * 获取上拉加载更多的加载成功View
         */
        fun getLoadViewSuccess(context: Context): View? {
            return 加载成功View
        }
    
        /**
         * 获取上拉加载更多的加载失败View
         */
        fun getLoadViewFailure(context: Context): View? {
            return 加载失败View
        }
    
        /**
         * 获取上拉加载更多的没有更多数据的View
         */
        fun getLoadViewEnd(context: Context): View? {
            return 没有更多数据的View
        }
    
        /**
         * 正在加载中
         */
        fun onLoading() {
            //网络数据请求
        }
    ```

- 共有方法手指拖拽距离，用来改变view样式（对需要根据拖拽距离改变样式的view需要）

    ```
         /**
         * 拖拽距离
         *
         * @param dragHeight 拖拽距离
         * @param currentStatus 当前状态[-1,8]
         */
        fun onDragDistance(dragHeight: Int, @RefreshConst currentStatus: Int) {}
    ```

## 下拉刷新方法介绍

- 下拉刷新成功--->`fun refreshHeaderSuccess()`
- 下拉刷新失败--->`fun refreshHeaderFailure()`

## 上拉刷新方法介绍

- 上拉刷新成功--->`fun refreshFooterSuccess()`
- 上拉刷新失败--->`fun refreshFooterFailure()`
- 上拉没有过多数据--->`fun refreshFooterEnd()`