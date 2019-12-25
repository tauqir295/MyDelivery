package com.my.delivery.deliverylist.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val listener: OnLoadMoreListener?
) : RecyclerView.OnScrollListener() {
    private var loading: Boolean = false // load more progress dialog

    //default constructor
    companion object {
        private val VISIBLE_THRESHOLD = 2
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dx == 0 && dy == 0)
            return
        val totalItemCount = linearLayoutManager.itemCount
        val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

        // checking the condition if user has scrolled to last item in recycler-view or not
        if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD && totalItemCount != 0) {
            listener?.onLoadMore()
            loading = true
        }
    }

    /**
     * setting the variable to enable [OnLoadMoreListener.onLoadMore] or not
     */
    fun setLoaded() {
        loading = false
    }

    /**
     * custom interface to pass event to load more data or not
     */
    interface OnLoadMoreListener {
        fun onLoadMore()
    }

}