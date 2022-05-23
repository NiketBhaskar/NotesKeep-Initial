package com.example.bridgelabzproject_fundonotes.database

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Spannable
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.bridgelabzproject_fundonotes.model.UserNote
import com.example.bridgelabzproject_fundonotes.model.UserNoteOperation


class SQLiteDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLENAME + "(" + NOTE_ID + " VARCHAR(500)," + USER_ID + " VARCHAR(500)," + NOTE_TITLE + " VARCHAR(500)," +
                NOTE_SUBTITLE + " VARCHAR(500)," + NOTE_CONTENT + " VARCHAR(2000)," + TIMESTAMP + " VARCHAR(500)" +")")
        db?.execSQL(createTable)
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLENAME = "Notes"
        private val NOTE_ID = "noteId"
        private val USER_ID = "userId"
        private val NOTE_TITLE = "noteTitle"
        private val NOTE_SUBTITLE = "noteSubtitle"
        private val NOTE_CONTENT = "noteContent"
        private val TIMESTAMP = "timeStamp"

    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    fun insertData(userNote: UserNote) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOTE_ID, userNote.noteId)
        contentValues.put(NOTE_TITLE, userNote.noteTitle)
        contentValues.put(NOTE_SUBTITLE, userNote.noteSubtitle)
        contentValues.put(NOTE_CONTENT, userNote.noteContent)
        contentValues.put(TIMESTAMP, userNote.timeStamp)
        val result = database.insert(TABLENAME, null, contentValues)
    }
    fun readData(): ArrayList<UserNote> {
        val list: ArrayList<UserNote> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val userNote = UserNote()
                userNote.noteId = result.getString(0)
                userNote.noteId = result.getString(0)
                userNote.userId = result.getString(0)
                userNote.noteTitle = result.getString(0)
                userNote.noteSubtitle = result.getString(0)
                userNote.noteContent = result.getString(0)
                userNote.timeStamp = result.getString(0)
                list.add(userNote)
            }
            while (result.moveToNext())
        }
        return list
    }

}