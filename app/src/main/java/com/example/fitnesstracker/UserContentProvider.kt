package com.example.fitnesstracker


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.content.*
import android.database.sqlite.SQLiteOpenHelper


private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

class UserContentProvider : ContentProvider() {

    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        val context = context ?: return false

        sUriMatcher.addURI(PROVIDER_NAME, "users", USERS)
        sUriMatcher.addURI(PROVIDER_NAME, "users/#", SINGLE_USER)

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
            SINGLE_USER -> TABLE_USERS
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    companion object {
        private const val PROVIDER_NAME = "com.example.fitnesstracker.provider"
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val USERS = 1
        private const val SINGLE_USER = 2
    }
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
    }
}


