package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Color;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.db.HighscoreDB;
import com.example.myhangmanapp.logic.Galgelogik;
import com.example.myhangmanapp.model.Highscore;

public class WonOrLostActivity extends AppCompatActivity implements OnClickListener {
    private TextView wonOrLostText,triesOrWordText,winner1,winner2,winner3;
    private Button tryAgainButton;
    private ConstraintLayout constraintLayout;
    Galgelogik logik;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wonorlost);
        logik = logik.getInstance();

        wonOrLostText = findViewById(R.id.statusText);
        tryAgainButton = findViewById(R.id.newGame);
        constraintLayout = findViewById(R.id.theme);
        triesOrWordText = findViewById(R.id.triesOrWord);
        winner1 = findViewById(R.id.winner1);
        winner2 = findViewById(R.id.winner2);
        winner3 = findViewById(R.id.winner3);

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
            wonOrLostText.setText(R.string.you_won);
            triesOrWordText.setText("You won with " + tries + " errors");
        } else {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[1]);
            constraintLayout.setBackgroundColor(color);
            wonOrLostText.setText(R.string.you_lost);
            triesOrWordText.setText("The word you tried to guess was: " + logik.getOrdet());
        }

        createHighscoreList();

    }

    private void createHighscoreList() {
        Resources resources = getResources();
        HighscoreDB highscoreDB = new HighscoreDB();
        int score = generateScore(logik.getAntalForkerteBogstaver(),logik.erSpilletVundet());
        Highscore hs = new Highscore(resources.getString(R.string.name_key),score);
        highscoreDB.addToDB(hs);

        String[] list = highscoreDB.getTop3();

        for (int i = 0;i<=3;i++) {
            winner1.setText(list[i]);
        }
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
    public void onClick(View v) {

    }
}
