package com.example.homework17_timer

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                }
                vibrate()
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

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator;
        } else {
            this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    200,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(200);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
