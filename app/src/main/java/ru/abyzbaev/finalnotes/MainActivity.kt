package ru.abyzbaev.finalnotes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import ru.abyzbaev.finalnotes.NotesFragment
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import ru.abyzbaev.finalnotes.R

class MainActivity : AppCompatActivity() {
    private var notesFragment = NotesFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.notes_container, notesFragment)
                .commit()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //toolbar.setBackgroundColor(R.color.white);
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_add ->                 //TODO добавить заметку
                //Toast.makeText(this,"Тут добавление заметки", Toast.LENGTH_SHORT).show();
                notesFragment.addNote()
        }
        return super.onOptionsItemSelected(item)
    }
}