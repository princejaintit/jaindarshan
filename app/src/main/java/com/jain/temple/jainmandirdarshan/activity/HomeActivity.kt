package com.jain.temple.jainmandirdarshan.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId

import com.jain.temple.jainmandirdarshan.BuildConfig
import com.jain.temple.jainmandirdarshan.Fragment.AboutUsFragment
import com.jain.temple.jainmandirdarshan.Fragment.DashBoardFragment
import com.jain.temple.jainmandirdarshan.Fragment.FragmentDrawer
import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener
import com.jain.temple.jainmandirdarshan.scheduler.MyStartServiceReceiver
import com.jain.temple.jainmandirdarshan.R
import com.jain.temple.jainmandirdarshan.Service.SendPushNotificationServiceOreo
import com.jain.temple.jainmandirdarshan.util.SharedPreferenceManager
import com.jain.temple.jainmandirdarshan.util.UiUtils

import org.jsoup.Jsoup

import java.lang.ref.WeakReference

class HomeActivity : BaseActivity(), FragmentDrawerListener {
    private var mainCoordinatorLayout: CoordinatorLayout? = null
    private var doubleBackToExitPressedOnce = false
    private var chargerReceiver: MyStartServiceReceiver? = null


    private var mToolbar: Toolbar? = null
    private var drawerFragment: FragmentDrawer? = null

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        try {
            GetVersionCode(this).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mainCoordinatorLayout = findViewById<View>(R.id.mainCoordinatorLayout) as CoordinatorLayout

        drawerFragment = supportFragmentManager.findFragmentById(R.id.fragment_navigation_drawer) as FragmentDrawer
        drawerFragment!!.setUp(R.id.fragment_navigation_drawer, findViewById<View>(R.id.drawer_layout) as DrawerLayout, mToolbar)


        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        //  Log.w(TAG, "getInstanceId failed", task.getException());
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result!!.token
                    val sharedPreferenceManager = SharedPreferenceManager(applicationContext)
                    sharedPreferenceManager.token=token
                    val i = Intent(applicationContext, SendPushNotificationServiceOreo::class.java)
                    SendPushNotificationServiceOreo.enqueueWork(this, i)
                    // Log and toast
                    // Log.d(TAG, token);
                })




        drawerFragment!!.setDrawerListener(this)

        // display the first navigation drawer view on app launch
        displayView(0)

        chargerReceiver = MyStartServiceReceiver()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            registerReceiver(
                    chargerReceiver,
                    IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }


    }



    override fun onDrawerItemSelected(view: View, position: Int) {
        displayView(position)
    }

    private fun displayView(position: Int) {
        var fragment: Fragment? = null
        var title = ""
        when (position) {
            0 -> {
                fragment = DashBoardFragment()
                title = getString(R.string.dash_board_title)
            }
            1 -> startActivity(Intent(this, NamokarActivity::class.java).putExtra("fromSplash", false))
            2 -> startActivity(Intent(this@HomeActivity, FavouriteActivity::class.java))
            3 -> startActivity(Intent(this@HomeActivity, AudioListActivity::class.java))
            4 -> startActivity(Intent(this@HomeActivity, DownloadListActivity::class.java))
            5 -> UiUtils().appShareContent(this)
            6 -> openAppInPlayStore(this)

            7 -> openMyOtherApp()
            8 -> {
                fragment = AboutUsFragment()
                title = getString(R.string.about_us_title)
            }
        }/* AlertDialog alertDialog= createAppRatingDialog(getString(R.string.app_name),getString(R.string.rate_app_message));
                alertDialog.show();*/

        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_body, fragment)
            // fragmentTransaction.addToBackStack("null");
            fragmentTransaction.commit()

            // set the toolbar title
            supportActionBar!!.title = title
        }
    }

    private inner class GetVersionCode (activity: Activity) : AsyncTask<Void, String, String>() {

        private val loginActivityWeakRef: WeakReference<HomeActivity>

        init {
            this.loginActivityWeakRef = WeakReference(activity as HomeActivity)
        }

        override fun doInBackground(vararg voids: Void): String? {

            var newVersion: String? = null
            try {


                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")

                        .timeout(30000)

                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")

                        .referrer("http://www.google.com")

                        .get()

                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")

                        .first()

                        .ownText()

                return newVersion

            } catch (e: Exception) {
                return newVersion
            }

        }

        override fun onPostExecute(onlineVersion: String?) {
            super.onPostExecute(onlineVersion)
            var currentVersion: String? = null
            try {
                currentVersion = packageManager.getPackageInfo(packageName, 0).versionName

                Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion)
                if (onlineVersion != null && !onlineVersion.isEmpty()) {
                    if (!currentVersion!!.equals(onlineVersion, ignoreCase = true)) {
                        if (loginActivityWeakRef.get() != null && !loginActivityWeakRef.get()!!.isFinishing()) {
                            val alertDialog = createDeleteDialog(getString(R.string.app_name), getString(R.string.new_version_available))
                            alertDialog.show()
                        } else {

                        }
                    }
                }

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

        }
    }


    private fun createAppRatingDialog(rateAppTitle: String, rateAppMessage: String): AlertDialog {
        return AlertDialog.Builder(this).setPositiveButton(getString(R.string.dialog_app_rate)) { paramAnonymousDialogInterface, paramAnonymousInt -> openAppInPlayStore(this@HomeActivity) }/*.setNegativeButton(getString(R.string.dialog_your_feedback), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                openFeedback(MainActivity.this);

            }
        })*/.setNeutralButton(getString(R.string.dialog_ask_later)) { paramAnonymousDialogInterface, paramAnonymousInt -> paramAnonymousDialogInterface.dismiss() }.setMessage(rateAppMessage).setTitle(rateAppTitle).create()
    }


    fun showsnackBarMessage(msg: String) {
        val snackbar = Snackbar.make(mainCoordinatorLayout!!, msg, Snackbar.LENGTH_LONG)

        snackbar.show()
    }


    fun setTitle(title: String) {
        supportActionBar!!.title = title
    }


    private fun createDeleteDialog(title: String, message: String): AlertDialog {
        return AlertDialog.Builder(this).setPositiveButton(getString(R.string.update)) { paramAnonymousDialogInterface, paramAnonymousInt ->
            openAppInPlayStore(this@HomeActivity)
            paramAnonymousDialogInterface.dismiss()
            //  skipActivity();
        }.setNegativeButton(getString(R.string.no_thanks)) { paramAnonymousDialogInterface, paramAnonymousInt -> paramAnonymousDialogInterface.dismiss() }/*.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                skipActivity();

            }
        })*/
                .setCancelable(false)
                .setMessage(message).setTitle(title).create()
    }

    public override fun onPause() {

        super.onPause()
    }

    public override fun onResume() {
        super.onResume()

    }


    public override fun onDestroy() {

        super.onDestroy()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(chargerReceiver)
        }


    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        // Toast.makeText(this,getString(R.string.press_exit) , Toast.LENGTH_SHORT).show();
        val snackbar = Snackbar.make(mainCoordinatorLayout!!, getString(R.string.press_exit), Snackbar.LENGTH_SHORT)

        snackbar.show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu_option, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notification -> startActivity(Intent(this, NotificationActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = HomeActivity::class.java.simpleName
    }


}
