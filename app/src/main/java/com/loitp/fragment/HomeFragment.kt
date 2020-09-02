package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.viewmodels.MainViewModel

class HomeFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModels()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)


    }
}
