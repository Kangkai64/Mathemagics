package com.example.Mathemagics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    lateinit var btnLHome: Button
    lateinit var edtLEmail: EditText
    lateinit var edtLPassword: EditText
    lateinit var btnLLogin: Button
    lateinit var btnLSignUp: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLHome = findViewById(R.id.btnLHome)
        edtLEmail = findViewById(R.id.edtLEmail)
        edtLPassword = findViewById(R.id.edtLPassword)
        btnLLogin = findViewById(R.id.btnLLogin)
        btnLSignUp = findViewById(R.id.btnLSignUp)
        auth = Firebase.auth

        btnLHome.setOnClickListener {
            finish()
        }

        btnLLogin.setOnClickListener {
            val email = edtLEmail.text.toString().trim()
            val password = edtLPassword.text.toString().trim()
            var validEmail = false
            var validPassword = false

            if (email.isEmpty()){
                edtLEmail.setError("Email is empty")
            }else{
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtLEmail.setError("Please enter a valid email")
                } else{
                    validEmail = true
                }
            }

            if (password.isEmpty()) {
                edtLPassword.setError("Password is empty")
            }else{
                validPassword = true
            }

            if (validEmail && validPassword){
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { status ->
                        if(status.isSuccessful){
                            val toHome = Intent(this, Home::class.java)
                            toHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(toHome)
                        }
                        else{
                            toast("User does not exist, please sign up")
                        }
                    }
            }

        }

        btnLSignUp.setOnClickListener {
            val toSignUp = Intent(this, SignUp::class.java)
            startActivity(toSignUp)
        }
    }
}