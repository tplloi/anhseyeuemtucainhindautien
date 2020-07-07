package com.loitp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.core.base.BaseFontActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LDialogUtil
import com.core.utilities.LSocialUtil
import com.core.utilities.LUIUtil
import com.interfaces.Callback2
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.loitp.BuildConfig
import com.loitp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseFontActivity() {
    private var isAnimDone = false
    private var isCheckPermissionDone = false
    private var isShowDialogCheck = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LUIUtil.setDelay(mls = 1500, runnable = Runnable {
            isAnimDone = true
            goToHome()
        })
        textViewVersion.text = "Version ${BuildConfig.VERSION_NAME}"
        tvPolicy.setOnClickListener {
            LSocialUtil.openBrowserPolicy(context = activity)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isShowDialogCheck) {
            checkPermission()
        }
    }

    private fun checkPermission() {
        isShowDialogCheck = true
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            isCheckPermissionDone = true
                            goToHome()
                        } else {
                            showShouldAcceptPermission()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                        isShowDialogCheck = true
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    private fun goToHome() {
        if (isAnimDone && isCheckPermissionDone) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(activity)
            LUIUtil.setDelay(mls = 1000, runnable = Runnable {
                finish()
            })
        }
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setTag(): String {
        return javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    private fun showSettingsDialog() {
        val alertDialog = LDialogUtil.showDialog2(
                context = activity,
                title = getString(R.string.need_permisson),
                msg = getString(R.string.need_permisson_to_use_app),
                button1 = getString(R.string.setting),
                button2 = getString(R.string.deny),
                callback2 = object : Callback2 {
                    override fun onClick1() {
                        isShowDialogCheck = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, 101)
                    }

                    override fun onClick2() {
                        onBackPressed()
                    }
                })
        alertDialog.setCancelable(false)
    }

    private fun showShouldAcceptPermission() {
        val alertDialog = LDialogUtil.showDialog2(
                context = activity,
                title = getString(R.string.need_permisson),
                msg = getString(R.string.need_permisson_to_use_app),
                button1 = getString(R.string.yes),
                button2 = getString(R.string.deny),
                callback2 = object : Callback2 {
                    override fun onClick1() {
                        checkPermission()
                    }

                    override fun onClick2() {
                        onBackPressed()
                    }
                })
        alertDialog.setCancelable(false)
    }
}
