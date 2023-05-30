package com.example.fitnesstracker

import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TrackFood : AppCompatActivity() {

    private lateinit var foodNameEditText: EditText
    private lateinit var caloriesEditText: EditText
    private lateinit var addFoodButton: Button
    private lateinit var updateFoodButton: Button
    private lateinit var totalCaloriesTextView: TextView
    private lateinit var foodTable: TableLayout

    private var totalCalories: Int = 0

    private val foodEntries: MutableList<FoodEntry> = mutableListOf()
    private var selectedFoodEntry: FoodEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_track)

        foodNameEditText = findViewById(R.id.foodNameEditText)
        caloriesEditText = findViewById(R.id.caloriesEditText)
        addFoodButton = findViewById(R.id.addFoodButton)
        updateFoodButton = findViewById(R.id.updateFoodButton)
        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView)
        foodTable = findViewById(R.id.foodTable)

        addFoodButton.setOnClickListener {
            addFoodEntry()
        }

        updateFoodButton.setOnClickListener {
            updateFoodEntry()
        }

        loadFoodEntriesFromDatabase()
        displayFoodEntries()
    }
    private fun addFoodEntry() {
        val foodName = foodNameEditText.text.toString()
        val calories = caloriesEditText.text.toString().toIntOrNull()

        if (foodName.isNotEmpty() && calories != null) {
            val values = ContentValues()
            values.put("food", foodName)
            values.put("calories", calories)

            val uri = Uri.parse("content://com.example.fitnesstracker.provider/food")
            val newFoodUri = contentResolver.insert(uri, values)

            if (newFoodUri != null) {
                val id = newFoodUri.lastPathSegment?.toLongOrNull()
                val foodEntry = FoodEntry(id, foodName, calories)
                foodEntries.add(foodEntry)
                displayFoodEntry(foodEntry)

                totalCalories += calories
                totalCaloriesTextView.text = "Total Calories: $totalCalories"

                foodNameEditText.text.clear()
                caloriesEditText.text.clear()
            }
        }
    }
    private fun loadFoodEntriesFromDatabase() {
        val uri = Uri.parse("content://com.example.fitnesstracker.provider/food")
        val projection = arrayOf("_id", "food", "calories")
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use { c ->
            val idColumnIndex = c.getColumnIndexOrThrow("_id")
            val foodColumnIndex = c.getColumnIndexOrThrow("food")
            val caloriesColumnIndex = c.getColumnIndexOrThrow("calories")

            foodEntries.clear()

            while (c.moveToNext()) {
                val id = c.getLong(idColumnIndex)
                val foodName = c.getString(foodColumnIndex)
                val calories = c.getInt(caloriesColumnIndex)
                val foodEntry = FoodEntry(id, foodName, calories)
                foodEntries.add(foodEntry)
            }
        }
    }

    private fun displayFoodEntries() {
        foodTable.removeAllViews()

        for (foodEntry in foodEntries) {
            displayFoodEntry(foodEntry)
        }
    }

    private fun displayFoodEntry(foodEntry: FoodEntry) {
        val row = TableRow(this)

        val foodNameView = TextView(this)
        foodNameView.text = foodEntry.foodName
        row.addView(foodNameView)

        val caloriesView = TextView(this)
        caloriesView.text = foodEntry.calories.toString()
        row.addView(caloriesView)

        val editButton = Button(this)
        editButton.text = "Edit"
        editButton.setOnClickListener {

            selectedFoodEntry = foodEntry
            foodNameEditText.setText(foodEntry.foodName)
            caloriesEditText.setText(foodEntry.calories.toString())


            addFoodButton.setBackgroundColor(Color.GRAY)
            addFoodButton.isEnabled = false
            updateFoodButton.setBackgroundColor(Color.parseColor("#FF6200ED"))
        }
        row.addView(editButton)

        val deleteButton = Button(this)
        deleteButton.text = "Delete"
        deleteButton.setOnClickListener {

            foodTable.removeView(row)

            totalCalories -= foodEntry.calories
            totalCaloriesTextView.text = "Total Calories: $totalCalories"


            val uri = Uri.parse("content://com.example.fitnesstracker.provider/food/${foodEntry.id}")
            val rowsDeleted = contentResolver.delete(uri, null, null)
            if (rowsDeleted > 0) {

                foodEntries.remove(foodEntry)


            }
        }
        row.addView(deleteButton)

        foodTable.addView(row)


    }

    private fun updateFoodEntry() {
        val foodName = foodNameEditText.text.toString()
        val calories = caloriesEditText.text.toString().toIntOrNull()

        if (selectedFoodEntry != null && foodName.isNotEmpty() && calories != null) {
            val updatedFoodEntry = selectedFoodEntry!!.copy(foodName = foodName, calories = calories)


            val uri = Uri.parse("content://com.example.fitnesstracker.provider/food/${selectedFoodEntry!!.id}")
            val values = ContentValues().apply {
                put("food", foodName)
                put("calories", calories)
            }
            val rowsUpdated = contentResolver.update(uri, values, null, null)

            if (rowsUpdated > 0) {

                val index = foodEntries.indexOf(selectedFoodEntry!!)
                foodEntries[index] = updatedFoodEntry


                val row = foodTable.getChildAt(index) as TableRow
                val foodNameView = row.getChildAt(0) as TextView
                val caloriesView = row.getChildAt(1) as TextView

                foodNameView.text = updatedFoodEntry.foodName
                caloriesView.text = updatedFoodEntry.calories.toString()


                foodNameEditText.text.clear()
                caloriesEditText.text.clear()


                selectedFoodEntry = null
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private data class FoodEntry(val id: Long?, val foodName: String, val calories: Int)
}