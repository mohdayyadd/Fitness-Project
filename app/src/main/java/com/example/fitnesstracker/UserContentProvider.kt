package com.example.fitnesstracker

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class UserContentProvider : ContentProvider() {
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        val context = context ?: return false

        sUriMatcher.addURI(PROVIDER_NAME, "users", USERS)
        sUriMatcher.addURI(PROVIDER_NAME, "food", FOOD)
        sUriMatcher.addURI(PROVIDER_NAME, "weight", WEIGHT)
        sUriMatcher.addURI(PROVIDER_NAME, "food/#", FOOD_ID) // Add this line for specific food entry URIs
        sUriMatcher.addURI(PROVIDER_NAME, "food/#", WEIGHT_ID) // Add this line for specific food entry URIs


        val databaseHelper = DatabaseHelper(context)
        db = databaseHelper.writableDatabase

        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table = getTableName(uri)

        val rowId = db?.insert(table, null, values)

        if (rowId != null && rowId > 0) {
            val insertedUri = ContentUris.withAppendedId(uri, rowId)
            context?.contentResolver?.notifyChange(insertedUri, null)
            return insertedUri
        }

        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val table = getTableName(uri)

        return db?.query(table, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val table = getTableName(uri)

        val rowsUpdated = db?.update(table, values, selection, selectionArgs) ?: 0

        if (rowsUpdated > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return rowsUpdated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val table = getTableName(uri)

        val rowsDeleted = db?.delete(table, selection, selectionArgs) ?: 0

        if (rowsDeleted > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }

        return rowsDeleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    private fun getTableName(uri: Uri): String {
        return when (sUriMatcher.match(uri)) {
            USERS -> TABLE_USERS
            FOOD -> TABLE_FOOD
            WEIGHT -> TABLE_WEIGHT
            FOOD_ID -> TABLE_FOOD // Handle specific food entry URI
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    companion object {
        private const val PROVIDER_NAME = "com.example.fitnesstracker.provider"
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val TABLE_FOOD = "food"
        private const val TABLE_WEIGHT = "weight"
        private const val USERS = 1
        private const val FOOD = 2
        private const val WEIGHT = 3
        private const val FOOD_ID = 4
        private const val WEIGHT_ID = 4
    }

    private class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE $TABLE_USERS (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT," +
                        "password TEXT" +
                        ")"
            )

            db.execSQL(
                "CREATE TABLE $TABLE_FOOD (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "food TEXT," +
                        "calories INTEGER" +
                        ")"
            )

            db.execSQL(
                "CREATE TABLE $TABLE_WEIGHT (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "weight REAL," +
                        "sets INTEGER" +
                        ")"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_FOOD")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_WEIGHT")
            onCreate(db)
        }
    }
}
