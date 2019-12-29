package stas.batura.imagegetterkotl.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import stas.batura.imagegetterkotl.R
import stas.batura.imagegetterkotl.data.net.RetrofitClient
import stas.batura.imagegetterkotl.databinding.MainFragmentBinding

class MainFragment : Fragment() {

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

}
