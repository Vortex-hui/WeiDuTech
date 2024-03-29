package com.wd.tech.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.wd.tech.R
import com.wd.tech.bean.Card

/**
 * date:2019/4/25
 * author:冯泽林{2019/4/25}
 * function:
 */
class CardSmallAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<CardSmallAdapter.ViewHolder>() {
    var context:Context ?=null
    var list:List<String> ?=null
    init {
        this.context=context
        this.list=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(View.inflate(context, R.layout.item_small_card,null))
    }

    override fun getItemCount(): Int {
        if(list!!.size>0&&list!=null){
            return list!!.size
        }
        return 0
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.image_nicai.setImageURI(list!![p1])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_nicai: SimpleDraweeView = itemView.findViewById(R.id.image_nicai)
    }
}