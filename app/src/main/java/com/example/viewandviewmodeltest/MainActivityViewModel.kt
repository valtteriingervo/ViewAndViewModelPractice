package com.example.viewandviewmodeltest

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    private lateinit var timer: CountDownTimer

    // We don't want other classes to access the MutableLiveData variables
    // which we actively change in this ViewModel class
    private val _seconds = MutableLiveData<Int>()
    // so we make a non-mutable LiveData API for other classes to access
    val seconds: LiveData<Int> = _seconds

    private var _finished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean> = _finished


    fun startTimer(seconds: Int) {
        val millis = (seconds * 1000).toLong()
        timer = object : CountDownTimer(millis, 1000) {
            override fun onFinish() {
                _finished.value = true
            }

            override fun onTick(milliSecondsTillFinish: Long) {
                // Convert milliseconds into seconds
                val timeLeftSeconds = milliSecondsTillFinish / 1000
                _seconds.value = timeLeftSeconds.toInt()

            }
        }.start()
    }

    fun stopTimer() {
        timer.cancel()
    }

    fun timerResume() {
        val secondsLeft = _seconds.value ?: 0
        startTimer(secondsLeft)
    }

}