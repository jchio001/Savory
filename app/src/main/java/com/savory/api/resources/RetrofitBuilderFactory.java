package com.savory.api.resources;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * (Hopefully not) made in China, unlike my chains & new gold watch.
 */
public class RetrofitBuilderFactory {

    private static final Retrofit BASE_RETROFIT;

    private static final OkHttpClient BASE_OK_HTTP_CLIENT;

    private static final HttpLoggingInterceptor HTTP_LOGGING_INTERCEPTOR;

    private static final String STUBBED_BASE_URL = "http://pleasereplace.thisbaseurl";

    static {
        HTTP_LOGGING_INTERCEPTOR = new HttpLoggingInterceptor();
        HTTP_LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.BODY);

        BASE_OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .build();

        BASE_RETROFIT = new Retrofit.Builder()
            .client(BASE_OK_HTTP_CLIENT)
            .baseUrl(STUBBED_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
    }

    /**
     * Creates a new Retrofit.Builder() with everything but the base URL initialized properly. This
     * approach allows for the underlying OkHttpClient & MoshiConverterFactory to be pooled across
     * instances, which saves a good amount of memory.
     * @return A cookie cutter Retrofit.Builder() instance
     */
    public static Retrofit.Builder createBase(Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = BASE_OK_HTTP_CLIENT.newBuilder();

        // This is to ensure that the properly formatted request/response will be logged, as OkHttp
        // executes interceptors in sequential order. This means that if the logging interceptor is
        // placed first, an inaccurate request will be logged.
        for (int i = 0, length = interceptors.length; i < length; ++i) {
            okHttpClientBuilder.addInterceptor(interceptors[i]);
        }

        okHttpClientBuilder.addInterceptor(HTTP_LOGGING_INTERCEPTOR);

        return BASE_RETROFIT.newBuilder()
            .client(okHttpClientBuilder.build());
    }
}
