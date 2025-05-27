package com.example.app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 3

        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PROFILE_PIC = "profile_pic"
        private const val COLUMN_PREFERENCES = "preferences"
        private const val COLUMN_FAVORITES = "favorites"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_USERNAME TEXT, " +
                        "$COLUMN_PASSWORD TEXT, " +
                        "$COLUMN_EMAIL TEXT, " +
                        "$COLUMN_PROFILE_PIC TEXT, " +
                        "$COLUMN_PREFERENCES TEXT, " +
                        "$COLUMN_FAVORITES TEXT)"
                )
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_PREFERENCES TEXT DEFAULT ''")
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_FAVORITES TEXT DEFAULT ''")
        }
    }

    fun insertUser(
        username: String,
        password: String,
        email: String,
        profilePic: String,
        preferences: String = "",
        favorites: String = ""
    ): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PROFILE_PIC, profilePic)
            put(COLUMN_PREFERENCES, preferences)
            put(COLUMN_FAVORITES, favorites)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    @Suppress("Range")
    fun getUserDetails(username: String): Map<String, String> {
        val db = readableDatabase
        val cursor = db.query(
            "data",
            arrayOf(
                "username",
                "email",
                "profile_pic",
                "preferences",
                "favorites"
            ),
            "username = ?",
            arrayOf(username),
            null, null, null
        )

        val userDetails = mutableMapOf<String, String>()
        if (cursor.moveToFirst()) {
            userDetails["username"] = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            userDetails["email"] = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            userDetails["profile_pic"] =
                cursor.getString(cursor.getColumnIndexOrThrow("profile_pic")) ?: ""
            userDetails["preferences"] =
                cursor.getString(cursor.getColumnIndexOrThrow("preferences")) ?: ""
            userDetails["favorites"] =
                cursor.getString(cursor.getColumnIndexOrThrow("favorites")) ?: ""
        }
        cursor.close()
        return userDetails
    }


    fun updateUserFields(
        username: String,
        profilePic: String,
        preferences: String,
        favorites: String
    ): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PROFILE_PIC, profilePic)
            put(COLUMN_PREFERENCES, preferences)
            put(COLUMN_FAVORITES, favorites)
        }
        return db.update(TABLE_NAME, contentValues, "$COLUMN_USERNAME = ?", arrayOf(username))
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor =
            db.query(TABLE_NAME, null, selection, arrayOf(username, password), null, null, null)
        val userExists = cursor.moveToFirst()
        cursor.close()
        return userExists
    }
}
