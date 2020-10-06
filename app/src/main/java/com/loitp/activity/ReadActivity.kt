package com.loitp.activity

import android.os.Bundle
import com.annotation.IsShowAdWhenExit
import com.annotation.LayoutId
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.loitp.R

@LayoutId(R.layout.activity_read)
@LogTag("loitppReadActivity")
@IsShowAdWhenExit(true)
class ReadActivity : BaseFontActivity() {

    companion object {
        const val KEY_POSITION_CHAP = "KEY_POSITION_CHAP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupViewModels()

        val position = intent?.getIntExtra(KEY_POSITION_CHAP, 0)
        logD("onCreate position $position")
    }

    private fun setupData() {

    }

    private fun setupViews() {

    }

    private fun setupViewModels() {

    }
}
