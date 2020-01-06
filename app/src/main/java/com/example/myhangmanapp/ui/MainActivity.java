package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.logic.Galgelogik;
import com.example.myhangmanapp.model.Highscore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private EditText nameField;
    private Button normalGame,drGame,spreadsheetGame;
    private Galgelogik logik;
    private ArrayList<Highscore> highscores = new ArrayList<>();
    private String nameKey;

    //For sharedpref
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logik = logik.getInstance();
        logik.setHighscoreList(highscores);
        loadData();

        nameField = findViewById(R.id.nameContainer);
        normalGame = findViewById(R.id.normalGame);
        drGame = findViewById(R.id.getListFromDR);
        spreadsheetGame = findViewById(R.id.getListFromSpreadsheet);

        normalGame.setOnClickListener(this);
        drGame.setOnClickListener(this);
        spreadsheetGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        Resources resources = getResources();
        nameKey = nameField.getText().toString();

        String key = resources.getString(R.string.name_key);

        String gameKey = resources.getString(R.string.game_select_key);
        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        intent.putExtra(key, nameKey);
        //saveName();

        if(normalGame == isClicked) {
            intent.putExtra(gameKey,"0");
            startActivity(intent);
        } else if(drGame == isClicked) {
            intent.putExtra(gameKey,"1");
            startActivity(intent);
        } else if(spreadsheetGame == isClicked) {
            intent.putExtra(gameKey,"2");
            startActivity(intent);
        }
    }

    private ArrayList<Highscore> loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NAME, null);
        Type type = new TypeToken<ArrayList<Highscore>>() {}.getType();

        if(highscores == null) {
            highscores = new ArrayList<>();
        }

        return highscores = gson.fromJson(json, type);
    }
}
