package kr.uncode.firebaselog;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static final String BASE_URL = "https://dapi.kakao.com";


    private static Retrofit init() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder().addInterceptor(getLogger()).build();
    }



    private static HttpLoggingInterceptor getLogger() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static RetrofitService getService() {
        return init().create(RetrofitService.class);
    }
}


