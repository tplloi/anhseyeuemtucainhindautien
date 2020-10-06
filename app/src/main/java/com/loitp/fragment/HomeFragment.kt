package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LayoutId
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.loitp.activity.ReadActivity
import com.loitp.adapter.ChapAdapter
import com.loitp.viewmodels.MainViewModel
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_home.*

@LayoutId(R.layout.frm_home)
@LogTag("loitppHomeFragment")
class HomeFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null
    private var concatAdapter: ConcatAdapter? = null
    private var chapAdapter: ChapAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
        context?.let {
            mainViewModel?.loadListChap(context = it)
        }
    }

    private fun setupViews() {
        chapAdapter = ChapAdapter()
        chapAdapter?.let { ca ->

            ca.onClickRootListener = { _, position ->
                val intent = Intent(context, ReadActivity::class.java)
                intent.putExtra(ReadActivity.KEY_POSITION_CHAP, position)
                startActivity(intent)
                LActivityUtil.tranIn(context)
            }

            val listOfAdapters = listOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>(ca)
            concatAdapter = ConcatAdapter(listOfAdapters)
        }
        rvChap.layoutManager = LinearLayoutManager(context)
        rvChap.adapter = concatAdapter

        fabReadContinue.setSafeOnClickListener {
            //TODO
        }
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.eventLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                if (isLoading) {
                    indicatorView.smoothToShow()
                } else {
                    indicatorView.smoothToHide()
                }
            })

            mvm.listChapLiveData.observe(viewLifecycleOwner, Observer { listChap ->
                logD("<<<listChapLiveData " + BaseApplication.gson.toJson(listChap))
                chapAdapter?.setData(listChap)
            })
        }

    }
}
