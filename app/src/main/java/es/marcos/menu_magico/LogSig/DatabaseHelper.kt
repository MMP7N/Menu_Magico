package es.marcos.menu_magico.LogSig

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PROFILE_PIC = "profile_pic"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_USERNAME TEXT, " +
                    "$COLUMN_PASSWORD TEXT, " +
                    "$COLUMN_EMAIL TEXT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_PROFILE_PIC TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertUser(username: String, password: String, email: String, name: String, profilePic: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EMAIL, email)
            put(COLUMN_NAME, name)
            put(COLUMN_PROFILE_PIC, profilePic)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun getUserDetails(username: String): Map<String, String> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_NAME, COLUMN_PROFILE_PIC),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        val userDetails = mutableMapOf<String, String>()
        if (cursor.moveToFirst()) {
            userDetails["name"] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            userDetails["profile_pic"] = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_PIC))
        }
        cursor.close()
        return userDetails
    }
}
