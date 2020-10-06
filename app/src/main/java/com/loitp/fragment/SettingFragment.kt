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
import com.core.utilities.LSharedPrefsUtil
import com.google.ads.interactivemedia.v3.internal.it
import com.loitp.R
import com.loitp.activity.ReadActivity
import com.loitp.adapter.ChapAdapter
import com.loitp.constant.Constant
import com.loitp.viewmodels.MainViewModel
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_home.*
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

        swDarkTheme.setOnCheckedChangeListener { _, isOn ->
            if (isOn) {
                LSharedPrefsUtil.instance.putBoolean(Constant.KEY_IS_DARK_THEME, true)
            } else {
                LSharedPrefsUtil.instance.putBoolean(Constant.KEY_IS_DARK_THEME, false)
            }
        }
    }
}
