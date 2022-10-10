package ru.abyzbaev.finalnotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Parcelable
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment

class NodeFragment : Fragment() {
    //private SharedPreferences sharedPreferences;
    private var node: Node? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_node, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            val paramNode = arguments.getParcelable<Parcelable>(SELECTED_NODE) as Node?
            if (paramNode != null) node = paramNode
        }
        initTv(view)
    }

    private fun initTv(view: View) {
        val title = view.findViewById<EditText>(R.id.note_title_edit)
        title.setText(node!!.title)
        title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                node!!.title = title.text.toString()
                updateData(node)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val description = view.findViewById<EditText>(R.id.note_description_edit)
        description.setText(node!!.description)
        description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                node!!.description = description.text.toString()
                updateData(node)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private val notesFragment: NotesFragment
        get() = requireActivity()
            .supportFragmentManager
            .fragments
            .stream()
            .filter { fragment: Fragment? -> fragment is NotesFragment }
            .findFirst()
            .get() as NotesFragment

    private fun updateData(updatedNode: Node?) {
        //String jsonData = new GsonBuilder().create().toJson(getNotesFragment().getNotes());
        //sharedPreferences.edit().putString(DATA_KEY,jsonData).apply();
        notesFragment.updateData(updatedNode)
    }

    private fun collectNodeData(): Node {
        val title = node!!.title
        val description = node!!.description
        val id = node!!.id
        return Node(id, title, description)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val SELECTED_NODE = "SELECTED_NODE"
        @JvmStatic
        fun newInstance(node: Node?): NodeFragment {
            val fragment = NodeFragment()
            val args = Bundle()
            args.putParcelable(SELECTED_NODE, node)
            fragment.arguments = args
            return fragment
        }
    }
}