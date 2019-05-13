package com.crossapps.petpal.NavigationDrawer


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.crossapps.petpal.Login.Login
import com.crossapps.petpal.R
import com.crossapps.petpal.Util.Constant
import com.crossapps.petpal.Util.PrefernceFile
import com.crossapps.petpal.Util.custom.TextViewOpenSans
import java.util.*


class NavigationDrawerAdapter(
    private val context: Context,
    private val modelFeedArrayList: ArrayList<DrawerItem>,
    private val drawerLayout: DrawerLayout,
    private val navigation: BottomNavigationView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_nagivation_item, parent, false)

        return MyViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {

        val holder = viewHolder as MyViewHolder

        val item = modelFeedArrayList[viewHolder.getAdapterPosition()]
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, item.drawable))
        holder.option.text = item.optionName


        holder.navItem.setOnClickListener {
            when (item.id) {
//                1 -> {
//                    drawerLayout.closeDrawers()
//                    if (PrefernceFile.getInstance(context).getString(Constant.isLogin) == "true") {
//                        val currentFragment =
//                            (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.container)
//
//                        if (currentFragment is ProfileFragment) {
//
//                        } else {
//                            val helper = Helper.instance
//                            navigation.selectedItemId = R.id.profile
//                            helper.moveFragment(ProfileFragment(), null, R.id.frame, context)
//                        }
//                    } else {
//                        context.startActivity(Intent(context, Login::class.java))
//                    }
//                }
//                2 -> {
//                    drawerLayout.closeDrawers()
//                    if (PrefernceFile.getInstance(context).getString(Constant.isLogin) == "true") {
//                        context.startActivity(Intent(context, MyAddresses::class.java))
//                    } else {
//                        context.startActivity(Intent(context, Login::class.java))
//                    }
//                }
//                3 -> {
//                    if (PrefernceFile.getInstance(context).getString(Constant.isLogin) == "true") {
//                        context.startActivity(Intent(context, Cart::class.java))
//                    } else {
//                        context.startActivity(Intent(context, Login::class.java))
//                    }
//                }
//                4 -> {
//                    drawerLayout.closeDrawers()
//                    context.startActivity(Intent(context, Cart::class.java))
//                }
//                5 -> {
//                    drawerLayout.closeDrawers()
//                    context.startActivity(Intent(context, CustomerSupport::class.java))
//                }
//                6 -> {
//                    val i = Intent(Intent.ACTION_SEND)
//                    i.type = "text/plain"
//                    i.putExtra(Intent.EXTRA_TEXT, "http://www.url.com")
//                    context.startActivity(Intent.createChooser(i, "Share URL"))
//                }
//                7 -> {
//                    drawerLayout.closeDrawers()
//                    context.startActivity(Intent(context, AboutUs::class.java))
//                }
                8 -> {
                    PrefernceFile.getInstance(context).setString(Constant.isLogin, "false")
                    context.startActivity(Intent(context, Login::class.java))
                    (context as Activity).finishAffinity()
                }
                else -> {
                }
            }
        }


        if (PrefernceFile.getInstance(context).getString(Constant.isLogin) != "true") {

            when (item.id) {
//                1 -> {
//                    holder.navItem.visibility = View.VISIBLE
//                }
//                2 -> {
//                    holder.option.setTextColor(ContextCompat.getColor(context, R.color.hologrey))
//                    holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.hologrey))
//                }
//                3 -> {
//                    holder.option.setTextColor(ContextCompat.getColor(context, R.color.hologrey))
//                    holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.hologrey))
//                }
                8 -> {
                    holder.navItem.visibility = View.GONE
                }
                else -> {
                }
            }
        } else {
            when (item.id) {
//                1 -> {
//                    holder.navItem.visibility = View.VISIBLE
//                    holder.option.text = "My Profile"
//                }
                8 -> {
                    holder.navItem.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return modelFeedArrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    inner class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var option: TextViewOpenSans
        internal var icon: ImageView
        internal var navItem: LinearLayout


        init {


            option = itemView.findViewById(R.id.option)
            icon = itemView.findViewById(R.id.icon)
            navItem = itemView.findViewById(R.id.navItem)
        }
    }

}
