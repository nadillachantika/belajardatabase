package com.nadillla.belajarfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initFirebase()
        initView()
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun initView() {

        btnSignUp.setOnClickListener {

            val email = edEmailrg.text.toString()
            val password = edPasswdrg.text.toString()
            val pssconf = edPasswdConfirm.text.toString()

            if(email.isEmpty()){
                edEmail.error="Email tidak boleh kosong"
            }
            else if(password.isEmpty()){
                edPasswdrg.error= "Password tidak boleh kosong"
            }
            else if(pssconf.isEmpty()){
                edPasswdConfirm.error="Konfirmasi password tidak boleh kosong"
            }
            else if(password!=pssconf){
                edPasswdConfirm.error="Email tidak sama"
            }else if (password.length<6){
                edPasswd.error = "Password harus lebih dari 6 karakter"
            }
            else {

                actionRegister()
            }

        }
    }

    private fun actionRegister() {
        auth.createUserWithEmailAndPassword(edEmailrg.text.toString(), edPasswdrg.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Tag", "createUserWithEmail:success")
                    val user = auth.currentUser
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Tag", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }
}