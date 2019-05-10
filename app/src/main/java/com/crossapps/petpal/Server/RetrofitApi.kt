package com.webpulse.trafficcontrol.Server

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


/**
 * Created by Server on 1/25/2018.
 */
object RetrofitAPI {


    private var retrofit: Retrofit? = null

    val Base_Url = "https://petpal-android.000webhostapp.com/index.php/"


    val client: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }



            return retrofit!!
        }




}
