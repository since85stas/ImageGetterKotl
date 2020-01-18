/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stas.batura.imagegetterkotl

import android.graphics.Bitmap
import android.view.View
import android.widget.*
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import stas.batura.imagegetterkotl.ui.main.EditTextWatcher
import stas.batura.imagegetterkotl.ui.main.ImageApiStatus

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
//@BindingAdapter("imageUrl")
//fun bindImage(imgView: ImageView, imgUrl: String?) {
//    imgUrl?.let {
//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
//        Glide.with(imgView.context)
//                .load(imgUri)
//                .apply(RequestOptions()
//                        .placeholder(R.drawable.loading_animation)
//                        .error(R.drawable.ic_broken_image))
//                .into(imgView)
//    }
//}

/**
 * This binding adapter displays the [ImageApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("imageStatus")
fun bindStatus(statusImageView: ImageView, status: ImageApiStatus?) {
    when (status) {
        ImageApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ImageApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ImageApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

/**
 * Загружает загруженную фотку в imageView
 */
@BindingAdapter("catImage")
fun bindImage(catImageView: ImageView, bitmap: Bitmap?) {
    if (bitmap != null) {
        catImageView.visibility = View.VISIBLE
        catImageView.setImageBitmap(bitmap)
    }
}

/**
 * Управляет видимостью кнопки загрузки, при начале загрузки кнопка скрывается
 * после загрузки опять появляется
 */
@BindingAdapter("getButtonStatus")
fun bindButtonStatus(button:Button, status: Boolean) {
    if (status) {
        button.visibility = View.INVISIBLE
    } else{
        button.visibility = View.VISIBLE
    }
}

/**
 * Управляет видимостью поля с вводом текста, при нажатии на checkBox поле появляется
 * при отжатии пропадает
 */
@BindingAdapter("saysTextBoxState")
fun bindSaysEditTextState(editText: EditText, status: Boolean) {
    if (status) {
        editText.visibility = View.VISIBLE
    } else{
        editText.visibility = View.INVISIBLE
    }
}

/*

 */
@BindingAdapter("shareButtonStatus")
fun bindShareButonStatus(button: ImageButton,status: Boolean) {
    if (status) {
        button.visibility = View.VISIBLE
    } else{
        button.visibility = View.INVISIBLE
    }
}

/*
    добавляем слушателя дял получения текста из EditText
 */
@BindingAdapter("addEditTextWather")
fun bindEditText(editText: EditText, editTextWatcher: EditTextWatcher) {
    editText.addTextChangedListener(editTextWatcher)
}