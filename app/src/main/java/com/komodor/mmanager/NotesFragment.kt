package com.komodor.mmanager

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.coroutines.launch

class NotesFragment : BaseFragment(),OnNoteListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {


        return inflater.inflate(R.layout.fragment_notes, container, false)


    }

    private var listMode: Int = 1
    private var list: MutableList<Note> = mutableListOf()
    private var selectedList: MutableList<Note> = mutableListOf()
    private var adapter : NotesAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        recyclerViewNotes.setHasFixedSize(true)
        recyclerViewNotes.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        floatingActionButtonAddNote.setOnClickListener {
            startActivity(Intent(context, NoteActivity::class.java))
        }


        adapter = context?.let { NotesAdapter(list, it,this@NotesFragment,listMode) }
        recyclerViewNotes.adapter = adapter


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        selectAll.setOnClickListener {
            if (selectedList.size != list.size){
                selectedList.clear()
                selectedList.addAll(list)
                adapter?.allSelected =  true
                adapter?.notifyDataSetChanged()
            }
        }

        cancelMultipleDelete.setOnClickListener {
            selectedList.clear()
            adapter?.selected?.clear()
            adapter?.listMode = 1
            adapter?.allSelected  = false
            onStart()
            floatingActionButtonAddNote.visibility = View.VISIBLE
            bottomCollapsibleBar.visibility = View.GONE

        }

        deleteSelectedNotes.setOnClickListener {

            launch {
                context?.let {
                    NoteDatabase(it).getNoteDao().deleteMultipleNotes(selectedList)
                    selectedList.clear()
                    adapter?.selected?.clear()
                    adapter?.listMode = 1
                    adapter?.allSelected  = false
                    bottomCollapsibleBar.visibility = View.GONE
                    floatingActionButtonAddNote.visibility = View.VISIBLE
                    list.clear()
                    list.addAll(NoteDatabase(it).getNoteDao().getAllNotes())
                    recyclerViewNotes.adapter?.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onStart() {
        launch {
            context?.let {
                list.clear()
                list.addAll(NoteDatabase(it).getNoteDao().getAllNotes())
                recyclerViewNotes.adapter?.notifyDataSetChanged()
            }
        }
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_activity_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onNoteClickListener(position: Int,listMode:Int,isSelected:Boolean) {
        if (listMode == 1){
            val intent = Intent(context,NoteActivity::class.java)
            intent.putExtra("note_extra",list[position])
            startActivity(intent)
        }
        else if(listMode == 2){
            if (isSelected){
                adapter?.selected?.remove(position)
                selectedList.remove(list[position])
            }
            else{
                adapter?.selected?.add(position)
                selectedList.add(list[position])
            }
        }
    }

    override fun onLongNoteClickListener(position: Int) {
        selectedList.clear()
        adapter?.listMode = 2
        adapter?.allSelected = false
        onStart()
        bottomCollapsibleBar.visibility = View.VISIBLE
        floatingActionButtonAddNote.visibility = View.GONE
    }


}