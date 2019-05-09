package com.crossapps.petpal.YourPets

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crossapps.petpal.R
import com.crossapps.petpal.Server.Response.PetResponseData
import com.crossapps.petpal.Util.UtilityofActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class YourPetsFragment : Fragment(){

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mContext: Context? = null
    var layoutManager: LinearLayoutManager? = null
    private var petAdapter:PetAdapter?=null
    private var petsList: ArrayList<PetResponseData>? = ArrayList()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utilityofActivity = UtilityofActivity(appCompatActivity!!)

        layoutManager = LinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)
        items.layoutManager = layoutManager


        petsList!!.add(PetResponseData("1","Scooby","https://images.dog.ceo/breeds/eskimo/n02109961_16718.jpg"))
        petsList!!.add(PetResponseData("2","Scooby","https://images.dog.ceo/breeds/eskimo/n02109961_16718.jpg"))
        petsList!!.add(PetResponseData("3","Scooby","https://images.dog.ceo/breeds/eskimo/n02109961_16718.jpg"))

        petAdapter = PetAdapter(context, petsList)

        items.adapter = petAdapter


    }
}