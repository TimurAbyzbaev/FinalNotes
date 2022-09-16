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

public class NotesFragment extends Fragment {
    private ArrayList<Node> notes = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    static final String DATA_KEY = "DATA_KEY";
    private SharedPreferences sharedPreferences;
    public NotesFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        initRecycleView(recyclerView);
        return view;
    }

    private void initRecycleView(RecyclerView recyclerView) {
        notesAdapter = new NotesAdapter(notes);
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

        String savedData = sharedPreferences.getString(DATA_KEY, null);
        if(savedData == null){
            Toast.makeText(requireContext(), "Empty Data", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                Type type = new TypeToken<ArrayList<Node>>(){}.getType();
                notes = new GsonBuilder().create().fromJson(savedData, type);
                notesAdapter.setData(notes);

            }
            catch (Exception e){
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void showNodeFragment(int position) {
        NodeFragment nodeFragment = NodeFragment.newInstance(notes.get(position));
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
        return notes;
    }
    public void addNote(){
        notes.add(new Node("Заметка", ""));
        updateData();
        System.out.println(notes.size());
    }
    private void saveData(){
        String jsonData = new GsonBuilder().create().toJson(notes);
        sharedPreferences.edit().putString(DATA_KEY,jsonData).apply();
    }

    public void updateData(){
        notesAdapter.setData(notes);
        saveData();
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
                        Toast.makeText(requireContext(),"удаление", Toast.LENGTH_SHORT).show();
                        Node tempNode = notes.get(position);
                        notes.remove(position);
                        updateData();
                        showSnackbar(position, tempNode);
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
                        notes.add(position, tempNode);
                        updateData();
                    }
                }).show();
    }
}