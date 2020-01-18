package stas.batura.imagegetterkotl.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import stas.batura.imagegetterkotl.data.net.RetrofitClient

object Repository {

    var job: CompletableJob? = null

    fun getImage(command: String): LiveData<Bitmap> {
        job = Job()
        return object : LiveData<Bitmap>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        withContext(Main) {
//                            val user = RetrofitClient.getBitmapFrom(command) {
//                                value = it
//                                theJob.complete()
//                            }
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }

}
















