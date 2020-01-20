package com.aiqency.tmdb.activity.main.view.adapters

import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aiqency.tmdb.realms.GenericListener

/**
 * @see [stackoverflow](https://stackoverflow.com/a/42209600/2754562)
 */
internal class EndlessScrollListener(private val refreshList: GenericListener<Int>) : RecyclerView.OnScrollListener() {
    private var isLoading: Boolean = false
    private var hasMorePages: Boolean = true
    private var pageNumber = 1
    private var isRefreshing: Boolean = false
    private var pastVisibleItems: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView.layoutManager as StaggeredGridLayoutManager?
        manager?.apply {
            val visibleItemCount = childCount
            val totalItemCount = itemCount
            val firstVisibleItems = findFirstVisibleItemPositions(null)
            if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                pastVisibleItems = firstVisibleItems[0]
            }

            if (visibleItemCount + pastVisibleItems >= totalItemCount / 2 && !isLoading) {
                isLoading = true
                if (hasMorePages && !isRefreshing) {
                    isRefreshing = true
                    Handler().postDelayed({ refreshList.invoke(pageNumber) }, 200)
                }
            } else {
                isLoading = false
            }
        }
    }

    fun noMorePages() {
        hasMorePages = false
    }

    fun notifyMorePages() {
        isRefreshing = false
        pageNumber += 1
    }

}