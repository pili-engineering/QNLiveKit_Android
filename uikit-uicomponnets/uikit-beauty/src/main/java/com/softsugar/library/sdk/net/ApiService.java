package com.softsugar.library.sdk.net;
import com.softsugar.library.log.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static APIInterface createApiService() {
        return retrofit.create(APIInterface.class);
    }

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(APIInterface.BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new CommonParamsInterceptor())
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
              //  .addInterceptor(new FakeApkInterceptor())
                .build();
    }

    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }
}
