package com.wd.tech.Fragment


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import chat.activity.fragment.ContactsFragment
import chat.activity.fragment.ConversationListFragment
import chat.controller.MenuItemController
import chat.view.MenuItemView

import com.wd.tech.R
import com.wd.tech.activity.AddFriendOrGroupActivity
import com.wd.tech.adapter.MyPagerAdapter
import com.wd.tech.base.BaseFragment
import com.wd.tech.mvp.Constanct
import com.wd.tech.mvp.Presenter
import kotlinx.android.synthetic.main.fragment_message.*

/**
 * 消息
 */
class MessageFragment : BaseFragment<Constanct.View, Constanct.Presenter>(), Constanct.View {


    var adapter: MyPagerAdapter? = null
    var mlist: MutableList<Fragment>? = null
    var pop: PopupWindow? = null
    private var mMenuView: View? = null
    private var mMenuPopWindow: PopupWindow? = null
    private var mMenuItemView: MenuItemView? = null
    private var mMenuController: MenuItemController? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

    override fun initPresenter(): Constanct.Presenter {
        return Presenter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MyPagerAdapter(activity!!.supportFragmentManager)
        mlist = ArrayList()
        mlist!!.add(ConversationListFragment())
        mlist!!.add(ContactsFragment())
        adapter!!.setFragmentList(mlist as ArrayList<Fragment>)
        view_pager.adapter = adapter
        view_pager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                when (p0) {
                    0 -> {
                        rb_msg.isChecked = true
                        rb_msg.setTextColor(Color.parseColor("#ffffff"))
                        rb_linkman.setTextColor(Color.parseColor("#333333"))
                    }
                    1 -> {
                        rb_linkman.isChecked = true
                        rb_msg.setTextColor(Color.parseColor("#333333"))
                        rb_linkman.setTextColor(Color.parseColor("#ffffff"))
                    }
                }
            }

        })

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_msg -> {
                    view_pager.currentItem = 0
                    rb_msg.setTextColor(Color.parseColor("#ffffff"))
                    rb_linkman.setTextColor(Color.parseColor("#333333"))
                }
                R.id.rb_linkman -> {
                    view_pager.currentItem = 1
                    rb_msg.setTextColor(Color.parseColor("#333333"))
                    rb_linkman.setTextColor(Color.parseColor("#ffffff"))
                }
            }
        }
        mMenuView = activity!!.layoutInflater.inflate(R.layout.drop_down_menu, null)
        mMenuPopWindow = PopupWindow(mMenuView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true)
        mMenuItemView = MenuItemView(mMenuView)
        mMenuItemView!!.initModule()
        mMenuController = MenuItemController(this)
        mMenuItemView!!.setListeners(mMenuController)
        img_add.setOnClickListener {
            showPopWindow()
        }
    }

    fun showPopWindow() {
        mMenuPopWindow!!.setTouchable(true)
        mMenuPopWindow!!.setOutsideTouchable(true)
        mMenuPopWindow!!.setBackgroundDrawable(BitmapDrawable(resources, null as Bitmap?))
        if (mMenuPopWindow!!.isShowing()) {
            mMenuPopWindow!!.dismiss()
        } else {
            mMenuPopWindow!!.showAsDropDown(img_add, -10, -5)
        }
    }

    fun dismissPopWindow() {
        if (mMenuPopWindow!!.isShowing()) {
            mMenuPopWindow!!.dismiss()
        }
    }
    private fun showPop() {
        val view = LayoutInflater.from(context).inflate(R.layout.message_pop_layout, null)
        pop = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val add_friend_linear = view.findViewById<LinearLayout>(R.id.add_friend_linear)
        val creat_group_linear = view.findViewById<LinearLayout>(R.id.creat_group_linear)
        pop!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pop!!.isOutsideTouchable = true
        pop!!.isFocusable = true
        pop!!.showAsDropDown(img_add, 100, 0)
        add_friend_linear.setOnClickListener {
            startActivity(Intent(context, AddFriendOrGroupActivity::class.java))
            pop!!.dismiss()
        }
        creat_group_linear.setOnClickListener {

        }
    }


    override fun initData() {


    }

    override fun View(any: Any) {

    }


}

