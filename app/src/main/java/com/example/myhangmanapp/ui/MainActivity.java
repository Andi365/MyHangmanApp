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
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private EditText nameField;
    private Button normalGame,drGame,spreadsheetGame;
    private Galgelogik logik;
    private List<Highscore> highscores;
    private String name;
    private String nameKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logik = logik.getInstance();
        highscores = logik.getHighscores();

        nameField = findViewById(R.id.nameContainer);
        normalGame = findViewById(R.id.normalGame);
        drGame = findViewById(R.id.getListFromDR);
        spreadsheetGame = findViewById(R.id.getListFromSpreadsheet);

        getScore();

        normalGame.setOnClickListener(this);
        drGame.setOnClickListener(this);
        spreadsheetGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        Resources resources = getResources();
        name = nameField.getText().toString();
        nameKey = resources.getString(R.string.name_key);

        String gameKey = resources.getString(R.string.game_select_key);
        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        intent.putExtra(nameKey, name);
        saveName();

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

    private void saveName(){
        Resources resources = getResources();
        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        nameKey = resources.getString(R.string.name_key);
        editor.putString("name",nameKey);
        editor.apply();
    }

    private void getScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("highscore", null);
        Type type = new TypeToken<ArrayList<Highscore>>() {
        }.getType();
        highscores = gson.fromJson(json, type);
        name = sharedPreferences.getString("name",null);
        if (highscores == null) {
            highscores = new ArrayList<>();
        }
    }
}
