package com.example.fitnesstracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TrackWeights : AppCompatActivity() {
    private lateinit var exerciseNameEditText: EditText
    private lateinit var setsEditText: EditText
    private lateinit var trackExerciseButton: Button
    private lateinit var totalSetsTextView: TextView
    private lateinit var exerciseTable: TableLayout

    private var totalSets: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weight_track)

        exerciseNameEditText = findViewById(R.id.exerciseNameEditText)
        setsEditText = findViewById(R.id.setsEditText)
        trackExerciseButton = findViewById(R.id.trackExerciseButton)
        totalSetsTextView = findViewById(R.id.totalSetsTextView)
        exerciseTable = findViewById(R.id.exerciseTable)

        trackExerciseButton.setOnClickListener {
            trackExercise()
        }
    }

    private fun trackExercise() {
        val exerciseName = exerciseNameEditText.text.toString()
        val sets = setsEditText.text.toString().toIntOrNull()

        if (exerciseName.isNotEmpty() && sets != null && sets > 0) {
            addExerciseToTable(exerciseName, sets)
            updateTotalSets()
            clearInputFields()
        }
    }

    private fun addExerciseToTable(exerciseName: String, sets: Int) {
        val tableRow = TableRow(this)

        val exerciseNameTextView = TextView(this)
        exerciseNameTextView.text = exerciseName
        tableRow.addView(exerciseNameTextView)

        val setsTextView = TextView(this)
        setsTextView.text = sets.toString()
        tableRow.addView(setsTextView)

        exerciseTable.addView(tableRow)
    }

    private fun updateTotalSets() {
        totalSets++
        totalSetsTextView.text = "Total Sets: $totalSets"
    }

    private fun clearInputFields() {
        exerciseNameEditText.text.clear()
        setsEditText.text.clear()
    }
}