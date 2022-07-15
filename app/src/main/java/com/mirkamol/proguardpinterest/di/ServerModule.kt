package com.mirkamol.proguardpinterest.di
import com.mirkamol.proguardpinterest.network.ApiService
import com.mirkamol.proguardpinterest.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    class ServerModule {
        @Provides
        @Singleton
        fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun getApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)

        @Provides
        @Singleton
        fun getClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            //   .addInterceptor(ChuckInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("Authorization", "Client-ID bValYORwDm0_OREt1Oc85l50EThOKKBbNhWtqImJbNU")
                //   if (sharedPref.user != ""){
                //     builder.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIrOTk4OTQxMTEwNzE3Iiwicm9sZXMiOlsiQ0xJRU5UIl0sImV4cCI6MTY1NTA0OTkyMn0.jmnpKdM0Dub-ylE_LesdGWXpmzhHGGr5DcrZj7bRYJI")
                //  }
                chain.proceed(builder.build())
            })
            .build()

    }
