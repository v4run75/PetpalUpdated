package com.crossapps.petpal.Server;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by CREATION on 10/18/2017.
 */
public class RetrofitApiAuthSingleTon {

    private static Retrofit retrofit;
    private static OkHttpClient client;

    public static Retrofit createRetrofitAuth(String authToken) {
        if (authToken != null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();


            okHttpBuilder.addInterceptor(loggingInterceptor);

            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            okHttpBuilder.addInterceptor(interceptor);

            client = okHttpBuilder.build();

            retrofit= new Retrofit.Builder().baseUrl("http://webpulse.co/pathprahari/web_apis/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass,String auth) {
        if (retrofit == null)
            return createRetrofitAuth(auth).create(serviceClass);
        else
            return retrofit.create(serviceClass);
    }

    public static void clearRetrofit() {
        retrofit = null;
    }

    //in any case where the current retrofit instance is required, call this method
    public static Retrofit getRetrofit(String auth) {
        if (retrofit == null)
            return createRetrofitAuth(auth);
        else
            return retrofit;
    }

}
