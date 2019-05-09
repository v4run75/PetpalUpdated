package com.crossapps.petpal.Server;

import android.text.TextUtils;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by CREATION on 10/18/2017.
 */
public class RetrofitAPIAuth {


    private static Retrofit retrofit = null;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static String Base_Url = "http://webpulse.co/pathprahari/web_apis/";
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create());

//    public static <S> S createService(Class<S> serviceClass) {
//        return createService(serviceClass, null);
//    }


    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {

            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//            if (!httpClient.interceptors().contains(interceptor)||!httpClient.interceptors().contains(httpLoggingInterceptor)) {
                httpClient.addInterceptor(interceptor);
                httpClient.addInterceptor(httpLoggingInterceptor);


                builder.client(httpClient.build());

                if(retrofit==null){
                    retrofit = builder.build();
                }
//            }


//            if () {
//                httpClient.addInterceptor(interceptor);
//
//                builder.client(httpClient.build());
//                retrofit = builder.build();
//            }
        }

        return retrofit.create(serviceClass);
    }

}
