package com.example.Mathemagics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import kotlin.math.roundToInt

class Result : AppCompatActivity() {
    lateinit var btnReHome: Button
    lateinit var txtReName: TextView
    lateinit var txtReCorrect: TextView
    lateinit var txtReTime: TextView
    lateinit var txtReMarkslbl: TextView
    lateinit var txtReMarks: TextView
    lateinit var txtReGrade: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        btnReHome = findViewById(R.id.btnReHome)
        txtReName = findViewById(R.id.txtReName)
        txtReCorrect = findViewById(R.id.txtReCorrect)
        txtReTime = findViewById(R.id.txtReTime)
        txtReMarkslbl = findViewById(R.id.txtReMarkslbl)
        txtReMarks = findViewById(R.id.txtReMarks)
        txtReGrade = findViewById(R.id.txtReGrade)

        btnReHome.setOnClickListener {
            val toMain = Intent(this, MainActivity::class.java)
            toMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(toMain)
        }

        val db = FirebaseFirestore.getInstance()
        val authName = FirebaseAuth.getInstance()
        val currentUsername = authName.currentUser

        currentUsername?.let { userName ->
            val userRID = userName.uid

            db.collection("User Information").document(userRID)
                .get()
                .addOnSuccessListener { nameResult ->
                    val username = nameResult.getString("Username")
                    txtReName.setText(username)
                }
        }

        val correct = intent.getStringExtra("correct").toString().toInt()
        val question_amount = intent.getStringExtra("question_amount").toString().toInt()
        var marks = (correct.toFloat() / question_amount.toFloat() * 100).roundToInt()
        val time = intent.getStringExtra("time").toString().toInt()
        val endless = intent.getStringExtra("endless").toString()
        val grade: String
        var mode = intent.getStringExtra("mode").toString()

        val hours = time / 3600
        val minutes = (time / 60) % 60
        val seconds = time % 60

        val timeDisplay = if (hours == 0) {
            String.format("%d:%02d", minutes, seconds)
        }

        else {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        }

        if (endless == "true") {
            grade = when {
                correct >= 25 -> "A+"
                correct >= 20 -> "A"
                correct >= 15 -> "B"
                correct >= 10 -> "C"
                correct >= 5 -> "D"
                correct > 0 -> "F"
                correct == 0 -> "F-"
                else -> "h"
            }
            txtReCorrect.setText("$correct")
            txtReMarkslbl.setText("")
            txtReMarks.setText("")

            marks = 0
            mode = "∞"
        }

        else {
            grade = when {
                marks == 100 -> "A+"
                marks >= 80 -> "A"
                marks >= 60 -> "B"
                marks >= 40 -> "C"
                marks >= 20 -> "D"
                marks > 0 -> "F"
                marks == 0 -> "F-"
                else -> "h"
            }
            txtReCorrect.setText("$correct/$question_amount")
            txtReMarks.setText("$marks%")
        }

        txtReTime.setText(timeDisplay)
        txtReGrade.setText(grade)

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val userID = user.uid
            val userEmail = user.email

            database.collection("User Information").document(userID)
                .get()
                .addOnSuccessListener { userData ->
                    val username = userData.getString("Username")
                    val calendar = Calendar.getInstance()
                    val dateTime = calendar.time
                    val userGrade = grade
                    val userMode = mode

                    val result = hashMapOf(
                        "User ID" to userID,
                        "Username" to username,
                        "User Email" to userEmail,
                        "Marks" to marks,
                        "Mode" to userMode,
                        "Correct" to correct,
                        "Grade" to userGrade,
                        "Time spent" to timeDisplay,
                        "Date and Time" to dateTime
                    )

                    db.collection("Result")
                        .add(result)
                }
        }
    }
}