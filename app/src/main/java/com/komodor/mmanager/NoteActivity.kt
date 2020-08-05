package com.komodor.mmanager

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {


    private var note: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)


        setSupportActionBar(toolbar)


        note = intent.extras?.get("note_extra") as? Note

        Toast.makeText(this, note?.title, Toast.LENGTH_SHORT).show()

        if (note != null) {
            editTextNoteTitle.setText(note?.title)
            editTextNoteBody.setText(note?.body)
        }

        floatingActionButtonSaveNote.setOnClickListener {

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_note -> getNote(1)

            R.id.delete_note -> getNote(3)
        }

        return super.onOptionsItemSelected(item)
    }


    private fun getNote(action: Int) {
        val noteTitle = editTextNoteTitle.text.toString().trim()
        val noteBody = editTextNoteBody.text.toString().trim()


        if (noteTitle.isBlank()) {
            editTextNoteTitle.error = "title required"
            editTextNoteTitle.requestFocus()
            return
        }

        if (noteBody.isEmpty()) {
            editTextNoteBody.error = "note body cannot be empty"
            editTextNoteBody.requestFocus()
            return
        }
        val newNote = Note(noteTitle, noteBody)

        if (note == null) {
            Log.i("action", "going for save")

            saveNote(newNote, 1)
        } else {
            if (action == 1) {
                Log.i("action", "going for update")
                newNote.id = note!!.id
                saveNote(newNote, 2)
            } else if (action == 3) {
                Log.i("action", "going for delete ")
                AlertDialog.Builder(this).apply {
                    setTitle("Are You Sure?")
                    setMessage("this note will be deleted permanently")
                    setPositiveButton("yes") { _, _ ->
                        Log.i("action", "in the process delete")
                        saveNote(note!!, 3)

                    }
                    setNegativeButton("No") { _, _ ->
                    }
                }.create().show()


            }
        }


    }

    private fun saveNote(note: Note, action: Int) {
        class SaveNote() : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                when (action) {
                    1 -> {
                        Log.i("action", "note added")
                        NoteDatabase(this@NoteActivity).getNoteDao().addNote(note)
                    }
                    2 -> {
                        Log.i("action", "note updated")
                        NoteDatabase(this@NoteActivity).getNoteDao().updateNote(note)
                    }
                    3 -> {
                        Log.i("action", "note deleted")
                        NoteDatabase(this@NoteActivity).getNoteDao().deleteNote(note)
                    }
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                if (action == 1) {
                    Toast.makeText(this@NoteActivity, "Note Saved", Toast.LENGTH_SHORT).show()

                } else if (action == 2) {
                    Toast.makeText(this@NoteActivity, "Note updated", Toast.LENGTH_SHORT).show()

                }
                onBackPressed()

            }
        }
        SaveNote().execute()
    }


}
