package com.abrullc.spainonrails.userModule

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.abrullc.spainonrails.R
import com.abrullc.spainonrails.SpainOnRailsApplication
import com.abrullc.spainonrails.common.utils.CommonFunctions
import com.abrullc.spainonrails.common.utils.CommonUserFunctions
import com.abrullc.spainonrails.databinding.ActivityUserBinding
import com.abrullc.spainonrails.retrofit.entities.Usuario
import com.abrullc.spainonrails.retrofit.services.UsuarioService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityUserBinding
    private lateinit var commonFunctions: CommonFunctions
    private lateinit var commonUserFunctions: CommonUserFunctions
    private lateinit var currentUser: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        commonFunctions = CommonFunctions()
        commonUserFunctions = CommonUserFunctions(this, this, true)

        setupUser()

        with(mBinding) {
            imgGoBack.setOnClickListener { finish() }

            imgUserEdit.setOnClickListener { editUser() }

            imgLogout.setOnClickListener {
                commonFunctions.logout(this@UserActivity) {
                    finish()
                }
            }

            btnDeleteUser.setOnClickListener { deleteUsuario() }
        }
    }

    private fun setupUser() {
        currentUser = SpainOnRailsApplication.usuario

        with(mBinding) {
            Glide.with(this@UserActivity)
                .load(currentUser.imagen)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(imgProfile)

            tvUsername.text = currentUser.username

            val email = currentUser.email
            if (!email.isNullOrEmpty()) {
                tvEmail.text = email
            } else {
                tvEmail.text = getString(R.string.email_not_specified)
            }

            cardUserTickets.setOnClickListener {
                commonFunctions.infoAlertDialog(getString(R.string.future_development), this@UserActivity)
            }

            cardUserTravelPlans.setOnClickListener {
                commonFunctions.infoAlertDialog(getString(R.string.future_development), this@UserActivity)
            }
        }
    }

    private fun editUser() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_edit_user_title)
            .setView(dialogView)
            .setPositiveButton(R.string.dialog_edit_user, null)
            .setNegativeButton(R.string.dialog_cancel, null)
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.findViewById<TextInputEditText>(R.id.etUsername)!!.setText(currentUser.username)
        dialog.findViewById<TextInputEditText>(R.id.etPassword)!!.setText(currentUser.password)

        val currentEmail = currentUser.email
        if (!currentEmail.isNullOrEmpty()) {
            dialogView.findViewById<TextInputEditText>(R.id.etEmail)!!.setText(currentEmail)
        }

        val currentImagen = currentUser.imagen
        if (!currentImagen.isNullOrEmpty()) {
            dialogView.findViewById<TextInputEditText>(R.id.etImage)!!.setText(currentImagen)
        }

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val username = dialogView.findViewById<TextInputEditText>(R.id.etUsername).text.toString().trim()
            val password = dialogView.findViewById<TextInputEditText>(R.id.etPassword).text.toString().trim()
            val email: String = dialogView.findViewById<TextInputEditText>(R.id.etEmail).text.toString().trim()
            val image = dialogView.findViewById<TextInputEditText>(R.id.etImage).text.toString().trim()

            if (commonUserFunctions.validateFields(username, password, image)) {
                commonFunctions.launchLifeCycleScope({
                    val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)
                    val resultUsuario = service.validateNewUsuario(username).body()

                    withContext(Dispatchers.Main) {
                        if (resultUsuario == null || resultUsuario == currentUser) {
                            val updatedUsuario = Usuario(
                                id = currentUser.id,
                                username = username,
                                password = password,
                                email = commonUserFunctions.checkOptionalField(email),
                                imagen = commonUserFunctions.checkOptionalField(image)
                            )

                            updateUser(updatedUsuario)

                            dialog.dismiss()
                        } else {
                            commonFunctions.errorAlertDialog(getString(R.string.error_existent_username), this@UserActivity)
                        }
                    }
                }, this, this)
            }
        }

        commonUserFunctions.usernameChecker(dialogView.findViewById(R.id.tilUsername), dialogView.findViewById(R.id.etUsername))
        commonUserFunctions.focusChangeListener(dialogView.findViewById(R.id.tilPassword), dialogView.findViewById(R.id.etPassword))
        commonUserFunctions.focusChangeListenerImage(dialogView.findViewById(R.id.tilImage), dialogView.findViewById(R.id.etImage))
    }

    private fun updateUser(updatedUsuario: Usuario) {
        commonFunctions.launchLifeCycleScope({
            val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)

            val resultUpdatedUsuario = service.updateUsuario(updatedUsuario.id, updatedUsuario).body()!!

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@UserActivity,
                    "Usuario ${resultUpdatedUsuario.username} actualizado!",
                    Toast.LENGTH_SHORT
                ).show()

                SpainOnRailsApplication.usuario = resultUpdatedUsuario
                SpainOnRailsApplication.isUsuarioUpdated = true

                setupUser()
            }
        }, this, this)
    }

    private fun deleteUsuario() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_user, null)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delte_user_title)
            .setMessage(R.string.delete_user_info)
            .setView(dialogView)
            .setPositiveButton(R.string.dialog_edit_user, null)
            .setNegativeButton(R.string.dialog_cancel, null)
            .setCancelable(false)
            .create()

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            val password = dialogView.findViewById<TextInputEditText>(R.id.etPassword).text.toString().trim()

            if (password == currentUser.password) {
                commonFunctions.launchLifeCycleScope({
                    val service = SpainOnRailsApplication.retrofit.create(UsuarioService::class.java)
                    val resultDeletedUsuario = service.deleteUsuario(currentUser.id).body()!!

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@UserActivity,
                            "Usuario ${resultDeletedUsuario.username} eliminado",
                            Toast.LENGTH_SHORT
                        ).show()

                        SpainOnRailsApplication.isLogoutRequired = true

                        finish()
                    }
                }, this, this)
            } else {
                commonFunctions.errorAlertDialog(getString(R.string.invalid_password), this@UserActivity)
            }
        }

        commonUserFunctions.focusChangeListener(dialogView.findViewById(R.id.tilPassword), dialogView.findViewById(R.id.etPassword))
    }
}