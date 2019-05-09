package com.crossapps.petpal

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.crossapps.petpal.RegisterPet.RegisterPet
import com.crossapps.petpal.Util.Helper
import com.crossapps.petpal.Util.UtilityofActivity
import com.crossapps.petpal.YourPets.YourPetsFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = this
    var context: Context? = null
    var handler: Handler? = null
    var helper: Helper? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.your_pets -> {
                    fab.show()
                    fab.setOnClickListener {
                        val intent = Intent(this@MainActivity, RegisterPet::class.java)
//                        startActivityForResult(intent, 2)
                        startActivity(intent)

                    }


                    val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

                    if (currentFragment is YourPetsFragment) {

                    } else {
                        helper = Helper.instance
                        helper?.moveFragment(YourPetsFragment(), null, R.id.container, appCompatActivity!!)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                R.id.home -> {

                    fab.show()
                    fab.setOnClickListener {
                        val intent = Intent(this@MainActivity, RegisterPet::class.java)
//                        startActivityForResult(intent, 2)
                        startActivity(intent)

                    }


                    val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

                    if (currentFragment is HomeFragment) {

                    } else {
                        helper = Helper.instance
                        helper?.moveFragment(HomeFragment(), null, R.id.container, appCompatActivity!!)
                        return@OnNavigationItemSelectedListener true
                    }
                }
            }

            return@OnNavigationItemSelectedListener false
        }


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


        setContentView(R.layout.activity_main)

        context = this

        utilityofActivity= UtilityofActivity(appCompatActivity!!)

        supportActionBar!!.title = ""

        fab.show()
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterPet::class.java)
            startActivity(intent)
        }

        helper = Helper.instance
        helper?.moveFragment(HomeFragment(), null, R.id.container, appCompatActivity!!)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
