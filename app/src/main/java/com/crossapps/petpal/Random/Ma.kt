package com.crossapps.petpal.Random

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.crossapps.petpal.R
import com.crossapps.petpal.Util.UtilityofActivity

class Ma : AppCompatActivity() {

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = this
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.hologrey)
        }

        setContentView(R.layout.activity_ma)

        context = this

        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        utilityofActivity!!.configureToolbar(appCompatActivity!!)


    }
}
