package com.example.fitnesstracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TrackFood : AppCompatActivity() {

    private lateinit var foodNameEditText: EditText
    private lateinit var caloriesEditText: EditText
    private lateinit var addFoodButton: Button
    private lateinit var totalCaloriesTextView: TextView
    private lateinit var foodTable: TableLayout

    private var totalCalories: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_track)

        // Initialize views
        foodNameEditText = findViewById(R.id.foodNameEditText)
        caloriesEditText = findViewById(R.id.caloriesEditText)
        addFoodButton = findViewById(R.id.addFoodButton)
        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView)
        foodTable = findViewById(R.id.foodTable)

        addFoodButton.setOnClickListener {
            addFoodEntry()
        }
    }

    private fun addFoodEntry() {
        val foodName = foodNameEditText.text.toString()
        val calories = caloriesEditText.text.toString().toIntOrNull()

        if (foodName.isNotEmpty() && calories != null) {
            val row = TableRow(this)

            val foodNameView = TextView(this)
            foodNameView.text = foodName
            row.addView(foodNameView)

            val caloriesView = TextView(this)
            caloriesView.text = calories.toString()
            row.addView(caloriesView)

            foodTable.addView(row)

            totalCalories += calories
            totalCaloriesTextView.text = "Total Calories: $totalCalories"

            // Clear the input fields
            foodNameEditText.text.clear()
            caloriesEditText.text.clear()
        }
    }
}