package com.iasiris.muniapp.view.ui.components

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberToastController(): ToastController {
    val context = LocalContext.current
    return remember {
        ToastController(context)
    }
}

class ToastController(
    private val context: Context
) {
    fun show(
        @StringRes messageRes: Int,
        duration: Int = Toast.LENGTH_SHORT,
        onDismiss: () -> Unit = {}
    ) {
        Toast.makeText(context, context.getString(messageRes), duration).apply {
            addCallback(object : Toast.Callback() {
                override fun onToastHidden() = onDismiss()
            })
            show()
        }
    }
}