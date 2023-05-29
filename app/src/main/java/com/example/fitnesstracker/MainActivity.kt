package com.example.fitnesstracker

import android.content.*
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var stepCountReceiver: BroadcastReceiver? = null
    var currentSteps: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Register the step count broadcast receiver
        val userBtn: Button = findViewById(R.id.userBtn)
        val testBtn: Button = findViewById(R.id.testBtn)
        val stepTV: TextView = findViewById(R.id.stepTV)
        val foodBtn: Button = findViewById(R.id.foodBtn)
        foodBtn.setOnClickListener {
            val intent = Intent(this, TrackFood::class.java)
            startActivity(intent)
        }

        val weightButton: Button = findViewById(R.id.weightBtn)
        weightButton.setOnClickListener {
            val intent = Intent(this, TrackWeights::class.java)
            startActivity(intent)
        }

        userBtn.setOnClickListener{
            var dialog = FitnessDialogFragment()
            dialog.show(supportFragmentManager, "SignUp / Login")
        }

        stepCountReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "STEP_COUNT_UPDATE") {
                    val steps = intent.getFloatExtra("steps", 0f)
                    currentSteps = steps // Update the current step count
                    stepTV.text = "Steps: $currentSteps"
                }
            }
        }

        testBtn.setOnClickListener {
            currentSteps += 1000 // Increment the step count by 1000
            val intent = Intent("STEP_COUNT_UPDATE")
            intent.putExtra("steps", currentSteps)
            sendBroadcast(intent)
        }
        registerReceiver(stepCountReceiver, IntentFilter("STEP_COUNT_UPDATE"))

        // Start the step count service
        val serviceIntent = Intent(this, StepCountService::class.java)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the step count broadcast receiver
        unregisterReceiver(stepCountReceiver)

        // Stop the step count service
        val serviceIntent = Intent(this, StepCountService::class.java)
        stopService(serviceIntent)
    }
}