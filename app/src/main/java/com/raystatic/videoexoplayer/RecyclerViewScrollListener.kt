package com.raystatic.videoexoplayer

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewScrollListener : RecyclerView.OnScrollListener(){

    private var firstVisibleItem = 0
    private var visibleItemCount = 0

    @Volatile
    private var enabled = true
    private var preLoadCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (enabled){
            val manager = recyclerView.layoutManager
            require(manager is LinearLayoutManager){"Expected recyclerview to have linear layout manager"}
            val layoutManager = manager
            visibleItemCount = layoutManager.childCount
            firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

        }
    }

    abstract fun onItemIsFirstVisibleItem(index:Int)

    fun disableScrollListener(){
        enabled = false
    }

    fun enableScrollListener(){
        enabled = true
    }

    fun setPreLoadCount(preLoadCount:Int){
        this.preLoadCount = preLoadCount
    }

}