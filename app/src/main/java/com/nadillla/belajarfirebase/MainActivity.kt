package com.nadillla.belajarfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        initView()

    }

    private fun initView() {
        var user = auth?.currentUser
        user.let{
            tvHello.text = "Email : " +user?.email
        }


    }

    private fun initFirebase(){
        auth = FirebaseAuth.getInstance()


    }
}