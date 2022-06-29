package com.loitp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.utilities.*
import com.loitp.BuildConfig
import com.loitp.R
import com.model.GG
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.Call

@SuppressLint("CustomSplashScreen")
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

        fun checkPer() {
            isShowDialogCheck = true
            val color = if (LUIUtil.isDarkTheme()) {
                Color.WHITE
            } else {
                Color.BLACK
            }
            PermissionX.init(this)
                .permissions(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                .setDialogTintColor(color, color)
                .onExplainRequestReason { scope, deniedList, _ ->
                    val message = getString(R.string.app_name) + getString(R.string.needs_per)
                    scope.showRequestReasonDialog(
                        permissions = deniedList,
                        message = message,
                        positiveText = getString(R.string.allow),
                        negativeText = getString(R.string.deny)
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        permissions = deniedList,
                        message = getString(R.string.per_manually_msg),
                        positiveText = getString(R.string.ok),
                        negativeText = getString(R.string.cancel)
                    )
                }
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        isCheckReadyDone = true
                        goToHome()
                    } else {
                        finish()
                        LActivityUtil.tranOut(this)
                    }
                    isShowDialogCheck = false
                }
        }

//        val isCanWriteSystem = LScreenUtil.checkSystemWritePermission()
//        if (isCanWriteSystem) {
//            checkPer()
//        } else {
//            val alertDialog = LDialogUtil.showDialog2(
//                context = this,
//                title = "Need Permissions",
//                msg = "This app needs permission to allow modifying system settings",
//                button1 = getString(R.string.ok),
//                button2 = getString(R.string.cancel),
//                onClickButton1 = {
//                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
//                    intent.data = Uri.parse("package:$packageName")
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
//                    LActivityUtil.tranIn(this@SplashActivity)
//                },
//                onClickButton2 = {
//                    onBackPressed()
//                }
//            )
//            alertDialog.setCancelable(false)
//        }

        checkPer()
    }

    private fun goToHome() {
        if (isAnimDone && isCheckReadyDone) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
            finishAfterTransition()
        }
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
        logD("<<<linkGGDriveCheckReady $linkGGDriveCheckReady")
        LStoreUtil.getTextFromGGDrive(
            linkGGDrive = linkGGDriveCheckReady,
            onGGFailure = { _: Call, e: Exception ->
                e.printStackTrace()
                showDialogNotReady()
            },
            onGGResponse = { listGG: ArrayList<GG> ->
                logD(">>>getGG listGG: -> " + BaseApplication.gson.toJson(listGG))

                fun isReady(): Boolean {
                    return listGG.any {
                        it.pkg == packageName && it.isReady
                    }
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
