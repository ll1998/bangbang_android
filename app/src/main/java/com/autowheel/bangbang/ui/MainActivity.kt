package com.autowheel.bangbang.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseActivity
import com.autowheel.bangbang.ui.index.IndexFragment
import com.autowheel.bangbang.ui.msg.MsgFragment
import com.autowheel.bangbang.ui.user.UserFragment
import com.autowheel.bangbang.utils.toastInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Xily on 2020/3/25.
 */
class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var exitTime: Long = 0
    private lateinit var fragments: Array<Fragment>
    private var currentTabIndex = 0
    private var index = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initFragment()
        initBottomNavigationView()
    }


    private fun initBottomNavigationView() {
        nav_view.enableAnimation(false)
        nav_view.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        nav_view.onNavigationItemSelectedListener = this
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_index -> changeFragmentIndex(0)
            R.id.action_msg -> changeFragmentIndex(1)
            R.id.action_user -> changeFragmentIndex(2)
        }
        return true
    }

    private fun initFragment() {
        val indexFragment = IndexFragment()
        val msgFragment = MsgFragment()
        val userFragment = UserFragment()
        fragments = arrayOf(indexFragment, msgFragment, userFragment)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, fragments[currentTabIndex])
            .show(fragments[currentTabIndex]).commit()
    }

    private fun changeFragmentIndex(currentIndex: Int) {
        index = currentIndex
        switchFragment()
    }

    private fun switchFragment() {
        val trx = supportFragmentManager.beginTransaction()
        trx.hide(fragments[currentTabIndex])
        if (!fragments[index].isAdded) {
            trx.add(R.id.fragment, fragments[index])
        }
        trx.show(fragments[index]).commit()
        currentTabIndex = index
    }

    override fun onBackPressed() {
        when {
            System.currentTimeMillis() - exitTime < 2000 -> super.onBackPressed()
            else -> {
                toastInfo("再按一次返回键退出程序")
                exitTime = System.currentTimeMillis()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {

    }
}