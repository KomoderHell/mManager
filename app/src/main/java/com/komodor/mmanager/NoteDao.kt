package com.komodor.mmanager

import androidx.room.*


@Dao
interface NoteDao {

    @Insert
    fun addNote(note: Note)

    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes(): List<Note>

    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Delete
    suspend fun deleteMultipleNotes(list: MutableList<Note>)

}