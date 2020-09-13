package com.github.trxthix.developerslife.di

import com.github.trxthix.developerslife.BuildConfig
import com.github.trxthix.developerslife.data.DevLifeApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://developerslife.ru/"

@Module
interface NetworkModule {
    companion object {
        @AppSingleton
        @Provides
        fun provideApi(retrofit: Retrofit): DevLifeApi = retrofit.create(DevLifeApi::class.java)

        @Provides
        fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder().run {
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(MoshiConverterFactory.create(moshi))
            baseUrl(BASE_URL)
            client(client)
            build()
        }

        @Provides
        fun provideMoshi(): Moshi = Moshi.Builder().run {
            addLast(KotlinJsonAdapterFactory())
            build()
        }

        @Provides
        fun provideOkHttpClient(dispatcher: Dispatcher): OkHttpClient = OkHttpClient.Builder().run {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            dispatcher(dispatcher)
            build()
        }

        @Provides
        fun provideOkHttpDispatcher(): Dispatcher = Dispatcher().apply {
            maxRequests = 2
        }
    }
}