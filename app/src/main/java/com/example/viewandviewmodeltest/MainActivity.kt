package com.example.viewandviewmodeltest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var startButton: Button
    private lateinit var cancelButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resumeButton: Button
    private lateinit var editTextSeconds: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModel class for all the actual logic of the View (Which is this activity, not the Views like TextView, EditText etc.)
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // Fetch the R.id references
        textView = findViewById(R.id.result)
        startButton = findViewById(R.id.start_button)
        editTextSeconds = findViewById(R.id.editTextNumber)
        cancelButton = findViewById(R.id.cancel_button)
        pauseButton = findViewById(R.id.pause_button)
        resumeButton = findViewById(R.id.resume_button)

        startButton.setOnClickListener{
            clickStartButton(viewModel)
        }

        cancelButton.setOnClickListener{
            clickCancelButton(viewModel)
        }

        pauseButton.setOnClickListener{
            clickPauseButton(viewModel)
        }

        resumeButton.setOnClickListener{
            clickResumeButton(viewModel)
        }

        viewModel.seconds.observe(this, Observer {
            // it refers to the 'seconds' variable we are observing
            textView.text = it.toString()
        })

        viewModel.finished.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Finished!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clickStartButton (vm: MainActivityViewModel) {
        val inputSeconds = editTextSeconds.text.toString()
        if (inputSeconds.trim().isNotEmpty()) {
            val parsedSeconds = parseInt(inputSeconds)
            vm.startTimer(parsedSeconds)
            // Clear the editText
            editTextSeconds.text.clear()

            // Turn off the keyboard after pressing the button
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editTextSeconds.windowToken, 0)

            // Hide the start button
            startButton.visibility = View.GONE

            // And show the cancel button
            cancelButton.visibility = View.VISIBLE
        }
        else {
            Toast.makeText(this, "Please enter seconds before starting timer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clickCancelButton(vm: MainActivityViewModel) {
        // Stop the timer
        vm.stopTimer()

        // Hide the cancel button
        cancelButton.visibility = View.GONE

        // And show the start button
        startButton.visibility = View.VISIBLE

        // Reset the TextView to 0
        textView.text = "0"

    }

    private fun clickPauseButton(vm: MainActivityViewModel) {
        // Pause the timer
        vm.stopTimer()

        // Hide the pause button
        pauseButton.visibility = View.GONE

        // Show the resume button
        resumeButton.visibility = View.VISIBLE
    }

    private fun clickResumeButton(vm: MainActivityViewModel) {
        // Resume the timer
        vm.timerResume()

        // Hide the resume button
        resumeButton.visibility = View.GONE

        // Show the pause button
        pauseButton.visibility = View.VISIBLE
    }

}