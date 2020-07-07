package com.loitp.activity

import android.os.Bundle
import android.os.Handler
import com.core.base.BaseFontActivity
import com.core.utilities.LUIUtil
import com.loitp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseFontActivity() {

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LUIUtil.createAdBanner(adView)
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    public override fun onResume() {
        adView.resume()
        super.onResume()
    }

    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        showShort(getString(R.string.press_again_to_exit))
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}
