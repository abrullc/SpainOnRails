package com.abrullc.spainonrails.loginModule

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.common.utils.CommonUserFunctions
import com.abrullc.spainonrails.databinding.ActivityLoginBinding
import com.abrullc.spainonrails.mainModule.MainActivity
import com.abrullc.spainonrails.retrofit.entities.Usuario
import com.abrullc.spainonrails.retrofit.services.UsuarioService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var commonUserFunctions: CommonUserFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        loadImgPortada("http://spainonrails.navelsystems.es/images/stations/valenciaTermino.jpeg")

        commonFunctions = CommonFunctions()
        commonUserFunctions = CommonUserFunctions(this, this, false)

        val preferences = getPreferences(MODE_PRIVATE)

        val rememberUsusario = preferences.getBoolean(getString(R.string.sp_remember_user), false)
        val spIdUsuario = preferences.getInt(getString(R.string.sp_id_user), 0)

        if (rememberUsusario) {
            getUsuario(spIdUsuario)
        }

        mBinding.btnLogin.setOnClickListener {
            val username = mBinding.etUsername.text.toString().trim()
            val password = mBinding.etPassword.text.toString().trim()

            commonUserFunctions.checkUserFields(username, password)

            checkUsuario(username, password)
        }

        mBinding.newUser.setOnClickListener {
            createUser()
        }
    }

    override fun onResume() {
        super.onResume()

        if (SpainOnRailsApplication.isLogoutRequired) {
            val preferences = getPreferences(MODE_PRIVATE)

            with(mBinding) {
                etUsername.setText("")
                etPassword.setText("")
                cbRememberPass.isChecked = false
            }

            with(preferences.edit()) {
                putInt(getString(R.string.sp_id_user), 0)
                putBoolean(getString(R.string.sp_remember_user), false)
                    .apply()
            }

            SpainOnRailsApplication.isLogoutRequired = false
        } else if (SpainOnRailsApplication.isUsuarioUpdated) {
            with(mBinding) {
                etUsername.setText(SpainOnRailsApplication.usuario.username)
                etPassword.setText(SpainOnRailsApplication.usuario.password)
            }

            SpainOnRailsApplication.isUsuarioUpdated = false
        }
    }

    private fun checkUsuario(loginUsername: String, loginPassword: String) {
        commonFunctions.launchLifeCycleScope({
            val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)

            val resultUsuario = service.validateUsuario(usuario = Usuario(
                id = 0,
                username = loginUsername,
                password = loginPassword,
                email = null,
                imagen = null)).body()

            if (resultUsuario != null) {
                SpainOnRailsApplication.usuario = resultUsuario
                spLogin()
                Toast.makeText(this@LoginActivity, "Bienvenido ${resultUsuario.username}!", Toast.LENGTH_LONG).show()
                goToMain()
            } else {
                commonFunctions.errorAlertDialog(getString(R.string.login_error), this@LoginActivity)
            }
        }, this, this)
    }

    private fun getUsuario(idUsuario: Int) {
        commonFunctions.launchLifeCycleScope({
            val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)

            val result = service.getUsuario(idUsuario)
            val usuario = result.body()!!

            withContext(Dispatchers.Main) {
                with(mBinding) {
                    etUsername.setText(usuario.username)
                    etPassword.setText(usuario.password)
                    cbRememberPass.isChecked = true
                }
            }
        }, this, this)
    }

    private fun spLogin() {
        val preferences = getPreferences(MODE_PRIVATE)

        if (mBinding.cbRememberPass.isChecked) {
            with(preferences.edit()) {
                putInt(getString(R.string.sp_id_user), SpainOnRailsApplication.usuario.id)
                putBoolean(getString(R.string.sp_remember_user), true)
                    .apply()
            }
        } else {
            with(preferences.edit()) {
                putBoolean(getString(R.string.sp_remember_user), false)
                    .apply()
            }
        }
    }

    private fun createUser() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_new_user_title)
            .setView(dialogView)
            .setPositiveButton(R.string.dialog_register_user, null)
            .setNegativeButton(R.string.dialog_cancel, null)
            .setCancelable(false)
            .create()

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val username = dialogView.findViewById<TextInputEditText>(R.id.etUsername).text.toString().trim()
            val password = dialogView.findViewById<TextInputEditText>(R.id.etPassword).text.toString().trim()
            val email = dialogView.findViewById<TextInputEditText>(R.id.etEmail).text.toString().trim()
            val image = dialogView.findViewById<TextInputEditText>(R.id.etImage).text.toString().trim()

            if (commonUserFunctions.validateFields(username, password, image)) {
                commonFunctions.launchLifeCycleScope({
                    val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)
                    val resultUsuario = service.validateNewUsuario(username).body()

                    withContext(Dispatchers.Main) {
                        if (resultUsuario == null) {
                            val usuario = Usuario(
                                id = 0,
                                username = username,
                                password = password,
                                email = commonUserFunctions.checkOptionalField(email),
                                imagen = commonUserFunctions.checkOptionalField(image)
                            )

                            registerUser(usuario)

                            dialog.dismiss()
                        } else {
                            commonFunctions.errorAlertDialog(getString(R.string.error_existent_username), this@LoginActivity)
                        }
                    }
                }, this, this)
            }
        }

        commonUserFunctions.usernameChecker(dialogView.findViewById(R.id.tilUsername), dialogView.findViewById(R.id.etUsername))
        commonUserFunctions.focusChangeListener(dialogView.findViewById(R.id.tilPassword), dialogView.findViewById(R.id.etPassword))
        commonUserFunctions.focusChangeListenerImage(dialogView.findViewById(R.id.tilImage), dialogView.findViewById(R.id.etImage))
    }

    private fun registerUser(usuario: Usuario) {
        commonFunctions.launchLifeCycleScope({
            val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)

            val resultUsuario = service.postUsuario(usuario).body()!!

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@LoginActivity,
                    "Nuevo usuario ${resultUsuario.username} registrado!",
                    Toast.LENGTH_SHORT
                ).show()

                with(mBinding) {
                    etUsername.setText(usuario.username)
                    etPassword.setText(usuario.password)
                }
            }
        }, this, this)
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun loadImgPortada(url: String) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPortada)
    }
}