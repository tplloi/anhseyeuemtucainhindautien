package com.loitp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.utilities.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.loitp.BuildConfig
import com.loitp.R
import com.model.GG
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.Call

@LogTag("SplashActivity")
class SplashActivity : BaseFontActivity() {
    private var isAnimDone = false
    private var isCheckReadyDone = false
    private var isShowDialogCheck = false

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        LUIUtil.setDelay(mls = 1500, runnable = {
            isAnimDone = true
            goToHome()
        })
        textViewVersion.text = "Version ${BuildConfig.VERSION_NAME}"
        tvPolicy.setOnClickListener {
            LSocialUtil.openBrowserPolicy(context = this)
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
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            checkReady()
                        } else {
                            showShouldAcceptPermission()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                        isShowDialogCheck = true
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    private fun goToHome() {
        if (isAnimDone && isCheckReadyDone) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
            finishAfterTransition()
        }
    }

    private fun showSettingsDialog() {
        val alertDialog = LDialogUtil.showDialog2(
                context = this,
                title = getString(R.string.need_permisson),
                msg = getString(R.string.need_permisson_to_use_app),
                button1 = getString(R.string.setting),
                button2 = getString(R.string.deny),
                onClickButton1 = {
                    isShowDialogCheck = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, 101)
                },
                onClickButton2 = {
                    onBackPressed()
                }
        )
        alertDialog.setCancelable(false)
    }

    private fun showShouldAcceptPermission() {
        val alertDialog = LDialogUtil.showDialog2(
                context = this,
                title = getString(R.string.need_permisson),
                msg = getString(R.string.need_permisson_to_use_app),
                button1 = getString(R.string.yes),
                button2 = getString(R.string.deny),
                onClickButton1 = {
                    checkPermission()
                },
                onClickButton2 = {
                    onBackPressed()
                }
        )
        alertDialog.setCancelable(false)
    }

    private fun showDialogNotReady() {
        runOnUiThread {
            val title = if (LConnectivityUtil.isConnected()) {
                getString(R.string.app_is_not_ready)
            } else {
                getString(R.string.check_ur_connection)
            }
            val alertDial = LDialogUtil.showDialog2(context = this,
                    title = getString(R.string.warning),
                    msg = title,
                    button1 = getString(R.string.exit),
                    button2 = getString(R.string.try_again),
                    onClickButton1 = {
                        onBackPressed()
                    },
                    onClickButton2 = {
                        checkReady()
                    }
            )
            alertDial.setCancelable(false)
        }
    }

    private fun checkReady() {

        fun setReady() {
            runOnUiThread {
                isCheckReadyDone = true
                goToHome()
            }
        }

        if (LPrefUtil.getCheckAppReady()) {
            setReady()
            return
        }
        val linkGGDriveCheckReady = getString(R.string.link_gg_drive)
        LStoreUtil.getTextFromGGDrive(
                linkGGDrive = linkGGDriveCheckReady,
                onGGFailure = { _: Call, e: Exception ->
                    e.printStackTrace()
                    showDialogNotReady()
                },
                onGGResponse = { listGG: ArrayList<GG> ->
                    logD("getGG listGG: -> " + BaseApplication.gson.toJson(listGG))

                    fun isReady(): Boolean {
                        listGG.forEach { gg ->
                            if (packageName == gg.pkg) {
                                return gg.isReady
                            }
                        }
                        return false
                    }

                    val isReady = isReady()
                    if (isReady) {
                        LPrefUtil.setCheckAppReady(value = true)
                        setReady()
                    } else {
                        showDialogNotReady()
                    }
                }
        )
    }
}
