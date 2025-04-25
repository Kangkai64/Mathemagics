package com.example.Mathemagics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserHistory : AppCompatActivity() {
    lateinit var txtHTimelbl: TextView
    lateinit var ibtnHMode: ImageButton
    lateinit var txtLHistory: TextView
    lateinit var txtUHistory: TextView
    lateinit var txtMHistory: TextView
    lateinit var btnHBack: Button
    lateinit var progressH: ProgressBar
    lateinit var txtHMsg: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        txtHTimelbl = findViewById(R.id.txtHTimelbl)
        ibtnHMode = findViewById(R.id.ibtnHMode)
        txtLHistory = findViewById(R.id.txtLHistory)
        txtUHistory = findViewById(R.id.txtUHistory)
        txtMHistory = findViewById(R.id.txtMHistory)
        btnHBack = findViewById(R.id.btnHBack)
        progressH = findViewById(R.id.progressH)
        txtHMsg = findViewById(R.id.txtHMsg)

        val progressHArray = arrayOf(txtHMsg, txtLHistory, txtMHistory, txtUHistory)
        var count = 0
        val db = FirebaseFirestore.getInstance()
        val authName = FirebaseAuth.getInstance()
        val currentUsername = authName.currentUser

        fun displayHistory(count: Int) {
            currentUsername?.let { userName ->
                val userRID = userName.uid

                db.collection("Result")
                    .orderBy("Date and Time", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { record ->
                        val userDateTime = StringBuilder()
                        val userMode = StringBuilder()
                        val userTime = StringBuilder()
                        val dtFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                        dtFormat.timeZone = TimeZone.getTimeZone("GMT+8")
                        for (document in record){
                            val timeStamp = document.getTimestamp("Date and Time")
                            val userID = document.getString("User ID")
                            if (timeStamp !=null && userID == userRID){
                                val mode = document.getString("Mode")
                                val dateTime = timeStamp.toDate()
                                val formattedDT = dtFormat.format(dateTime)
                                hideProgressBar(progressHArray, progressH)

                                if (mode == "∞" && (count % 2) == 1) {
                                    val correct = document.getLong("Correct")
                                    userTime.append("$correct\n")
                                    userDateTime.append("$formattedDT\n")
                                    userMode.append("$mode\n")
                                }

                                else if (mode != "∞" && (count % 2) == 0){
                                    val timeSpent = document.getString("Time spent")
                                    userTime.append("$timeSpent\n")
                                    userDateTime.append("$formattedDT\n")
                                    userMode.append("$mode\n")
                                }
                            }
                        }
                        txtLHistory.text = userDateTime.toString()
                        txtUHistory.text = userMode.toString()
                        txtMHistory.text = userTime.toString()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error. Please check the Internet connection", Toast.LENGTH_SHORT).show()
                       hideProgressBar(progressHArray, progressH)
                    }
            }
        }

        btnHBack.setOnClickListener {
            finish()
        }

        displayHistory(count)
        displayProgressBar(progressHArray, progressH)

        ibtnHMode.setOnClickListener {
            count ++

            if (count % 2 == 0) {
                txtHTimelbl.setText("Time")
                ibtnHMode.setImageResource(R.drawable.icon)
            }

            else {
                txtHTimelbl.setText("Correct")
                ibtnHMode.setImageResource(R.drawable.infinity)
            }

            displayHistory(count)
            displayProgressBar(progressHArray, progressH)
        }
    }
}
