package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.core.utilities.LDialogUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.activity.MainActivity
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
            context?.let { c ->
                LDialogUtil.showDialog2(
                        context = c,
                        title = getString(R.string.warning_vn),
                        msg = getString(R.string.app_will_be_restart),
                        button1 = getString(R.string.ok),
                        button2 = getString(R.string.cancel),
                        onClickButton1 = {
                            LUIUtil.setDarkTheme(isDarkTheme = isOn)
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            c.startActivity(intent)
                            LActivityUtil.tranIn(c)
                        }
                )
            }
        }
    }
}
