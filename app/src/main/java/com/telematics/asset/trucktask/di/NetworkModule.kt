package com.telematics.asset.trucktask.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.telematics.asset.trucktask.api.ApiService
import com.telematics.asset.trucktask.util.BaseApiUtils
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("API_LOG", message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor, interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS) // connect timeout
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {

        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BaseApiUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}