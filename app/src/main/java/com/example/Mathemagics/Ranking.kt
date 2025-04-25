package com.example.Mathemagics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Ranking : AppCompatActivity() {
    lateinit var txtRaTimeTitle: TextView
    lateinit var txtRaRank: TextView
    lateinit var txtRaURank: TextView
    lateinit var txtRaMRank: TextView
    lateinit var txtRaUserRank: TextView
    lateinit var txtRaUserUser: TextView
    lateinit var txtRaUserTime: TextView
    lateinit var btnRaBack: Button
    lateinit var progressRa: ProgressBar
    lateinit var txtRaMsg: TextView
    lateinit var ibtnRaMode: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        txtRaTimeTitle = findViewById(R.id.txtRaTimeTitle)
        txtRaRank = findViewById(R.id.txtRaRank)
        txtRaURank = findViewById(R.id.txtRaURank)
        txtRaMRank = findViewById(R.id.txtRaMRank)
        txtRaUserRank = findViewById(R.id.txtRaUserRank)
        txtRaUserUser = findViewById(R.id.txtRaUserUser)
        txtRaUserTime = findViewById(R.id.txtRaUserTime)
        btnRaBack = findViewById(R.id.btnRaBack)
        progressRa = findViewById(R.id.progressRa)
        txtRaMsg = findViewById(R.id.txtRaMsg)
        ibtnRaMode = findViewById(R.id.ibtnRaMode)

        val db = FirebaseFirestore.getInstance()
        val refCollection = db.collection("Result")
        val authName = FirebaseAuth.getInstance()
        val currentUsername = authName.currentUser
        val progressRaArray = arrayOf(txtRaMsg, txtRaRank, txtRaMRank, txtRaURank)

        fun displayRanking(choice: Array<String>, count: Int) {
            var mode = ""

            txtRaUserRank.setText("")
            txtRaUserUser.setText("")
            txtRaUserTime.setText("")

            when (choice[count % 5]) {
                "+" -> mode = "+"
                "-" -> mode = "-"
                "x" -> mode = "x"
                "÷" -> mode = "÷"
                "∞" -> mode = "∞"
            }

            when (mode) {
                "+" -> ibtnRaMode.setImageResource(R.drawable.addition)
                "-" -> ibtnRaMode.setImageResource(R.drawable.subtraction)
                "x" -> ibtnRaMode.setImageResource(R.drawable.multiplication)
                "÷" -> ibtnRaMode.setImageResource(R.drawable.division)
                "∞" -> ibtnRaMode.setImageResource(R.drawable.infinity)
            }

            if (mode == "∞") {
                txtRaTimeTitle.setText("Correct")
            } else {
                txtRaTimeTitle.setText("Time")
            }

            if (mode == "∞") {
                currentUsername?.let { userName ->
                    val userRID = userName.uid

                    refCollection.orderBy("Correct", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { ranking ->
                            val userRank = StringBuilder()
                            val userNameRank = StringBuilder()
                            val userCorrect = StringBuilder()
                            var addedID = arrayOf("")
                            var rank = 1
                            for (document in ranking) {
                                val userID = document.getString("User ID")
                                val userMode = document.getString("Mode")

                                if (userID !in addedID && userMode == mode) {
                                    val userName = document.getString("Username")
                                    val correct = document.getLong("Correct")
                                    userCorrect.append("$correct\n")
                                    userRank.append("$rank\n")
                                    userNameRank.append("$userName\n")

                                    if (userID == userRID) {
                                        txtRaUserRank.text = rank.toString()
                                        txtRaUserUser.text = userName.toString()
                                        txtRaUserTime.text = correct.toString()
                                    }

                                    addedID += userID.toString().trim()
                                    rank++
                                }
                            }
                            txtRaRank.text = userRank.toString()
                            txtRaURank.text = userNameRank.toString()
                            txtRaMRank.text = userCorrect.toString()

                            hideProgressBar(progressRaArray, progressRa)
                            txtRaUserRank.visibility = View.VISIBLE
                            txtRaUserUser.visibility = View.VISIBLE
                            txtRaUserTime.visibility = View.VISIBLE
                        }

                        .addOnFailureListener {
                            toast("Error. Please check the Internet connection")
                        }
                }
            }

            else {
                currentUsername?.let { userName ->
                    val userRID = userName.uid

                    refCollection.orderBy("Time spent", Query.Direction.ASCENDING)
                        .get()
                        .addOnSuccessListener { ranking ->
                            val userRank = StringBuilder()
                            val userNameRank = StringBuilder()
                            val userTime = StringBuilder()
                            var addedID = arrayOf("")
                            var rank = 1
                            for (document in ranking) {
                                val userID = document.getString("User ID")
                                val userMode = document.getString("Mode")

                                if (userID !in addedID && userMode == mode) {
                                    val userName = document.getString("Username")
                                    val time = document.getString("Time spent")
                                    userTime.append("$time\n")

                                    userRank.append("$rank\n")
                                    userNameRank.append("$userName\n")

                                    if (userID == userRID) {
                                        txtRaUserRank.text = rank.toString()
                                        txtRaUserUser.text = userName.toString()
                                        txtRaUserTime.text = time.toString()
                                    }

                                    addedID += userID.toString().trim()
                                    rank++
                                }
                            }
                            txtRaRank.text = userRank.toString()
                            txtRaURank.text = userNameRank.toString()
                            txtRaMRank.text = userTime.toString()

                            hideProgressBar(progressRaArray, progressRa)
                            txtRaUserRank.visibility = View.VISIBLE
                            txtRaUserUser.visibility = View.VISIBLE
                            txtRaUserTime.visibility = View.VISIBLE
                        }

                        .addOnFailureListener {
                           toast("Error. Please check the Internet connection")
                        }
                }
            }
        }

        btnRaBack.setOnClickListener {
            finish()
        }

        val choice = arrayOf("+","-","x","÷","∞")
        var count = 0

        displayProgressBar(progressRaArray, progressRa)
        txtRaUserRank.visibility = View.GONE
        txtRaUserUser.visibility = View.GONE
        txtRaUserTime.visibility = View.GONE
        displayRanking(choice, count)

        ibtnRaMode.setOnClickListener{
            count ++

            displayProgressBar(progressRaArray, progressRa)
            txtRaUserRank.visibility = View.GONE
            txtRaUserUser.visibility = View.GONE
            txtRaUserTime.visibility = View.GONE
            displayRanking(choice, count)
        }
    }
}