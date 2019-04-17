package com.wd.tech.adapter

import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.wd.tech.R

/**
 * date:2019/4/15
 * author:冯泽林{2019/4/15}
 * function:
 */
class CommunityFileAdapter(context: Context) : RecyclerView.Adapter<CommunityFileAdapter.ViewHolder>() {
    var context:Context ?=null
    var list:List<String> ?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(View.inflate(context, R.layout.item_filecommunity,null))
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.image_nicai.setImageURI(list!![p1])
    }

    fun setNiCai(split: MutableList<String>) {
        list=split
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_nicai:SimpleDraweeView = itemView.findViewById(R.id.image_nicai)
    }
}