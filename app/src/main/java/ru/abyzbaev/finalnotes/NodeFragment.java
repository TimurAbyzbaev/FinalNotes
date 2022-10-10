package ru.abyzbaev.finalnotes;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.GsonBuilder;
import static ru.abyzbaev.finalnotes.NotesFragment.DATA_KEY;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class NodeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SELECTED_NODE = "SELECTED_NODE";
    //private SharedPreferences sharedPreferences;
    private Node node;

    public NodeFragment() {
        // Required empty public constructor
    }


    public static NodeFragment newInstance(Node node) {
        NodeFragment fragment = new NodeFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NODE, node);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_node, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            Node paramNode = (Node) arguments.getParcelable(SELECTED_NODE);
            if(paramNode != null)
                node = paramNode;
        }
        initTv(view);
    }

    private void initTv(View view){
        EditText title = view.findViewById(R.id.note_title_edit);
        title.setText(node.getTitle());
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                node.setTitle(title.getText().toString());
                updateData(node);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        EditText description = view.findViewById(R.id.note_description_edit);
        description.setText(node.getDescription());
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                node.setDescription(description.getText().toString());
                updateData(node);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    private NotesFragment getNotesFragment(){
        return (NotesFragment) requireActivity()
                .getSupportFragmentManager()
                .getFragments()
                .stream()
                .filter(fragment -> fragment instanceof NotesFragment)
                .findFirst()
                .get();
    }

    private void updateData(Node updatedNode){
        //String jsonData = new GsonBuilder().create().toJson(getNotesFragment().getNotes());
        //sharedPreferences.edit().putString(DATA_KEY,jsonData).apply();

        getNotesFragment().updateData(updatedNode);
    }

    private Node collectNodeData(){
        String title = node.getTitle();
        String description = node.getDescription();
        String id = node.getId();
        Node answer;
        answer = new Node(id,title,description);
        return answer;
    }

}