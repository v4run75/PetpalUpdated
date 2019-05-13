package com.crossapps.petpal.SplashScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.crossapps.petpal.Login.Login
import com.crossapps.petpal.MainActivity
import com.crossapps.petpal.R

class SplashScreen : AppCompatActivity() {
    var mContext: Context? = null
    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)
        mContext = this

//        Glide.with(this).load(R.drawable.car).into(splash)

        handler = Handler()
        handler!!.postDelayed({

            val intent = Intent(this@SplashScreen, Login::class.java)
            startActivity(intent)
            finish()

        }, 1000)
    }
}


