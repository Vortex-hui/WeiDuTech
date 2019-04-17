package com.wd.tech.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.transition.Explode
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.hjm.bottomtabbar.BottomTabBar
import com.wd.tech.Fragment.CommunityFragment
import com.wd.tech.Fragment.InformationFragment
import com.wd.tech.Fragment.MessageFragment
import com.wd.tech.Fragment.MineFragment
import com.wd.tech.R
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        window.enterTransition = Explode().setDuration(1000)
        window.exitTransition = Explode().setDuration(1000)
        bottom_tab_bar.init(supportFragmentManager)
                .setImgSize(90f, 90f)
                .setFontSize(12f)
                .setTabPadding(4f, 6f, 10f)
                .setChangeColor(Color.BLACK, Color.GRAY)
                .addTabItem("资讯", R.mipmap.common_tab_informatiion_s, R.mipmap.common_tab_information_n, InformationFragment::class.java)
                .addTabItem("消息", R.mipmap.common_tab_message_s, R.mipmap.common_tab_message_n, MessageFragment::class.java)
                .addTabItem("社区", R.mipmap.common_tab_community_s, R.mipmap.common_tab_community_n, CommunityFragment::class.java)
                .isShowDivider(false)
                .setOnTabChangeListener(object : BottomTabBar.OnTabChangeListener {
                    override fun onTabChange(position: Int, name: String?) {
                    }
                })
        val supportFragmentManager = supportFragmentManager
        var mineFragment = MineFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_frame, mineFragment)
                .commit()
        bottom_tab_bar.measure(0, 0)
       // drawer_layout.setScrimColor(Color.TRANSPARENT);//去除阴影
        var width = bottom_tab_bar.getMeasuredWidth() * 0.2f
        bottom_tab_bar.setTranslationX(-width);                 //底布局左移
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {

            }

            override fun onDrawerSlide(p0: View, p1: Float) {
                //bottom_tab_bar.setTranslationX(-width+width*p1);
                bottom_tab_bar.setTranslationX(p0.getMeasuredWidth() * p1);
            }

            override fun onDrawerClosed(p0: View) {
            }

            override fun onDrawerOpened(p0: View) {
            }

        })

    }

    private var firstTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val secondTime = System.currentTimeMillis()
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0)
            } else {
                Toast.makeText(this@ShowActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                firstTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
