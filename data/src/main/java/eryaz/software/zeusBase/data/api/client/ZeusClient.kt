package eryaz.software.zeusBase.data.api.client

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import eryaz.software.zeusBase.data.api.interceptors.AuthorizationInterceptor
import eryaz.software.zeusBase.data.api.interceptors.HttpLoggingInterceptor
import eryaz.software.zeusBase.data.api.interceptors.UnAuthorizedInterceptor
import eryaz.software.zeusBase.data.api.services.*
import okhttp3.OkHttpClient
import eryaz.software.zeusBase.data.BuildConfig
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ZeusClient {

    fun provideApi(): AuthApiService {
        return getRetrofit(BuildConfig.BASE_URL).create(AuthApiService::class.java)
    }

    fun provideUserApi(): UserApiService {
        return getRetrofit(BuildConfig.BASE_URL).create(UserApiService::class.java)
    }

    fun provideWorkActivityApi(): WorkActivityService {
        return getRetrofit(BuildConfig.BASE_URL).create(WorkActivityService::class.java)
    }

    fun provideBarcodeApi(): BarcodeService {
        return getRetrofit(BuildConfig.BASE_URL).create(BarcodeService::class.java)
    }

    fun providePlacementApi(): PlacementService {
        return getRetrofit(BuildConfig.BASE_URL).create(PlacementService::class.java)
    }

    fun provideOrderApi(): OrderService {
        return getRetrofit(BuildConfig.BASE_URL).create(OrderService::class.java)
    }

    fun provideCountingApi(): CountingService {
        return getRetrofit(BuildConfig.BASE_URL).create(CountingService::class.java)
    }

    private fun getRetrofit(baseUrl: String): Retrofit {
        val client = OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(UnAuthorizedInterceptor())
            .addInterceptor(AuthorizationInterceptor())
            .addNetworkInterceptor(HttpLoggingInterceptor.getInterceptor())
            .build()

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}