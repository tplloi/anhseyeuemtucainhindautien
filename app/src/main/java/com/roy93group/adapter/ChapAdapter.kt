package com.roy93group.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.R
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.adapter.BaseAdapter
import com.loitpcore.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.view_row_chap.view.*

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("ChapAdapter")
class ChapAdapter : BaseAdapter() {

    private var listChap = ArrayList<String>()
    var onClickRootListener: ((String, Int) -> Unit)? = null

    fun setData(listChap: List<String>) {
        this.listChap.clear()
        this.listChap.addAll(listChap)
//        logD("setData " + BaseApplication.gson.toJson(this.listChap))
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(chap: String) {

            itemView.tvChap.text = chap
            itemView.tvChap.setSafeOnClickListener {
                onClickRootListener?.invoke(chap, bindingAdapterPosition)
            }
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
