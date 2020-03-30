package com.autowheel.bangbang.model.network;

import com.autowheel.bangbang.BuildConfig;
import com.autowheel.bangbang.ConstantsKt;
import com.autowheel.bangbang.model.DataManager;
import com.autowheel.bangbang.model.network.service.ApiService;
import com.didichuxing.doraemonkit.kit.network.okhttp.DoraemonInterceptor;
import com.didichuxing.doraemonkit.kit.network.okhttp.DoraemonWeakNetworkInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Xily on 2020/3/25.
 */
public class RetrofitHelper {
    private volatile static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitHelper.class) {
                if (apiService == null) {
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
                    Interceptor tokenInterceptor = chain -> {
                        String token = DataManager.INSTANCE.getToken();
                        if (token.isEmpty()) {
                            return chain.proceed(chain.request());
                        }
                        Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("token", token);
                        return chain.proceed(builder.build());
                    };
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(httpLoggingInterceptor)
                            .addInterceptor(tokenInterceptor)
                            .addNetworkInterceptor(new DoraemonWeakNetworkInterceptor())
                            .addInterceptor(new DoraemonInterceptor())
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                    apiService = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl(ConstantsKt.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
