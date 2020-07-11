package com.loitp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.helper.gallery.GalleryCoreSplashActivity
import com.core.utilities.*
import com.google.android.material.navigation.NavigationView
import com.loitp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer_end.*
import kotlinx.android.synthetic.main.view_drawer_main.*
import kotlinx.android.synthetic.main.view_drawer_start.view.*

class MainActivity : BaseFontActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navViewStart.setNavigationItemSelectedListener(this)
        drawerLayout.useCustomBehavior(Gravity.START)
        drawerLayout.useCustomBehavior(Gravity.END)

        //cover
        LImageUtil.load(context = activity, url = getString(R.string.link_cover), imageView = navViewStart.getHeaderView(0).ivCover)

        tvAd.text = LStoreUtil.readTxtFromRawFolder(context = activity, nameOfRawFile = R.raw.ad)
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navGallery -> {
                val intent = Intent(activity, GalleryCoreSplashActivity::class.java)
                intent.putExtra(Constants.AD_UNIT_ID_BANNER, getString(R.string.str_b))
                intent.putExtra(Constants.BKG_SPLASH_SCREEN, getString(R.string.link_cover))
                intent.putExtra(Constants.BKG_ROOT_VIEW, R.drawable.bkg_black)
                //neu muon remove albumn nao thi cu pass id cua albumn do
                val removeAlbumFlickrList = ArrayList<String>()
                removeAlbumFlickrList.add(Constants.FLICKR_ID_STICKER)
                intent.putStringArrayListExtra(Constants.KEY_REMOVE_ALBUM_FLICKR_LIST, removeAlbumFlickrList)
                startActivity(intent)
                LActivityUtil.tranIn(activity)
            }
            R.id.navRateApp -> {
                LSocialUtil.rateApp(activity = activity, packageName = packageName)
            }
            R.id.navMoreApp -> {
                //TODO pass developer name
                LSocialUtil.moreApp(activity)
            }
            R.id.navFacebookFanPage -> {
                LSocialUtil.likeFacebookFanpage(activity)
            }
            R.id.navShareApp -> {
                LSocialUtil.shareApp(activity)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
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
