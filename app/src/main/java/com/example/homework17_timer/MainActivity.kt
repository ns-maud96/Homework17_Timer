package com.example.homework17_timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        btnStart.setOnClickListener {
            startTimer(inputSeconds.text)
        }
        btnPause.setOnClickListener {
            pauseTimer()
        }
        btnReset.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun startTimer(secondsFromEditText: Editable) {
        var secondsToInt: Int = secondsFromEditText.toString().toInt()
        job = scope.launch {
            withContext(Dispatchers.Main) {
                while (secondsToInt > 0) {
                    delay(1000)
                    secondsToInt -= 1
                    inputSeconds.text =
                        Editable.Factory.getInstance().newEditable("$secondsToInt")
//                    vibrate()
                }
            }
        }
    }

    private fun pauseTimer() {
        job?.cancel()
    }

    private fun resetTimer() {
        job?.cancel()
        inputSeconds.text = null
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setMessage("Are you sure you want to reset timer?")
            setPositiveButton("Yes") { dialog, id -> resetTimer() }
            setNegativeButton("No") { dialog, id -> }
        }
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }
/*
private fun vibrate() {
val vibration = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as Vibrator
vibration.vibrate(VibrationEffect.createOneShot(2, 2))
}
*/
}
