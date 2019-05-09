package com.crossapps.petpal

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.crossapps.petpal.RegisterPet.RegisterPet
import com.crossapps.petpal.Util.Helper
import com.crossapps.petpal.Util.UtilityofActivity
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


//                    val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
//
//                    if (currentFragment is Traffic) {
//
//                    } else {
//                        helper = Helper.instance
//                        helper?.moveFragment(Traffic(), null, R.id.container, appCompatActivity!!)
                        return@OnNavigationItemSelectedListener true
//                    }
                }
                R.id.home -> {

                    return@OnNavigationItemSelectedListener true
                }
            }

            return@OnNavigationItemSelectedListener false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        utilityofActivity= UtilityofActivity(appCompatActivity!!)

        supportActionBar!!.title = ""

        fab.show()
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterPet::class.java)
            startActivity(intent)
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
