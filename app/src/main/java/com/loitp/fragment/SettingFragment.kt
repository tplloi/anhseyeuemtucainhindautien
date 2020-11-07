package com.loitp.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.annotation.LayoutId
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LSharedPrefsUtil
import com.data.EventBusData
import com.loitp.R
import kotlinx.android.synthetic.main.frm_setting.*

@LayoutId(R.layout.frm_setting)
@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val isDarkTheme = LSharedPrefsUtil.instance.getBoolean(Constants.KEY_IS_DARK_THEME, true)
        swDarkTheme.isChecked = isDarkTheme
        setupTheme(isDarkTheme = isDarkTheme)

        swDarkTheme.setOnCheckedChangeListener { _, isOn ->
            EventBusData.instance.sendThemeChange(isDarkTheme = isOn)
        }
    }

    private fun setupTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            LSharedPrefsUtil.instance.putBoolean(Constants.KEY_IS_DARK_THEME, true)
            layoutRootView.setBackgroundColor(Color.BLACK)
            swDarkTheme.setTextColor(Color.WHITE)
        } else {
            LSharedPrefsUtil.instance.putBoolean(Constants.KEY_IS_DARK_THEME, false)
            layoutRootView.setBackgroundColor(Color.WHITE)
            swDarkTheme.setTextColor(Color.BLACK)
        }
    }

    override fun onThemeChange(event: EventBusData.ThemeEvent) {
        super.onThemeChange(event)
        setupTheme(isDarkTheme = event.isDarkTheme)
    }
}
