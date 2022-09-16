package ru.abyzbaev.finalnotes;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    NotesFragment notesFragment = new NotesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notes_container, notesFragment)
                    .commit();
        }

    }

    @SuppressLint("ResourceAsColor")
    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setBackgroundColor(R.color.white);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                //TODO добавить заметку
                //Toast.makeText(this,"Тут добавление заметки", Toast.LENGTH_SHORT).show();
                notesFragment.addNote();
        }
        return super.onOptionsItemSelected(item);
    }
}