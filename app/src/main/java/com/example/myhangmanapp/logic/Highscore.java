package com.example.myhangmanapp.logic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.model.HighscoreObj;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Highscore extends AppCompatActivity {
    RecyclerViewAdapter adapter;
    Galgelogik logik;

    //For sharedpref
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    private ArrayList<HighscoreObj> highscoreObjs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        logik = logik.getInstance();
        loadData();

        initRecyclerView();
    }

    private void initRecyclerView() {
        if (logik.getHighscoreList() != null && highscoreObjs == null) {
            highscoreObjs = logik.getHighscoreList();
            Toast.makeText(this,"No scores yet saved!",Toast.LENGTH_SHORT).show();
        }

        HighscoreObj player1 = new HighscoreObj("Janus",100);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);
        highscoreObjs.add(player1);





        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        adapter = new RecyclerViewAdapter(this, highscoreObjs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<HighscoreObj> loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NAME, null);
        Type type = new TypeToken<ArrayList<HighscoreObj>>() {}.getType();

        if(highscoreObjs == null || highscoreObjs.isEmpty()) {
            highscoreObjs = new ArrayList<>();
        }
        return highscoreObjs = gson.fromJson(json,type);
    }
}
