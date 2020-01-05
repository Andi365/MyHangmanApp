package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class WonOrLostActivity extends AppCompatActivity implements OnClickListener {
    private TextView wonOrLostText,triesOrWordText, firstPlace, secondPlace, thirdPlace;
    private Button tryAgainButton;
    private ConstraintLayout constraintLayout;
    Galgelogik logik;
    private List<Highscore> highscores = new ArrayList<>();
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
        name = getName();

        if (logik.getHighscoreList() != null && highscores.size() == 0) {
            highscores.addAll(logik.getHighscoreList());
        }

        if(highscores.size() == 1) {
            firstPlace.setText(highscores.get(0).getName() + " har førstepladsen med scoren: " + highscores.get(0).getScore());
            secondPlace.setVisibility(View.INVISIBLE);
            thirdPlace.setVisibility(View.INVISIBLE);
        } else if(highscores.size() == 2) {
            firstPlace.setText(highscores.get(0).getName() + " har førstepladsen med scoren: " + highscores.get(0).getScore());
            secondPlace.setText(highscores.get(1).getName() + " har andenpladsen med scoren: " + highscores.get(1).getScore());
            thirdPlace.setVisibility(View.INVISIBLE);
        } else if(highscores.size() == 3) {
            firstPlace.setText(highscores.get(0).getName() + " har førstepladsen med scoren: " + highscores.get(0).getScore());
            secondPlace.setText(highscores.get(1).getName() + " har andenpladsen med scoren: " + highscores.get(1).getScore());
            thirdPlace.setText(highscores.get(2).getName() + " har tredjepladsen med scoren: " + highscores.get(2).getScore());
        }
    }

    private String getName() {
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name_key));
        return name;
    }

    @Override
    public void onClick(View isClicked) {
        if(tryAgainButton == isClicked) {
            logik.nulstil();
            firstPlace.setVisibility(View.VISIBLE);
            secondPlace.setVisibility(View.VISIBLE);
            thirdPlace.setVisibility(View.VISIBLE);
            Intent intent = new Intent(WonOrLostActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
