package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.logic.Galgelogik;
import com.example.myhangmanapp.model.Highscore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class WonOrLostActivity extends AppCompatActivity implements OnClickListener {
    private TextView wonOrLostText,triesOrWordText, firstPlace, secondPlace, thirdPlace;
    private Button tryAgainButton;
    private ConstraintLayout constraintLayout;
    private nl.dionsegijn.konfetti.KonfettiView viewKonfetti;
    private com.airbnb.lottie.LottieAnimationView lottieAnimationView;
    Galgelogik logik;
    private ArrayList<Highscore> highscores = new ArrayList<>();
    private String name;
    private String nameKey;

    //For sharedpref
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wonorlost);
        logik = logik.getInstance();

        loadData();

        wonOrLostText = findViewById(R.id.statusText);
        tryAgainButton = findViewById(R.id.newGame);
        constraintLayout = findViewById(R.id.theme);
        triesOrWordText = findViewById(R.id.triesOrWord);
        firstPlace = findViewById(R.id.winner1);
        secondPlace = findViewById(R.id.winner2);
        thirdPlace = findViewById(R.id.winner3);
        viewKonfetti = findViewById(R.id.viewKonfetti);
        lottieAnimationView = findViewById(R.id.animation_view);

        generatescore();

        tryAgainButton.setOnClickListener(this);
    }

    private void generatescore() {
        String name = getName();
        int score = 0;

        int wrongLetters = logik.getAntalForkerteBogstaver();
        boolean isGameWon = logik.erSpilletVundet();
        if(wrongLetters <= 1 && isGameWon) {
            score = 100 - 20;
        } else if((wrongLetters <= 3 && wrongLetters > 1) && isGameWon) {
            score = 100-40;
        } else if((wrongLetters <= 5 && wrongLetters > 3) && isGameWon) {
            score = 100-60;
        }

        logik.setHighscores(name,score);
        saveHighscore();

        wonOrLost();
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

            //Confetti implementation
            viewKonfetti.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(false)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5))
                    .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                    .streamFor(300, 5000L);

            // Sound of applause
            MediaPlayer mp = MediaPlayer.create(this, R.raw.applause);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } else {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[1]);
            constraintLayout.setBackgroundColor(color);
            lottieAnimationView.setVisibility(View.INVISIBLE);
            wonOrLostText.setText(R.string.you_lost);
            triesOrWordText.setText("The word you tried to guess was: " + logik.getOrdet());

            MediaPlayer mp = MediaPlayer.create(this, R.raw.boohiss);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }

        generateScoreBoard();
    }

    private void generateScoreBoard() {
        if (logik.getHighscoreList() != null && highscores == null) {
            highscores = logik.getHighscoreList();
        }

        if(highscores.size() == 1) {
            firstPlace.setText(highscores.get(0).getName() + " is in the lead with a score of: " + highscores.get(0).getScore());
            secondPlace.setVisibility(View.INVISIBLE);
            thirdPlace.setVisibility(View.INVISIBLE);
        } else if(highscores.size() == 2) {
            firstPlace.setText(highscores.get(0).getName() + " is in the lead with a score of: " + highscores.get(0).getScore());
            secondPlace.setText(highscores.get(1).getName() + " has second place with a score of: " + highscores.get(1).getScore());
            thirdPlace.setVisibility(View.INVISIBLE);
        } else if(highscores.size() == 3) {
            firstPlace.setText(highscores.get(0).getName() + " is in the lead with a score of: " + highscores.get(0).getScore());
            secondPlace.setText(highscores.get(1).getName() + " has second place with a score of: " + highscores.get(1).getScore());
            thirdPlace.setText(highscores.get(2).getName() + " has third place with a score of: " + highscores.get(2).getScore());
        }
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

    @Override
    public void onBackPressed() {
        logik.nulstil();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void saveHighscore(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(highscores);
        editor.putString(NAME,json);
        editor.apply();

        Toast.makeText(this,"Highscore saved",Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NAME, null);
        Type type = new TypeToken<ArrayList<Highscore>>() {}.getType();

        if(highscores == null) {
            highscores = new ArrayList<>();
        }
        highscores = gson.fromJson(json,type);
    }

    private String getName() {
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name_key));
        return name;
    }
}
