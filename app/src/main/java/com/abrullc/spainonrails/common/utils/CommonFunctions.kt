package com.abrullc.spainonrails.common.utils

import android.content.Context
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.abrullc.spainonrails.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CommonFunctions {
    fun validateURL(url: String): Boolean {
        return URLUtil.isValidUrl(url.trim())
    }

    fun errorAlertDialog(texto: String, context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_error_title)
            .setMessage(texto)
            .setPositiveButton(R.string.dialog_confirm, null)
            .setCancelable(false)
            .show()
    }

    fun launchLifeCycleScope(
        code: suspend () -> Unit,
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {
        lifecycleOwner.lifecycleScope.launch {
            try {
                code()
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when(it.code()) {
                        400 -> {
                            Toast.makeText(context, R.string.main_error_server, Toast.LENGTH_SHORT).show()
                        }
                        else ->
                            Toast.makeText(context, R.string.unknown_error_server, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun launchFragmentfromFragment(framgentManager: FragmentManager, newFragment: Fragment) {
        framgentManager
            .beginTransaction()
            .replace(R.id.frame_container, newFragment)
            .addToBackStack(null)
            .commit()
    }
}