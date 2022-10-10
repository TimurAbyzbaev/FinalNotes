package ru.abyzbaev.finalnotes


import ru.abyzbaev.finalnotes.NodeFragment.Companion.newInstance
//import ru.abyzbaev.finalnotes.NodeListSource.nodeList
//import ru.abyzbaev.finalnotes.NodeListSource.size

import ru.abyzbaev.finalnotes.NodeListSource
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.finalnotes.NotesAdapter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.abyzbaev.finalnotes.R
import ru.abyzbaev.finalnotes.NodeListSourceFirebaseImpl
import ru.abyzbaev.finalnotes.NodeListResponse
import android.widget.Toast
import android.widget.TextView
import ru.abyzbaev.finalnotes.NodeFragment
import android.app.Activity
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.util.ArrayList

class NotesFragment : Fragment(), NodeListSource {
    //private ArrayList<Node> notes;
    private var data: NodeListSource? = null
    private var recyclerView: RecyclerView? = null
    private var notesAdapter: NotesAdapter? = null
    private val sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        notesAdapter = NotesAdapter()
        data = NodeListSourceFirebaseImpl().init(object : NodeListResponse {
            override fun initialized(nodeListSource: NodeListSource?) {
                notesAdapter!!.notifyDataSetChanged()
            }
        })
        notesAdapter!!.setDataSource(data)
        recyclerView = view.findViewById(R.id.recycle_view)
        initRecycleView(recyclerView)
        return view
    }

    private fun initRecycleView(recyclerView: RecyclerView?) {
        recyclerView!!.setHasFixedSize(true)
        recyclerView.adapter = notesAdapter
        notesAdapter!!.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                Toast.makeText(context, (view as TextView).text, Toast.LENGTH_SHORT).show()
                showNodeFragment(position)
            }

            override fun onItemLongClick(view: View?, position: Int) {
                if (view != null) {
                    initPopupMenu(view, position)
                }
            }
        })
    }

    private fun showNodeFragment(position: Int) {
        val nodeFragment = newInstance(data?.getNodeList()?.get(position))
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.notes_container, nodeFragment)
            .addToBackStack("")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /*fun initRecycleView() {
        initRecycleView(recyclerView)
    }*/

    fun addNote() {
        data!!.addNode(Node("Заметка", "123"))
        //data.getNodeList();
        //notesAdapter.setDataSource(data);
        updateData()
        //notesAdapter.notifyItemInserted(data.size());
        println("size = " + data!!.size())
    }

    fun updateData() {
        //notesAdapter.notifyItemInserted();
        data = NodeListSourceFirebaseImpl().init(object : NodeListResponse {
            override fun initialized(nodeListSource: NodeListSource?) {
                notesAdapter!!.notifyDataSetChanged()
            }
        })
        notesAdapter!!.setDataSource(data)
        initRecycleView(recyclerView)
        //saveData();
    }

    fun updateData(updatedNode: Node?) {
        data!!.updateNode(updatedNode)
        updateData()
    }

    private fun initPopupMenu(view: View, position: Int) {
        val activity: Activity = requireActivity()
        val popupMenu = PopupMenu(activity, view)
        activity.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_popup_delete -> {
                    //TODO delete note
                    deleteNode(position)
                    return@OnMenuItemClickListener true
                }
                R.id.action_popup_edit -> {
                    showNodeFragment(position)
                    return@OnMenuItemClickListener true
                }
            }
            true
        })
        popupMenu.show()
    }

    private fun showSnackbar(position: Int, tempNode: Node) {
        Snackbar.make(requireView(), "Заметка удалена", BaseTransientBottomBar.LENGTH_LONG)
            .setAction("Return") {
                data!!.addNode(tempNode)
                updateData()
            }.show()
    }

    override fun init(nodeListResponse: NodeListResponse?): NodeListSource? {
        nodeListResponse?.initialized(this)
        return this
    }

    override fun getNodeList(): ArrayList<Node> {
        TODO("Not yet implemented")
    }

    /*val nodeList: ArrayList<Node?>?
        get() = data.nodeList*/

    override fun size(): Int {
        return 0
    }

    override fun deleteNode(position: Int) {
        //TODO deleteNote
        Toast.makeText(requireContext(), "удаление", Toast.LENGTH_SHORT).show()
        val tempNode: Node? = data?.getNodeList()?.get(position)
        //val tempNode: Node = data.nodeList.get(position)
        //data.getNodeList().remove(position);
        data!!.deleteNode(position)
        updateData()
        if (tempNode != null) {
            showSnackbar(position, tempNode)
        }
    }

    override fun updateNode(node: Node?) {
        data!!.updateNode(node)
    }

    override fun addNode(node: Node?) {
        data?.getNodeList()?.add(Node("Заметка", ""))
        updateData()
        //println(data.getNodeList().size)
    }

    companion object {
        const val DATA_KEY = "DATA_KEY"
    }
}