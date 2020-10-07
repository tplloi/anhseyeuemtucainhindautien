package com.loitp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.AnimationAdapter
import com.core.base.BaseApplication
import com.core.utilities.LAppResource
import com.loitp.R
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.view_row_chap.view.*

@LogTag("ChapAdapter")
class ChapAdapter : AnimationAdapter() {

    private var listChap = ArrayList<String>()
    var onClickRootListener: ((String, Int) -> Unit)? = null
    private var isDarkTheme: Boolean = false

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        this.isDarkTheme = isDarkTheme
        notifyDataSetChanged()
    }

    fun setData(listChap: List<String>) {
        this.listChap.clear()
        this.listChap.addAll(listChap)
//        logD("setData " + BaseApplication.gson.toJson(this.listChap))
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(chap: String) {

            if (isDarkTheme) {
                itemView.cardView.setCardBackgroundColor(LAppResource.getColor(R.color.dark900))
                itemView.tvChap.setTextColor(Color.WHITE)
                itemView.ivNext.setColorFilter(Color.WHITE)
            } else {
                itemView.cardView.setCardBackgroundColor(Color.WHITE)
                itemView.tvChap.setTextColor(Color.BLACK)
                itemView.ivNext.setColorFilter(Color.BLACK)
            }

            itemView.tvChap.text = chap
            itemView.tvChap.setSafeOnClickListener {
                onClickRootListener?.invoke(chap, bindingAdapterPosition)
            }
            setAnimation(viewToAnimate = itemView, position = bindingAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.view_row_chap, parent,
                    false
            ))

    override fun getItemCount(): Int = listChap.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(chap = listChap[position])
        }
    }

}
