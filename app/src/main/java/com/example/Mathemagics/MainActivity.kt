package com.example.Mathemagics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var btnAdd: Button
    lateinit var btnSub: Button
    lateinit var btnMul: Button
    lateinit var btnDiv: Button
    lateinit var ibtnAdd: ImageButton
    lateinit var ibtnSub: ImageButton
    lateinit var ibtnMul: ImageButton
    lateinit var ibtnDiv: ImageButton
    lateinit var btnMHome: Button
    lateinit var btnEndless: Button
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdd = findViewById(R.id.btnAdd)
        btnSub = findViewById(R.id.btnSubt)
        btnMul = findViewById(R.id.btnMul)
        btnDiv = findViewById(R.id.btnDiv)
        ibtnAdd = findViewById(R.id.ibtnAdd)
        ibtnSub = findViewById(R.id.ibtnSubt)
        ibtnMul = findViewById(R.id.ibtnMul)
        ibtnDiv = findViewById(R.id.ibtnDiv)
        btnMHome = findViewById(R.id.btnMHome)
        btnEndless = findViewById(R.id.btnEndless)
        myRef = FirebaseDatabase.getInstance().reference

        btnMHome.setOnClickListener {
            finish()
        }

        fun startTest(operator: String) {
            val toTest = Intent(this,Test::class.java)
            toTest.putExtra("operator", operator)

            startActivity(toTest)
        }

        btnAdd.setOnClickListener {
            startTest("+")
        }

        ibtnAdd.setOnClickListener {
            startTest("+")
        }

        btnSub.setOnClickListener {
            startTest("-")
        }

        ibtnSub.setOnClickListener {
            startTest("-")
        }

        btnMul.setOnClickListener {
            startTest("x")
        }

        ibtnMul.setOnClickListener {
            startTest("x")
        }

        btnDiv.setOnClickListener {
            startTest("÷")
        }

        ibtnDiv.setOnClickListener {
            startTest("÷")
        }

        btnEndless.setOnClickListener {
            startTest("∞")
        }
    }
}