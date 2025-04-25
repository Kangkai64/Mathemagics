package com.example.Mathemagics

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast


fun changeBtnColor(button: Button, color: String){
    button.setBackgroundColor(Color.parseColor(color))
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, msg, duration).show()
}

fun formatTime(time: Int): String {
    val hours = time / 3600
    val minutes = (time / 60) % 60
    val seconds = time % 60

    val timeDisplay = if (hours == 0) {
        String.format("%d:%02d", minutes, seconds)
    }

    else {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    }

    return timeDisplay
}

fun displayProgressBar(progressArray: Array<TextView>, progress: ProgressBar) {
    progressArray[0].setText(arrayOf("Casting Spell \uD83E\uDE84", "Doing Hat Trick \uD83C\uDFA9", "Filling Magic \uD83D\uDD2E").random())

    progress.visibility = View.VISIBLE
    progressArray[0].visibility = View.VISIBLE
    progressArray[1].visibility = View.GONE
    progressArray[2].visibility = View.GONE
    progressArray[3].visibility = View.GONE
}

fun hideProgressBar(progressArray: Array<TextView>, progress: ProgressBar) {
    progress.visibility = View.GONE
    progressArray[0].visibility = View.GONE
    progressArray[1].visibility = View.VISIBLE
    progressArray[2].visibility = View.VISIBLE
    progressArray[3].visibility = View.VISIBLE
}
