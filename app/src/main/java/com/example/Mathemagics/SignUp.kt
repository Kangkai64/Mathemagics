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
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    lateinit var btnSBack: Button
    lateinit var edtSUsername: EditText
    lateinit var edtSEmail: EditText
    lateinit var edtSPassword: EditText
    lateinit var edtSConfirmPassword: EditText
    lateinit var btnSSignUp: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnSBack = findViewById(R.id.btnSBack)
        edtSUsername = findViewById(R.id.edtSUsername)
        edtSEmail = findViewById(R.id.edtSEmail)
        edtSPassword = findViewById(R.id.edtSPassword)
        edtSConfirmPassword = findViewById(R.id.edtSConfirmPassword)
        btnSSignUp = findViewById(R.id.btnSSignUp)
        auth = Firebase.auth

        btnSBack.setOnClickListener {
            finish()
        }

        btnSSignUp.setOnClickListener {
            val username = edtSUsername.text.toString().trim()
            val email = edtSEmail.text.toString().trim()
            val password = edtSPassword.text.toString().trim()
            val confirmPassword = edtSConfirmPassword.text.toString().trim()
            var validUsername = false
            var validEmail = false
            var validPassword = false
            var validConfirmPassword = false

            if (username.isEmpty()) {
                edtSUsername.setError("Username is empty")
            }else{
                validUsername = true
            }

            if (email.isEmpty()){
                edtSEmail.setError("Email is empty")
            }else{
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtSEmail.setError("Please enter a valid email")
                } else{
                    validEmail = true
                }
            }


            if (password.isEmpty()) {
                edtSPassword.setError("Password is empty")
            }else{
                validPassword = true
            }

            if (password.isEmpty()) {
                edtSConfirmPassword.setError("Confirm Password is empty")
            }else {
                if (password != confirmPassword) {
                    edtSConfirmPassword.setError("Password and Confirm Password is not identical")
                } else {
                    validConfirmPassword = true
                }
            }

            if (validUsername && validEmail && validPassword && validConfirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { status ->
                        if (status.isSuccessful) {
                            val user = auth.currentUser
                            val userID = user?.uid
                            val db = FirebaseFirestore.getInstance()
                            val userData = hashMapOf(
                                "User ID" to userID,
                                "Username" to username,
                                "Email" to email,
                                "Password" to password
                            )

                            userID?.let {
                                db.collection("User Information").document(it)
                                    .set(userData)
                                    .addOnCompleteListener {
                                        val toHome = Intent(this, Home::class.java)
                                        toHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(toHome)
                                    }
                                    .addOnFailureListener {
                                        recreate()
                                    }
                            }

                        } else {
                            toast("User already exists")
                            recreate()
                        }
                    }
            }

        }

    }
}