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
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainFragment : Fragment() {

    private final val MY_PERMISSIONS_REQUEST_WRITE_EXT_STOR = 12


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // получаем фабрику для модели
        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)

        // получаем вью модель
        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        // настраиаваем связывание
        val bindings = MainFragmentBinding.inflate(inflater)
        bindings.mainFragmentModel = this.viewModel
        bindings.lifecycleOwner = this

        viewModel.shareButtonCliked.observe(this, Observer {
            if (it) {
                checkPermissions()
            }
        })
//        viewModel.buttonClicked.observe(this, Observer {
//
//        })

        return bindings.root
    }

    /*
        вызывается после создания контекста
     */


    /*
     поделиться фото
     */
    fun shareImage() {
//        checkPermissions()
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, viewModel.getLocalBitmapUri())
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, "iamge"))
    }

    /*
    проверяем разрешения и если все ок - передаем фото
    */
    private fun checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this.context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXT_STOR
            )
        } else {
            // Permission has already been granted
            shareImage()
        }
    }

    /*
    проверяем ответ от менеджара разрешений
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXT_STOR -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    shareImage()
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
