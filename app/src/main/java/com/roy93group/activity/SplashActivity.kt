package com.roy93group.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseApplication
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.*
import com.loitpcore.model.App
import com.loitpcore.views.setSafeOnClickListener
import com.permissionx.guolindev.PermissionX
import com.roy93group.BuildConfig
import com.roy93group.R
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.Call
import java.io.IOException

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
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
        textViewVersion.text = "Phiên bản ${BuildConfig.VERSION_NAME}"
        tvPolicy.setSafeOnClickListener {
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
                    Manifest.permission.ACCESS_FINE_LOCATION,
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
                        val isNeedCheckReady = true
                        if (isNeedCheckReady) {
                            checkReady()
                        } else {
                            isCheckReadyDone = true
                            goToHome()
                        }
                    } else {
                        finish()
                        LActivityUtil.tranOut(this)
                    }
                    isShowDialogCheck = false
                }
        }

        val isCanWriteSystem = LScreenUtil.checkSystemWritePermission()
        if (isCanWriteSystem) {
            checkPer()
        } else {
            val alertDialog = LDialogUtil.showDialog2(
                context = this,
                title = "Need Permissions",
                msg = "This app needs permission to allow modifying system settings",
                button1 = getString(R.string.ok),
                button2 = getString(R.string.cancel),
                onClickButton1 = {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    LActivityUtil.tranIn(this@SplashActivity)
                },
                onClickButton2 = {
                    onBaseBackPressed()
                }
            )
            alertDialog.setCancelable(false)
        }
    }

    private fun goToHome() {
        if (isAnimDone && isCheckReadyDone) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
            this.finishAfterTransition()
        }
    }

    private fun showDialogNotReady() {
        runOnUiThread {
            val title = if (LConnectivityUtil.isConnected()) {
                getString(R.string.app_is_not_ready)
            } else {
                getString(R.string.check_ur_connection)
            }
            val alertDial = LDialogUtil.showDialog2(
                context = this,
                title = getString(R.string.warning),
                msg = title,
                button1 = getString(R.string.exit),
                button2 = getString(R.string.try_again),
                onClickButton1 = {
                    onBaseBackPressed()
                },
                onClickButton2 = {
                    checkReady()
                }
            )
            alertDial.setCancelable(false)
        }
    }

    private fun checkReady() {
        if (LPrefUtil.getCheckAppReady()) {
            val app = LPrefUtil.getGGAppSetting()
            val isFullData = app.config?.isFullData == true
            if (isFullData) {
                isCheckReadyDone = true
                goToHome()
                return
            } else {
                //continue to download config from drive
            }
        }
        //https://drive.google.com/drive/u/0/folders/1fBHp13gaIebFCaeiUoCuijjw-TvvAtDI
        val linkGGDriveConfigSetting =
            "https://drive.google.com/uc?export=download&id=1bjIaEuvDJhrdlP__LNEf43__pkEVV75D"
        LStoreUtil.getSettingFromGGDrive(
            linkGGDriveSetting = linkGGDriveConfigSetting,
            onGGFailure = { _: Call, _: IOException ->
                showDialogNotReady()
            },
            onGGResponse = { app: App? ->
                logD(">>>checkReady " + BaseApplication.gson.toJson(app))
                if (app == null || app.config?.isReady == false) {
                    showDialogNotReady()
                } else {
                    LPrefUtil.setCheckAppReady(true)
                    LPrefUtil.setGGAppSetting(app)
                    isCheckReadyDone = true
                    goToHome()
                }
            }
        )
    }
}
