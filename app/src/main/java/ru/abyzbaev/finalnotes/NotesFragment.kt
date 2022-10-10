package ru.abyzbaev.finalnotes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFragment extends Fragment implements NodeListSource{
    //private ArrayList<Node> notes;
    private NodeListSource data;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    static final String DATA_KEY = "DATA_KEY";
    private SharedPreferences sharedPreferences;
    public NotesFragment(){


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        notesAdapter = new NotesAdapter();
        data = new NodeListSourceFirebaseImpl().init(new NodeListResponse() {
            @Override
            public void initialized(NodeListSource nodeListSource) {
                notesAdapter.notifyDataSetChanged();
            }
        });
        notesAdapter.setDataSource(data);
        recyclerView = view.findViewById(R.id.recycle_view);
        initRecycleView(recyclerView);
        return view;
    }




    private void initRecycleView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
                showNodeFragment(position);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                initPopupMenu(view,position);
            }
        });
    }

    private void showNodeFragment(int position) {
        NodeFragment nodeFragment = NodeFragment.newInstance(data.getNodeList().get(position));
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.notes_container, nodeFragment)
                .addToBackStack("")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initRecycleView() {
        initRecycleView(recyclerView);
    }
    public ArrayList<Node> getNotes(){
        return data.getNodeList();
    }
    public void addNote(){
        data.addNode(new Node("Заметка", "123"));
        //data.getNodeList();
        //notesAdapter.setDataSource(data);
        updateData();
        //notesAdapter.notifyItemInserted(data.size());
        System.out.println("size = " + data.size());
    }


    public void updateData(){
        //notesAdapter.notifyItemInserted();
        data = new NodeListSourceFirebaseImpl().init(new NodeListResponse() {
            @Override
            public void initialized(NodeListSource nodeListSource) {
                notesAdapter.notifyDataSetChanged();
            }
        });
        notesAdapter.setDataSource(data);
        initRecycleView(recyclerView);
        //saveData();
    }

    public void updateData(Node updatedNode){
        data.updateNode(updatedNode);
        updateData();
    }


    private void initPopupMenu(View view, int position){
        Activity activity = requireActivity();
        PopupMenu popupMenu = new PopupMenu(activity, view);
        activity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_popup_delete:
                        //TODO delete note
                        deleteNode(position);
                        return true;
                    case R.id.action_popup_edit:
                        showNodeFragment(position);
                        return true;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void showSnackbar(int position, Node tempNode) {
        Snackbar.make(requireView(), "Заметка удалена", BaseTransientBottomBar.LENGTH_LONG)
                .setAction("Return", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.addNode(tempNode);
                        updateData();
                    }
                }).show();
    }

    @Override
    public NodeListSource init(NodeListResponse nodeListResponse) {
        if(nodeListResponse != null){
            nodeListResponse.initialized(this);
        }
        return this;
    }

    @Override
    public ArrayList<Node> getNodeList() {
        return data.getNodeList();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void deleteNode(int position) {
        //TODO deleteNote
        Toast.makeText(requireContext(),"удаление", Toast.LENGTH_SHORT).show();
        Node tempNode = data.getNodeList().get(position);
        //data.getNodeList().remove(position);
        data.deleteNode(position);
        updateData();
        showSnackbar(position, tempNode);
        

    }

    @Override
    public void updateNode(Node node) {
        data.updateNode(node);
    }

    @Override
    public void addNode(Node node) {
        data.getNodeList().add(new Node("Заметка", ""));
        updateData();
        System.out.println(data.getNodeList().size());
    }
}