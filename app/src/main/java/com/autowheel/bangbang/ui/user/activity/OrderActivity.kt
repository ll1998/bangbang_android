package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachBookListBean
import com.autowheel.bangbang.ui.user.adapter.OrderAdapter
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.activity_order.*

/**
 * Created by Xily on 2020/5/2.
 */
class OrderActivity : BackBaseActivity() {
    private lateinit var adapter: OrderAdapter
    private var list = arrayListOf<CoachBookListBean>()
    override fun getToolbarTitle(): String {
        return "预约列表"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order
    }

    override fun initViews(savedInstanceState: Bundle?) {
        swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.blue))
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        rv_order.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(list)
        adapter.listener = {
            agreeOrder(list[it].help_id, list[it].book_userlist[list[it].selectIndex].uid)
        }
        rv_order.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCoachBookList()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
                if (result.data.isEmpty()) {
                    toastInfo("空空如也")
                }
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        }, finallyBlock = {
            swipe_refresh_layout.isRefreshing = false
        })
    }

    private fun agreeOrder(id: Int, uid: Int) {

    }
}