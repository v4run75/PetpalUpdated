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
import com.crossapps.petpal.Server.Response.PetResponse
import com.crossapps.petpal.Server.Response.PetResponseData
import com.crossapps.petpal.Server.TCApi
import com.crossapps.petpal.Util.UtilityofActivity
import com.webpulse.trafficcontrol.Server.RetrofitAPI
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class YourPetsFragment : Fragment() {

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mContext: Context? = null
    var layoutManager: LinearLayoutManager? = null
    private var petAdapter: PetAdapter? = null
    var viewModel: PetsViewModel? = null
    var tcApi: TCApi? = null
    var petReponse:PetResponse?=null
    var petsList:List<PetResponseData>?=ArrayList()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }

    override fun onStart() {
        super.onStart()
        getPets()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        utilityofActivity = UtilityofActivity(appCompatActivity!!)

        tcApi = RetrofitAPI.client.create<TCApi>(TCApi::class.java)

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        items.layoutManager = layoutManager

        petAdapter = PetAdapter(context, ArrayList<PetResponseData>())

        items.adapter = petAdapter


      /* TODO MVVM
        viewModel = ViewModelProviders.of(this).get(PetsViewModel::class.java)

        viewModel!!.petsList.observe(appCompatActivity!!,
            Observer<List<PetsModel>> { t -> petAdapter!!.addItems(t) })*/


    }

    private fun getPets() {

        utilityofActivity!!.showProgressDialog()

        val call = tcApi?.callPetsApi()

        call?.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: retrofit2.Call<PetResponse>, response: Response<PetResponse>) {
                utilityofActivity!!.dismissProgressDialog()
                try {
                    petReponse = response.body()


                    if (petReponse!!.success) {

                        petsList=petReponse!!.data

                        petAdapter!!.addItems(petsList)


                    } else {
                        utilityofActivity!!.toast(petReponse!!.message)
                    }
                } catch (e: Exception) {
                    utilityofActivity!!.toast("Something went wrong, Please try again.")
                }

            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                call.cancel()
                utilityofActivity!!.dismissProgressDialog()
                utilityofActivity!!.toast("Failed, Please Check Your Internet Connection")
            }
        })
    }



}