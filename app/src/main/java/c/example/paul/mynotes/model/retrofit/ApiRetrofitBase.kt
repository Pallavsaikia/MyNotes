package c.example.paul.mynotes.model.retrofit

import android.content.Context
import c.example.paul.mynotes.BuildConfig


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class  ApiRetrofitBase{
    private val baseURL = "http://103.80.249.103:9002/api/s/"
    private var retrofit : Retrofit? = null

    private var logging = HttpLoggingInterceptor()
    private fun getHttpLogClient(context: Context) : OkHttpClient{
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
//                .addInterceptor(TokenRewriteInterceptor(context))
                .build()
    }
    fun getReply(context:Context): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpLogClient(context))
                    .build()
        }
        return retrofit
    }



}