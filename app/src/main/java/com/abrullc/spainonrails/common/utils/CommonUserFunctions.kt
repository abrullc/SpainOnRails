package com.abrullc.spainonrails.common.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LifecycleOwner
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.retrofit.services.UsuarioService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommonUserFunctions(val context: Context, val lifecycleOwner: LifecycleOwner, val isUpdate: Boolean) {
    private val commonFunctions = CommonFunctions()

    fun validateFields(username: String, password: String, image: String): Boolean {
        if (username.trim().isEmpty()) {
            commonFunctions.errorAlertDialog(getString(context, R.string.error_required_username), context)
            return false
        }

        if (password.trim().isEmpty()) {
            commonFunctions.errorAlertDialog(getString(context, R.string.error_required_password), context)
            return false
        }

        if (image.trim().isNotEmpty() && !commonFunctions.validateURL(image)) {
            commonFunctions.errorAlertDialog(getString(context, R.string.error_url), context)
            return false
        }

        return true
    }

    fun checkUserFields(username: String, password: String): Boolean {
        if (username.trim().isEmpty()) {
            commonFunctions.errorAlertDialog(getString(context, R.string.error_username_empty), context)
            return false
        }

        if (password.trim().isEmpty()) {
            commonFunctions.errorAlertDialog(getString(context, R.string.error_password_empty), context)
            return false
        }

        return true
    }

    fun checkOptionalField(field: String): String? {
        if (field.trim().isEmpty()) {
            return null
        } else {
            return field
        }
    }

    fun focusChangeListener(layout: TextInputLayout, textInput: TextInputEditText) {
        textInput.onFocusChangeListener = View.OnFocusChangeListener { view, focused ->
            var errorStr: String? = null

            if (!focused) {
                if(textInput.text.toString().trim().isEmpty()) {
                    errorStr = getString(context, R.string.error_required_field)
                }
            }

            layout.error = errorStr
        }
    }

    fun usernameChecker(layout: TextInputLayout, textInput: TextInputEditText) {
        textInput.onFocusChangeListener = View.OnFocusChangeListener { view, focused ->
            var errorStr: String? = null

            val username = textInput.text.toString().trim()

            if (!focused) {
                if (username.isEmpty()) {
                    errorStr = getString(context, R.string.error_required_field)
                    layout.error = errorStr
                } else {
                    commonFunctions.launchLifeCycleScope({
                        val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)

                        val resultUsuario = service.validateNewUsuario(username).body()

                        withContext(Dispatchers.Main) {
                            if (resultUsuario != null) {
                                if (isUpdate && SpainOnRailsApplication.usuario != resultUsuario){
                                    errorStr = getString(context, R.string.error_existent_username)
                                } else if (!isUpdate) {
                                    errorStr = getString(context, R.string.error_existent_username)
                                }
                            }
                            layout.error = errorStr
                        }
                    }, lifecycleOwner, context)
                }
            } else {
                layout.error = null
            }
        }
    }

    fun checkImageField(imageInput: TextInputEditText): Boolean {
        return !(imageInput.text.toString().trim().isNotEmpty() && !commonFunctions.validateURL(imageInput.text.toString()))
    }

    fun focusChangeListenerImage(layout: TextInputLayout, imageInput: TextInputEditText) {
        imageInput.onFocusChangeListener = View.OnFocusChangeListener { view, focused ->
            var errorStr: String? = null

            if (!focused) {
                if(!checkImageField(imageInput)) {
                    errorStr = getString(context, R.string.error_url)
                }
            }

            layout.error = errorStr
        }
    }
}