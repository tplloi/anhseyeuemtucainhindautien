package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LUIUtil
import com.loitp.R
import kotlinx.android.synthetic.main.frm_setting.*

@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val isDarkTheme = LUIUtil.isDarkTheme()
        swDarkTheme.isChecked = isDarkTheme

        swDarkTheme.setOnCheckedChangeListener { _, isOn ->
            //TODO
            LUIUtil.setDarkTheme(isDarkTheme = isOn)
        }
    }
}
