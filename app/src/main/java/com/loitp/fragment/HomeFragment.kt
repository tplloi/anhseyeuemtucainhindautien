package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.core.base.BaseFragment
import com.loitp.R
import com.views.menu.residemenu.ResideMenu

class HomeFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }
}
