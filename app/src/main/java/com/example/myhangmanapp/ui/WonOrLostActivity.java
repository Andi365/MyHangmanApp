package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.logic.Galgelogik;
import com.example.myhangmanapp.model.Highscore;
import com.google.gson.Gson;

import java.util.List;

public class WonOrLostActivity extends AppCompatActivity implements OnClickListener {
    private TextView wonOrLostText,triesOrWordText, firstPlace, secondPlace, thirdPlace;
    private Button tryAgainButton;
    private ConstraintLayout constraintLayout;
    Galgelogik logik;
    private List<Highscore> highscores;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wonorlost);
        logik = logik.getInstance();
        highscores = logik.getHighscores();

        wonOrLostText = findViewById(R.id.statusText);
        tryAgainButton = findViewById(R.id.newGame);
        constraintLayout = findViewById(R.id.theme);
        triesOrWordText = findViewById(R.id.triesOrWord);
        firstPlace = findViewById(R.id.winner1);
        secondPlace = findViewById(R.id.winner2);
        thirdPlace = findViewById(R.id.winner3);

        wonOrLost();

        tryAgainButton.setOnClickListener(this);
    }


    private void wonOrLost() {
        int color;
        String[] colors;
        Resources resources;

        if(logik.erSpilletVundet()) {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[0]);
            constraintLayout.setBackgroundColor(color);
            String tries = Integer.toString(logik.getAntalForkerteBogstaver());
            generateScoreBoard();
            wonOrLostText.setText(R.string.you_won);
            triesOrWordText.setText("You had " + tries + " errors");
        } else {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[1]);
            constraintLayout.setBackgroundColor(color);
            wonOrLostText.setText(R.string.you_lost);
            triesOrWordText.setText("The word you tried to guess was: " + logik.getOrdet());
        }

        generateScoreBoard();
    }

    private void generateScoreBoard() {
        int score = generateScore(logik.getAntalForkerteBogstaver(),logik.erSpilletVundet());

        name = getName();
        logik.setHighscores(name,score);
        saveScore();

        if(highscores.size()>=1) {
            firstPlace.setText(highscores.get(0).getName() + " har førstepladsen med scoren: " + highscores.get(0).getScore());
        } else if (highscores.size()>=2) {
            secondPlace.setText(highscores.get(1).getName() + " har førstepladsen med scoren: " + highscores.get(1).getScore());
        } else {
            thirdPlace.setText(highscores.get(2).getName() + " har førstepladsen med scoren: " + highscores.get(2).getScore());
        }
    }

    private String getName() {
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name_key));
        return name;
    }

    void saveScore(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(logik.getHighscoreList());
        editor.putString("highscore",json);
        editor.apply();
    }

    private int generateScore(int wrongLetters, Boolean gameWon) {
        int score = 100;
        if(wrongLetters <= 2 && gameWon) {
            score = 100 - 20;
        } else if((wrongLetters <= 4 && wrongLetters > 2) && gameWon) {
            score = 100-40;
        } else if((wrongLetters <= 6 && wrongLetters > 4) && gameWon) {
            score = 100-60;
        } else if(!gameWon) {
            score = 0;
        }
        return score;
    }

    @Override
    public void onClick(View isClicked) {
        if(tryAgainButton == isClicked) {
            Intent intent = new Intent(WonOrLostActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
