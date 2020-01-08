package stas.batura.imagegetterkotl.data.net

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


object RetrofitClient {

    val BASE_URL = "https://cataas.com/"

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://cataas.com/").build()
    }

    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object.
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    object netApi {
        val retrofitServise : API_COUR by lazy {
            retrofit.create(API_COUR::class.java)
        }
    }

    private interface API {
        @GET ("cat")
        fun getSimpleCat(): Call<ResponseBody>

        @GET ("cat/says/{sentence}")
        fun getSayingCat(@Path ("sentence") value: String) : Call<ResponseBody>
    }

    interface API_COUR {
        @GET ("cat")
        fun getSimpleCat(): Deferred<ResponseBody>

        @GET ("cat/says/{sentence}")
        fun getSayingCat(@Path ("sentence") value: String) : Deferred<ResponseBody>
    }

    private val api : API by lazy  { provideRetrofit().create(API::class.java) }

    fun getBitmapFrom(url: String, onComplete: (Bitmap?) -> Unit) {


        api.getSimpleCat().enqueue(object : retrofit2.Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                onComplete(null)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response == null || !response.isSuccessful || response.body() == null || response.errorBody() != null) {
                    onComplete(null)
                    return
                }
                val bytes = response.body()!!.bytes()
                onComplete(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }

        })
    }
}