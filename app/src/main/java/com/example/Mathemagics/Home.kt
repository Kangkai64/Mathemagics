package com.example.Mathemagics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {
    lateinit var btnHPlay: Button
    lateinit var btnHRanking: Button
    lateinit var btnHHistory: Button
    lateinit var btnHLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnHPlay = findViewById(R.id.btnHPlay)
        btnHRanking = findViewById(R.id.btnHRanking)
        btnHHistory = findViewById(R.id.btnHHistory)
        btnHLogin = findViewById(R.id.btnHLogin)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if(currentUser !=null){
            btnHLogin.setText("Logout")
            changeBtnColor(btnHLogin, "#F44336")
            changeBtnColor(btnHRanking, "#673AB7")
            changeBtnColor(btnHHistory, "#673AB7")

            btnHLogin.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                recreate()
                toast("Successfully Logged out")
            }
        }else{
            btnHLogin.setText("Login")
            changeBtnColor(btnHLogin, "#109387")
            changeBtnColor(btnHRanking, "#3F2370")
            changeBtnColor(btnHHistory, "#3F2370")

            btnHLogin.setOnClickListener {
                val toLogin = Intent(this, Login::class.java)
                startActivity(toLogin)
            }
        }

        btnHPlay.setOnClickListener {
            val toMainActivity = Intent(this, MainActivity::class.java)
            startActivity(toMainActivity)
        }

        btnHRanking.setOnClickListener {

            if (currentUser != null) {
                val toRanking = Intent(this, Ranking::class.java)
                startActivity(toRanking)
            }else{
                toast("Please login first")
            }
        }

        btnHHistory.setOnClickListener {
            if (currentUser != null) {
                val toUserHistory = Intent(this, UserHistory::class.java)
                startActivity(toUserHistory)
            }else{
                toast("Please login first")
            }
        }
    }
}