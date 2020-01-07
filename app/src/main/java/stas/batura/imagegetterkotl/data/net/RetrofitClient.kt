package stas.batura.imagegetterkotl.data.net

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


object RetrofitClient {

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://cataas.com/").build()
    }

    private interface API {
        @GET ("cat")
        fun getSimpleCat(): Call<ResponseBody>

        @GET ("cat/says/{sentence}")
        fun getSayingCat(@Path ("sentence") value: String) : Call<ResponseBody>
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