package com.nadillla.belajarfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private var client : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = Firebase.auth
        initFirebase()
        initView()
        initGmail()

    }

    private fun initGmail() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this,gso)
    }
    private fun signIn() {
        val signInIntent = client?.signInIntent
        startActivityForResult(signInIntent, 1)
    }
//    private fun signIn() {
//        val signInIntent = client?.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }


    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()

        //check login atau belum

        if(auth?.currentUser?.email?.isNotEmpty()?:false){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun initView() {

        btnSignInGoogle.setOnClickListener{
            signIn()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }



        btnLogin.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPasswd.text.toString()
            if (email.isEmpty()) {
                edEmail.error = "Email tidak boleh kosong"
            } else if (password.isEmpty()) {
                edPasswd.error = "Password tidak boleh kosong"
            } else {
                actionLogin()

            }
        }
    }

    private fun actionLogin() {

        auth.signInWithEmailAndPassword(edEmail.text.toString(), edPasswd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                    // ...
                }

                // ...
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                // ...
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this,MainActivity::class.java))
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    // ...
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }

                // ...
            }
    }
}



