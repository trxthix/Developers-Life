package com.github.trxthix.developerslife.data

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface DevLifeApi {
    @GET("/random?json=true")
    fun randPost(): Observable<Post>

    @GET("https://developerslife.ru/latest/{page}?json=true&pageSize=15")
    fun latestPosts(@Path("page") page: Int): Single<PostResult>

    @GET("https://developerslife.ru/top/{page}?json=true&pageSize=15")
    fun topPosts(@Path("page") page: Int): Single<PostResult>
}