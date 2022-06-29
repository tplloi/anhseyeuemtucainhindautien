package com.loitp.fragment

import android.app.AlertDialog
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

    private var dialog: AlertDialog? = null
    private fun setupViews() {
        fun setData() {
            val isDarkTheme = LUIUtil.isDarkTheme()
            swDarkTheme?.isChecked = isDarkTheme
        }

        setData()
        var isValid = true

        swDarkTheme.setOnCheckedChangeListener { _, isOn ->
            activity?.let { a ->
                if (isValid) {
                    dialog = LDialogUtil.showDialog2(
                        context = a,
                        title = getString(R.string.warning_vn),
                        msg = getString(R.string.app_will_be_restart),
                        button1 = getString(R.string.ok),
                        button2 = getString(R.string.cancel),
                        onClickButton1 = {
                            LUIUtil.setDarkTheme(isDarkTheme = isOn)
                            val intent = Intent(activity, MainActivity::class.java)
                            a.startActivity(intent)
                            LActivityUtil.tranIn(a)
                            a.finishAfterTransition()
                        },
                        onClickButton2 = {
                            isValid = false
                            setData()
                        }
                    )
                    dialog?.setCancelable(true)
                    dialog?.setOnCancelListener {
                        isValid = false
                        setData()
                    }
                } else {
                    isValid = true
                }
            }
        }
    }

    override fun onDestroyView() {
        dialog?.cancel()
        super.onDestroyView()
    }
}
