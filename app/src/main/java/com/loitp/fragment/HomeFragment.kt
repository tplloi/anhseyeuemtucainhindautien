package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.core.base.BaseFragment
import com.google.ads.interactivemedia.v3.internal.it
import com.loitp.R
import com.loitp.app.LApplication
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_home.*

class HomeFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModels()
        context?.let {
            mainViewModel?.loadListChap(context = it)
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    override fun setTag(): String? {
        return "loitpp" + javaClass.simpleName
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
                logD("<<<listChapLiveData " + LApplication.gson.toJson(listChap))
            })
        }

    }
}
