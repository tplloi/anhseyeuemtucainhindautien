package com.loitp.activity

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.core.base.BaseFontActivity
import com.core.utilities.LImageUtil
import com.core.utilities.LSocialUtil
import com.core.utilities.LUIUtil
import com.google.android.material.navigation.NavigationView
import com.loitp.R
import kotlinx.android.synthetic.main.activity_main.*
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
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
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
