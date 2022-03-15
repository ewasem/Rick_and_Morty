package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.BuildConfig
import br.com.ewapps.rickandmorty.BuildConfig.API_KEY
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TmdbClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/tv/60625/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val logging = HttpLoggingInterceptor()

    val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor {
                    chain ->
                val builder = chain.request().newBuilder()
                builder.header("apiKey", API_KEY)
                return@Interceptor chain.proceed(builder.build())
            }
        )
        logging.level = HttpLoggingInterceptor.Level.BODY
        addNetworkInterceptor(logging)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

    val tmdbService: TmdbService by lazy {
        retrofit.create(TmdbService::class.java)
    }
}