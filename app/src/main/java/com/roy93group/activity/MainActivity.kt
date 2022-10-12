package com.roy93group.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.common.Constants
import com.loitpcore.core.helper.adHelper.AdHelperActivity
import com.loitpcore.core.helper.gallery.GalleryCoreSplashActivity
import com.loitpcore.core.utilities.*
import com.roy93group.R
import com.roy93group.app.LApplication
import com.roy93group.fragment.HomeFrm
import com.roy93group.fragment.SettingFrm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer_end.*
import kotlinx.android.synthetic.main.view_drawer_main.*
import kotlinx.android.synthetic.main.view_drawer_start.view.*

/**
 * Created by Loitp on 2022.10.11
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("MainActivity")
class MainActivity : BaseFontActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val isFullData = LApplication.isFullData()

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            /* activity = */ this,
            /* drawerLayout = */ drawerLayout,
            /* toolbar = */ toolbar,
            /* openDrawerContentDescRes = */ R.string.navigation_drawer_open,
            /* closeDrawerContentDescRes = */ R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navViewStart.setNavigationItemSelectedListener(this)
        drawerLayout.useCustomBehavior(Gravity.START)
        drawerLayout.useCustomBehavior(Gravity.END)

        //cover
        LImageUtil.load(
            context = this,
            any = getString(R.string.link_cover),
            imageView = navViewStart.getHeaderView(0).ivCover
        )

        tvAd.text = LStoreUtil.readTxtFromRawFolder(nameOfRawFile = R.raw.ad)

        switchHomeScreen()
    }

    private fun switchHomeScreen() {
        if (isFullData) {
            navViewStart.menu.performIdentifierAction(R.id.navHome, 0)
            navViewStart.menu.findItem(R.id.navHome).isChecked = true
        } else {
            navViewStart.menu.performIdentifierAction(R.id.navSetting, 0)
            navViewStart.menu.findItem(R.id.navSetting).isChecked = true
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBaseBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBaseBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showShortInformation(msg = getString(R.string.press_again_to_exit), isTopAnchor = false)
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    private var idItemChecked = R.id.navHome
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> {
                if (isFullData) {
                    idItemChecked = R.id.navHome
                    LScreenUtil.replaceFragment(
                        activity = this,
                        containerFrameLayoutIdRes = R.id.flContainer,
                        fragment = HomeFrm(),
                        isAddToBackStack = false
                    )
                } else {
                    showShortInformation(msg = getString(R.string.err_unknown), isTopAnchor = false)
                }
            }
            R.id.navGallery -> {
                if (isFullData) {
                    val intent = Intent(this, GalleryCoreSplashActivity::class.java)
                    intent.putExtra(Constants.BKG_SPLASH_SCREEN, getString(R.string.link_cover_flickr))
                    intent.putExtra(Constants.BKG_ROOT_VIEW, R.drawable.bkg_black)
                    //neu muon remove albumn nao thi cu pass id cua albumn do
                    val removeAlbumFlickrList = ArrayList<String>()
                    removeAlbumFlickrList.add(Constants.FLICKR_ID_STICKER)
                    intent.putStringArrayListExtra(
                        Constants.KEY_REMOVE_ALBUM_FLICKR_LIST,
                        removeAlbumFlickrList
                    )
                    startActivity(intent)
                    LActivityUtil.tranIn(this)
                } else {
                    showShortInformation(msg = getString(R.string.err_unknown), isTopAnchor = false)
                }
            }
            R.id.navRateApp -> {
                LSocialUtil.rateApp(activity = this, packageName = packageName)
            }
            R.id.navMoreApp -> {
                LSocialUtil.moreApp(this)
            }
            R.id.navFacebookFanPage -> {
                LSocialUtil.likeFacebookFanpage(this)
            }
            R.id.navShareApp -> {
                LSocialUtil.shareApp(this)
            }
            R.id.navSetting -> {
                idItemChecked = R.id.navSetting
                LScreenUtil.replaceFragment(
                    activity = this,
                    containerFrameLayoutIdRes = R.id.flContainer,
                    fragment = SettingFrm(),
                    isAddToBackStack = false
                )
            }
            R.id.navChatWithDev -> {
                LSocialUtil.chatMessenger(this)
            }
            R.id.navAd -> {
                if (isFullData) {
                    val intent = Intent(this, AdHelperActivity::class.java)
                    intent.putExtra(Constants.AD_HELPER_IS_ENGLISH_LANGUAGE, false)
                    startActivity(intent)
                    LActivityUtil.tranIn(this)
                } else {
                    showShortInformation(msg = getString(R.string.err_unknown), isTopAnchor = false)
                }
            }
            R.id.navPolicy -> {
                LSocialUtil.openBrowserPolicy(context = this)
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        navViewStart.postDelayed({
            if (idItemChecked == R.id.navHome) {
                navViewStart.menu.findItem(R.id.navHome).isChecked =
                    true//hight light navViewStart menu home
            } else if (idItemChecked == R.id.navSetting) {
                navViewStart.menu.findItem(R.id.navSetting).isChecked =
                    true//hight light navViewStart menu setting
            }
        }, 500)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_drawer_end, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionRightDrawer -> {
                drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
