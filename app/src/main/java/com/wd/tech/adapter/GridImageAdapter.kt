package com.wd.tech.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.luck.picture.lib.tools.StringUtils
import com.wd.tech.R

import java.io.File
import java.util.ArrayList


/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.pictureselector.adapter
 * email：893855882@qq.com
 * data：16/7/27
 */
class GridImageAdapter(private val context: Context) : RecyclerView.Adapter<GridImageAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater
    private var list: MutableList<LocalMedia> = ArrayList<LocalMedia>()
    private var selectMax = 9
    private lateinit var addClickListener: ()->Unit
    protected var mItemClickListener: OnItemClickListener? = null

    fun onAddPicClickListener( addClickListener: ()->Unit) {
       this.addClickListener = addClickListener
    }

    init {
        mInflater = LayoutInflater.from(context)
    }

    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    fun setList(list: MutableList<LocalMedia>) {
        this.list = list
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal var mImg: ImageView
        internal var ll_del: LinearLayout
        internal var tv_duration: TextView

        init {
            mImg = view.findViewById(R.id.fiv) as ImageView
            ll_del = view.findViewById(R.id.ll_del) as LinearLayout
            tv_duration = view.findViewById(R.id.tv_duration) as TextView
        }
    }

    override fun getItemCount(): Int {
        return if (list.size < selectMax) {
            list.size + 1
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowAddItem(position)) {
            TYPE_CAMERA
        } else {
            TYPE_PICTURE
        }
    }

    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_filter_image,
                viewGroup, false)
        return ViewHolder(view)
    }

    private fun isShowAddItem(position: Int): Boolean {
        val size = if (list.size == 0) 0 else list.size
        return position == size
    }

    /**
     * 设置值
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.drawable.add_img)
            viewHolder.mImg.setOnClickListener {
                addClickListener.invoke()
            }
            viewHolder.ll_del.visibility = View.INVISIBLE
        } else {
            viewHolder.ll_del.visibility = View.VISIBLE
            viewHolder.ll_del.setOnClickListener {
                val index = viewHolder.adapterPosition
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION) {
                    list.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, list.size)
                    // Debug.i("delete position:", index + "--->remove after:" + list.size());
                }
            }
            val media = list[position]
            val mimeType = media.getMimeType()
            var path = ""
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath()
            } else if (media.isCompressed() || media.isCut() && media.isCompressed()) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath()
            } else {
                // 原图
                path = media.getPath()
            }
            // 图片
            if (media.isCompressed()) {
                Log.i("compress image result:", "${File(media.getCompressPath()).length() / 1024}k")
                Log.i("压缩地址::", media.getCompressPath())
            }

            Log.i("原图地址::", media.getPath())
            val pictureType = PictureMimeType.isPictureType(media.getPictureType())
            if (media.isCut()) {
                Log.i("裁剪地址::", media.getCutPath())
            }
            val duration = media.getDuration()
            viewHolder.tv_duration.visibility = if (pictureType == PictureConfig.TYPE_VIDEO)
                View.VISIBLE
            else
                View.GONE
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.tv_duration.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio)
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0)
            } else {
                val drawable = ContextCompat.getDrawable(context, R.drawable.video_icon)
                StringUtils.modifyTextViewDrawable(viewHolder.tv_duration, drawable, 0)
            }
            viewHolder.tv_duration.setText(DateUtils.timeParse(duration))
            if (mimeType == PictureMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource(R.drawable.audio_placeholder)
            } else {
                val options = RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.white)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(viewHolder.itemView.context)
                        .load(path)
                        .apply(options)
                        .into(viewHolder.mImg)
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener { v ->
                    val adapterPosition = viewHolder.adapterPosition
                    mItemClickListener!!.onItemClick(adapterPosition, v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    companion object {
        val TYPE_CAMERA = 1
        val TYPE_PICTURE = 2
    }
}
