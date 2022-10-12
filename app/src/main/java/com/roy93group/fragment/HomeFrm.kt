package com.roy93group.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFragment
import com.loitpcore.core.utilities.LActivityUtil
import com.loitpcore.core.utilities.LDialogUtil
import com.loitpcore.core.utilities.LSharedPrefsUtil
import com.loitpcore.views.setSafeOnClickListener
import com.roy93group.R
import com.roy93group.activity.ReadActivity
import com.roy93group.adapter.ChapAdapter
import com.roy93group.common.AppConstant
import com.roy93group.common.Presets
import com.roy93group.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.frm_home.*
import kotlinx.android.synthetic.main.frm_home.konfettiView
import kotlinx.android.synthetic.main.frm_home.pb

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("HomeFrm")
class HomeFrm : BaseFragment() {
    private var mainViewModel: MainViewModel? = null
    private var concatAdapter = ConcatAdapter()
    private var chapAdapter = ChapAdapter()

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()

        mainViewModel?.loadListChap()
    }

    private fun setupViews() {
        konfettiView.start(Presets.festive())
        chapAdapter.onClickRootListener = { _, position ->
            goToReadScreen(position = position, scroll = 0)
        }

        val listOfAdapters = listOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>(chapAdapter)
        concatAdapter = ConcatAdapter(listOfAdapters)

        rvChap.layoutManager = LinearLayoutManager(context)
        rvChap.adapter = concatAdapter

        fabReadContinue.setSafeOnClickListener {
            val currentItem = LSharedPrefsUtil.instance.getInt(AppConstant.KEY_CURRENT_POSITION)
            val scroll = LSharedPrefsUtil.instance.getInt(AppConstant.KEY_SCROLL)
//            logD("fabReadContinue currentItem $currentItem, scroll $scroll")
            goToReadScreen(position = currentItem, scroll = scroll)
        }
    }

    private fun goToReadScreen(position: Int, scroll: Int) {
        val i = Intent(context, ReadActivity::class.java)
        i.putExtra(ReadActivity.KEY_POSITION_CHAP, position)
        i.putExtra(ReadActivity.KEY_SCROLL, scroll)
        mainViewModel?.listChapLiveData?.value?.let {
            val list = it as ArrayList
            i.putStringArrayListExtra(ReadActivity.KEY_LIST_DATA, list)
        }
        startActivity(i)
        LActivityUtil.tranIn(context)
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.eventLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    LDialogUtil.showProgress(pb)
                } else {
                    LDialogUtil.hideProgress(pb)
                }
            }

            mvm.listChapLiveData.observe(viewLifecycleOwner) { listChap ->
//                logD("<<<listChapLiveData " + BaseApplication.gson.toJson(listChap))
                chapAdapter.setData(listChap)
            }
        }

    }
}
