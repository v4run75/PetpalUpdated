package com.crossapps.petpal

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.crossapps.petpal.CreatePost.CreatePost
import com.crossapps.petpal.Home.HomePosts
import com.crossapps.petpal.NavigationDrawer.DrawerItem
import com.crossapps.petpal.NavigationDrawer.NavigationDrawerAdapter
import com.crossapps.petpal.RegisterPet.RegisterPet
import com.crossapps.petpal.Util.Helper
import com.crossapps.petpal.Util.UtilityofActivity
import com.crossapps.petpal.YourPets.YourPetsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = this
    var context: Context? = null
    var handler: Handler? = null
    var helper: Helper? = null
    var drawerLayout: DrawerLayout? = null

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
                    val intent = Intent(this@MainActivity, CreatePost::class.java)
//                        startActivityForResult(intent, 2)
                    startActivity(intent)

                }


                val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

                if (currentFragment is HomePosts) {

                } else {
                    helper = Helper.instance
                    helper?.moveFragment(HomePosts(), null, R.id.container, appCompatActivity!!)
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

        utilityofActivity = UtilityofActivity(appCompatActivity!!)
        utilityofActivity!!.configureDrawerToolbar(appCompatActivity!!)
        configureNavigationDrawer()

//        supportActionBar!!.title = ""

        fab.show()
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterPet::class.java)
            startActivity(intent)
        }

        helper = Helper.instance
        helper?.moveFragment(HomePosts(), null, R.id.container, appCompatActivity!!)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun configureNavigationDrawer() {
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        val arrayList = ArrayList<DrawerItem>()

        arrayList.add(DrawerItem(1, R.drawable.ic_menu_black, "Login"))
        arrayList.add(DrawerItem(2, R.drawable.ic_menu_black, "My Addresses"))
        arrayList.add(DrawerItem(3, R.drawable.ic_menu_black, "My Orders"))
        arrayList.add(DrawerItem(4, R.drawable.ic_menu_black, "My Cart"))
        arrayList.add(DrawerItem(5, R.drawable.ic_menu_black, "Customer Support"))
        arrayList.add(DrawerItem(6, R.drawable.ic_menu_black, "Share"))
        arrayList.add(DrawerItem(7, R.drawable.ic_menu_black, "About Us"))
        arrayList.add(DrawerItem(8, R.drawable.ic_menu_black, "Logout"))


        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listDrawer.layoutManager = layoutManager


        val adapter = NavigationDrawerAdapter(context!!, arrayList, drawerLayout!!, navigation)
        listDrawer.adapter = adapter


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Android home
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }// manage other entries if you have it ...
        return true
    }

}
