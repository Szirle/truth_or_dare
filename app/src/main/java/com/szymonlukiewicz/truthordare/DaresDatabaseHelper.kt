package com.szymonlukiewicz.truthordare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by User on 2/28/2017.
 */
class DaresDatabaseHelper(context: Context?) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addData(item: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL2, item)
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME)
        val result = db.insert(TABLE_NAME, null, contentValues)

        //if date as inserted incorrectly it will return -1
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    val data: Cursor
        get() {
            val db = this.writableDatabase
            val query = "SELECT * FROM " + TABLE_NAME
            return db.rawQuery(query, null)
        }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    fun getItemID(name: String): Cursor {
        val db = this.writableDatabase
        val query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'"
        return db.rawQuery(query, null)
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    fun updateName(newName: String, id: Int, oldName: String) {
        val db = this.writableDatabase
        val query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'"
        Log.d(TAG, "updateName: query: $query")
        Log.d(TAG, "updateName: Setting name to $newName")
        db.execSQL(query)
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    fun deleteName(id: Int, name: String) {
        val db = this.writableDatabase
        val query = ("DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'")
        Log.d(TAG, "deleteName: query: $query")
        Log.d(TAG, "deleteName: Deleting $name from database.")
        db.execSQL(query)
    }

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val TABLE_NAME = "dares_table"
        private const val COL1 = "ID"
        private const val COL2 = "name"
    }
}