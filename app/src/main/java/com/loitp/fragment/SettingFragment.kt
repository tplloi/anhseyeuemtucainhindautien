package com.loitp.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.annotation.LayoutId
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LAppResource
import com.core.utilities.LSharedPrefsUtil
import com.loitp.R
import com.loitp.constant.Constant
import kotlinx.android.synthetic.main.frm_setting.*

@LayoutId(R.layout.frm_setting)
@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val isDarkTheme = LSharedPrefsUtil.instance.getBoolean(Constant.KEY_IS_DARK_THEME, true)
        swDarkTheme.isChecked = isDarkTheme
        setupTheme(isDarkTheme = isDarkTheme)

        swDarkTheme.setOnCheckedChangeListener { _, isOn ->
            setupTheme(isDarkTheme = isOn)
        }
    }

    private fun setupTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            LSharedPrefsUtil.instance.putBoolean(Constant.KEY_IS_DARK_THEME, true)
            layoutRootView.setBackgroundColor(Color.BLACK)
            swDarkTheme.setTextColor(Color.WHITE)
        } else {
            LSharedPrefsUtil.instance.putBoolean(Constant.KEY_IS_DARK_THEME, false)
            layoutRootView.setBackgroundColor(Color.WHITE)
            swDarkTheme.setTextColor(Color.BLACK)
        }
    }
}
