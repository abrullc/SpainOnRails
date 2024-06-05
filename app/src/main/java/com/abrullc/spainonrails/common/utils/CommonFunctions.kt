package com.abrullc.spainonrails.common.utils

import android.content.Context
import android.webkit.URLUtil
import com.abrullc.spainonrails.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CommonFunctions {
    fun validateURL(url: String): Boolean {
        return !(!URLUtil.isValidUrl(url.trim()) && url.trim().isNotEmpty())
    }

    fun errorAlertDialog(texto: String, context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_error_title)
            .setMessage(texto)
            .setPositiveButton(R.string.dialog_confirm, null)
            .setCancelable(false)
            .show()
    }
}