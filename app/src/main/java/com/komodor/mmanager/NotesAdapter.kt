package com.komodor.mmanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_layout.view.*

class NotesAdapter(val list: List<Note>,val context: Context,val onNoteListener: OnNoteListener, var listMode : Int) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

     var allSelected : Boolean = false
    var selected :MutableList<Int> = mutableListOf()
    inner class NoteViewHolder(val view: View): RecyclerView.ViewHolder(view)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return  NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_layout,parent,false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        if (listMode == 2){
            holder.view.checkBoxSelected.visibility = View.VISIBLE
            holder.view.checkBoxSelected.isChecked = selected.contains(position)
        } else if(listMode ==1){
            holder.view.checkBoxSelected.visibility = View.GONE

        }
        if (allSelected){
            holder.view.checkBoxSelected.isChecked = true
        }


        holder.view.textViewNoteTitle.text = list[position].title
        holder.view.textViewNoteBody.text = list[position].body

        holder.view.setOnLongClickListener {
            onNoteListener.onLongNoteClickListener(position)
            true
        }
        holder.view.setOnClickListener {
            onNoteListener.onNoteClickListener(position,listMode, holder.view.checkBoxSelected.isChecked)
            holder.view.checkBoxSelected.isChecked = !holder.view.checkBoxSelected.isChecked
        }
    }


}    public interface OnNoteListener{
    fun onNoteClickListener(position: Int,listMode: Int,isSelected:Boolean)
    fun onLongNoteClickListener(position: Int)
}