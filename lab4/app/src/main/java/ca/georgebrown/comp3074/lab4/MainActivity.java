package ca.georgebrown.comp3074.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesAdapter adapter;
    private List<Note> notes = new ArrayList<>();
    private RecyclerView recyclerView;

  private  void addNotes(){
        DatabaseHelper db = new DatabaseHelper(this);
        List<Note> list = db.getAllNotes();
        if(list.isEmpty()){
            db.insertNote("test note 1");
            db.insertNote("test note 2");
            db.insertNote("remember to close database");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        addNotes();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        notes.addAll(dbHelper.getAllNotes());

        adapter = new NotesAdapter(this,notes);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }
}