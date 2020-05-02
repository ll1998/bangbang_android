package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachBean
import com.autowheel.bangbang.ui.user.adapter.UserCoachRecordAdapter
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.activity_user_coach_record.*

/**
 * Created by Xily on 2020/5/2.
 */
class UserCoachRecordActivity : BackBaseActivity() {
    private lateinit var adapter: UserCoachRecordAdapter
    private var list = arrayListOf<CoachBean>()
    override fun getToolbarTitle(): String {
        return "辅导记录"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_coach_record
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
        rv_coach_record.layoutManager = LinearLayoutManager(this)
        adapter = UserCoachRecordAdapter(list)
        rv_coach_record.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCoachHistory()
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
}