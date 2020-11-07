package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.viewmodels.MainViewModel

@LogTag("loitppReadFragment")
class ReadFragment(
        val currentPosition: Int
) : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_chap
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
    }

    private fun setupViews() {
        logD("setupViews currentPosition $currentPosition")
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->

        }
    }

}
