package com.crossapps.petpal.YourPets

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crossapps.petpal.R
import com.crossapps.petpal.RoomModel.Entities.PetsModel
import com.crossapps.petpal.RoomModel.ViewModel.PetsViewModel
import com.crossapps.petpal.Util.UtilityofActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class YourPetsFragment : Fragment() {

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mContext: Context? = null
    var layoutManager: LinearLayoutManager? = null
    private var petAdapter: PetAdapter? = null
    var viewModel: PetsViewModel? = null


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

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        items.layoutManager = layoutManager

        petAdapter = PetAdapter(context, ArrayList<PetsModel>())

        items.adapter = petAdapter


        viewModel = ViewModelProviders.of(this).get(PetsViewModel::class.java)



        viewModel!!.petsList.observe(appCompatActivity!!,
            Observer<List<PetsModel>> { t -> petAdapter!!.addItems(t) })


    }
}