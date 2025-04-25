package com.example.Mathemagics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.isDigitsOnly

class Test : AppCompatActivity() {
    lateinit var txtEQsNum: TextView
    lateinit var txtETime: TextView
    lateinit var btnEBack: Button
    lateinit var txtEQs: TextView
    lateinit var edtEAns: EditText
    lateinit var txtEResult: TextView
    lateinit var btnENext: Button

    var time = 0

    val handler = Handler(Looper.getMainLooper())
    val timer = object : Runnable {
        override fun run() {
            time++
            txtETime = findViewById(R.id.txtETime)
            txtETime.setText(formatTime(time))

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        txtEQsNum = findViewById(R.id.txtEQsNum)
        btnEBack = findViewById(R.id.btnEBack)
        txtEQs = findViewById(R.id.txtEQs)
        edtEAns = findViewById(R.id.edtEAns)
        txtEResult = findViewById(R.id.txtEResult)
        btnENext = findViewById(R.id.btnENext)

        btnEBack.setOnClickListener {
            finish()
        }

        fun generateQuestion(operator: String): Array<Int> {
            var num1 = -1
            var num2 = -1

            if (operator == "+" || operator == "-") {
                num1 = (10..50).random()
                num2 = (10..50).random()

                if (operator == "-") {
                    if (num1 < num2) {
                        val x = num1
                        num1 = num2
                        num2 = x
                    }
                }
            }

            else if (operator == "x") {
                num1 = (2..12).random()
                num2 = (2..12).random()
            }

            else if (operator == "÷") {
                val ans = (2..12).random()
                num2 = (2..12).random()
                num1 = num2 * ans
            }

            else {
                // ERROR
            }
            
            val equation = "$num1 $operator $num2 = ?"

            txtEQs.setText(equation)
            return arrayOf(num1, num2)
        }

        fun calculateAnswer(numbers: Array<Int>, operator: String): Int {
            val num1 = numbers[0]
            val num2 = numbers[1]
            val answer = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "x" -> num1 * num2
                "÷" -> num1 / num2
                else -> -1
            }
            return answer
        }

        val questionAmount: Int

        var operator = intent.getStringExtra("operator").toString()

        val endless = if (operator == "∞") {
            operator = listOf("+", "-", "x", "÷").random()
            questionAmount = -1
            true
        }

        else {
            questionAmount = 10
            false
        }

        var question = 1
        var correct = 0
        var numbers = generateQuestion(operator,)
        var answer = calculateAnswer(numbers, operator)

        handler.postDelayed(timer, 1000)

        btnENext.setOnClickListener {
            if (btnENext.text == "Submit") {
                if (edtEAns.text.toString().trim().isDigitsOnly() && edtEAns.text.isNotEmpty()) {
                    val equation = numbers[0].toString() + " $operator " + numbers[1].toString() + " = $answer"
                    txtEQs.setText(equation)
                    edtEAns.setEnabled(false)
                    changeBtnColor(btnENext, "#1CC322")

                    if (edtEAns.text.toString().trim().toInt() == answer) {
                        correct += 1
                        txtEResult.setText("Correct!")
                    }

                    else {
                        if (endless) {
                            handler.removeCallbacks(timer)
                            txtEResult.setText("Wrong!")
                            btnENext.setText("Finish")
                            changeBtnColor(btnENext, "#E91E63")
                        }

                        else {
                            time += 15
                            txtETime.setText(formatTime(time))
                            txtEResult.setText("Wrong! (+15s)")
                        }
                    }

                    if (question == questionAmount) {
                        handler.removeCallbacks(timer)
                        btnENext.setText("Finish")
                        changeBtnColor(btnENext, "#E91E63")
                    }

                    else if (btnENext.text != "Finish") {
                        btnENext.setText("Next")
                        changeBtnColor(btnENext, "#FF9800")
                    }
                }

                else {
                    txtEResult.setText("Enter a Number!")
                }
            }

            else if (btnENext.text == "Next") {
                btnENext.setText("Submit")
                changeBtnColor(btnENext, "#128316")
                txtEResult.setText("")
                edtEAns.setText("")
                edtEAns.setEnabled(true)

                if (endless) {
                    operator = listOf("+", "-", "x", "÷").random()
                }
                numbers = generateQuestion(operator)
                answer = calculateAnswer(numbers, operator)
                question += 1
                txtEQsNum.setText("Qs $question")
            }

            else if (btnENext.text == "Finish") {
                val toResult = Intent(this,Result::class.java)
                toResult.putExtra("correct", "$correct")
                toResult.putExtra("question_amount", "$questionAmount")
                toResult.putExtra("time", "$time")
                toResult.putExtra("mode", operator)

                if (endless) {
                    toResult.putExtra("endless", "true")
                }

                else {
                    toResult.putExtra("endless", "false")
                }

                startActivity(toResult)
            }
        }
    }
}