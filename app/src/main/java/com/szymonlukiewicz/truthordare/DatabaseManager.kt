package com.szymonlukiewicz.truthordare

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context?) : SQLiteOpenHelper(context, TABLE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL2 TEXT, $COL3 TEXT, $COL4 TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addNewSet(name: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL2, name)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun getSet(set_id: String): Cursor? {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL1 = $set_id"
        return db.rawQuery(query, null)
    }

    fun updateTruthsOrDaresList(set_id: String, column: String, content: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(column, content)
        val result = db.update(TABLE_NAME, contentValues, "$COL1 = $set_id", null)
        return result != -1
    }

    val data: Cursor
        get() {
            val db = this.writableDatabase
            val query = "SELECT * FROM " + TABLE_NAME
            return db.rawQuery(query, null)
        }

    fun updateSet(newName: String, id: Int, oldName: String) {
        val db = this.writableDatabase
        val query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'"
        db.execSQL(query)
    }

    fun deleteSet(id: Int, name: String) {
        val db = this.writableDatabase
        val query = ("DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'")
        db.execSQL(query)
    }

    companion object {
        private const val TAG = "DatabaseManager"
        private const val TABLE_NAME = "sets_table"
        private const val COL1 = "ID"
        private const val COL2 = "name"
        private const val COL3 = "truths_list"
        private const val COL4 = "dares_list"
    }
}