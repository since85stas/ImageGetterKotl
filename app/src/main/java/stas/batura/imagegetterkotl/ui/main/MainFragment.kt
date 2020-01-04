package stas.batura.imagegetterkotl.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import stas.batura.imagegetterkotl.data.net.RetrofitClient
import stas.batura.imagegetterkotl.databinding.MainFragmentBinding
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainFragment : Fragment() {

    private final val MY_PERMISSIONS_REQUEST_READ_EXT_STOR = 11
    private final val MY_PERMISSIONS_REQUEST_WRITE_EXT_STOR = 12


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // получаем вью модель
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // настраиаваем связывание
        val bindings = MainFragmentBinding.inflate(inflater)
        bindings.mainFragmentModel = this.viewModel
        bindings.lifecycleOwner = this

        viewModel.shareButtonCliked.observe(this, Observer{
            if (it) {
                shareImage()
            }
        })

        return bindings.root
    }

    /*
        вызывается после создания контекста
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        RetrofitClient.getBitmapFrom("cat") {
            print(it.toString())
            cat_image_view.setImageBitmap(it)
        }
    }

    fun shareImage() {
        checkPermissions()
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(viewModel.imagetBit.value!!))
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, "iamge"))
    }

    fun getLocalBitmapUri(bitmap: Bitmap): Uri? {
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ), "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.getParentFile().mkdirs()
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            bmpUri = FileProvider.getUriForFile(context!!, requireActivity().packageName +".fileprovider", file);
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }

    private fun checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this.context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXT_STOR)

                // MY_PERMISSIONS_REQUEST_READ_EXT_STOR is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXT_STOR -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
