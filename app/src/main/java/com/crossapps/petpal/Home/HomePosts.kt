package com.crossapps.petpal.Home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crossapps.petpal.Posts.PostAdapter
import com.crossapps.petpal.R
import com.crossapps.petpal.Server.Request.GetPostRequest
import com.crossapps.petpal.Server.Response.LoginResponseData
import com.crossapps.petpal.Server.Response.PostResponse
import com.crossapps.petpal.Server.Response.PostResponseData
import com.crossapps.petpal.Server.RetrofitAPIAuth
import com.crossapps.petpal.Server.RetrofitApiAuthSingleTon
import com.crossapps.petpal.Server.TCApi
import com.crossapps.petpal.Util.Constant
import com.crossapps.petpal.Util.Logger
import com.crossapps.petpal.Util.PrefernceFile
import com.crossapps.petpal.Util.UtilityofActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home_posts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.logging.Handler


class HomePosts : Fragment() {

    var utilityofActivity: UtilityofActivity? = null
    var appCompatActivity: AppCompatActivity? = null
    var mConetxt: Context? = null
    var handler: Handler? = null


    var postAdapter: PostAdapter? = null
    var loginResponse: LoginResponseData? = null
    var postsList: PostResponse? = null
    var list: ArrayList<PostResponseData> = ArrayList()
    var isLoading: Boolean = false
    var pageNo: Int = 0
    var tcApi: TCApi? = null

    lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mConetxt = context
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.appCompatActivity = activity as AppCompatActivity?
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home_posts, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginResponse = Gson().fromJson(
            PrefernceFile.getInstance(context!!).getString(Constant.PREF_KEY_USER_DATA),
            LoginResponseData::class.java
        )

        tcApi = RetrofitAPIAuth.createService(TCApi::class.java, "token")



        utilityofActivity = UtilityofActivity(appCompatActivity!!)


        layoutManager = LinearLayoutManager(mConetxt!!)
        recyclerView.layoutManager = layoutManager


        list.clear()
        getMedia(0)

    }


    fun getMedia(pageNoLocal: Int) {
        utilityofActivity?.showProgressDialog()

//        val postRequest = GetPostRequest(loginResponse!!.userId, "HomePosts", pageNoLocal.toString())

        val call = tcApi?.callPostsApi()


        call?.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: retrofit2.Call<PostResponse>, response: Response<PostResponse>) {
                utilityofActivity?.dismissProgressDialog()

                try {


                    isLoading = true

                    postsList = response.body()



                    setUpAdapter(postsList!!.data)


                } catch (e: java.lang.Exception) {
                    utilityofActivity!!.toast("Something went wrong, Please try again.")
                }


            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                call.cancel()
                utilityofActivity?.dismissProgressDialog()
            }
        })

    }


    var totalItemCount: Int = 0
    var visibleItemCount: Int = 0
    var pastVisibleItemCount: Int = 0

    private fun setUpAdapter(data: ArrayList<PostResponseData>) {
        if (list.size == 0) {
            list = data
            if (list.isEmpty()) {
                no_content.visibility = View.VISIBLE
            } else no_content.visibility = View.GONE
            postAdapter = PostAdapter(mConetxt, list, appCompatActivity)
            recyclerView.adapter = postAdapter
        } else {
            val currentPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            list.addAll(data)
            postAdapter!!.notifyItemRangeInserted(layoutManager.itemCount, data.size)
            recyclerView.scrollToPosition(currentPosition)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItemCount =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (isLoading) {
                        if (visibleItemCount + pastVisibleItemCount >= totalItemCount) {
                            isLoading = false
                            pageNo += 1
                            Logger.d("Page No: ", pageNo.toString())
                            if (list.size % 10 == 0) {
                                getMedia(pageNo)
                            }
                        }
                    }
                }
            }

        })

    }


}

