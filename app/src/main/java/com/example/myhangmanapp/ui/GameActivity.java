package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.logic.Galgelogik;
import com.example.myhangmanapp.model.HighscoreObj;
import com.example.myhangmanapp.model.Picture;
import com.example.myhangmanapp.model.Pictures;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class GameActivity extends AppCompatActivity implements OnClickListener {
    private ArrayList<HighscoreObj> highscoreObjs = new ArrayList<>();
    private ImageView hangmanPicture;
    private TextView fixedText;
    private TextView wordField;
    private EditText guessField;
    private Button submitGuess;
    private Pictures pictures;
    private String name;
    private String gameSelect;
    Galgelogik logik;
    //https://stackoverflow.com/questions/7518803/wait-for-threads-to-complete-before-continuing
    final CountDownLatch latch = new CountDownLatch(1);

    //For sharedpref
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        logik = logik.getInstance();

        hangmanPicture = findViewById(R.id.gameImageview);
        fixedText = findViewById(R.id.gameStartText);
        guessField = findViewById(R.id.guessContainer);
        submitGuess = findViewById(R.id.guessButton);
        wordField = findViewById(R.id.wordField);

        name = getName();
        String fixedTextContainer = String.format("Welcome to this Hangman game, %s", name);
        fixedText.setText(fixedTextContainer);
        pictures = new Pictures();

        gameSelect = getGameKey();
        if(!gameSelect.equals("0")) {
            hentRegneArk.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logik.logStatus();

        wordField.setText("The word is: "+ logik.getSynligtOrd() + " used letters: " + logik.getBrugteBogstaver());

        submitGuess.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        String guess = guessField.getText().toString();

        logik.gætBogstav(guess);
        loadScreen(logik.getAntalForkerteBogstaver());
        guessField.getText().clear();
    }

    @Override
    public void onBackPressed() {
        logik.nulstil();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void loadScreen(int pictureNumber) {
        // Checks if game is lost or won and display screen accordingly
        if(logik.erSpilletVundet() || logik.erSpilletTabt()) {
            activitySwitchWinOrLost();
        }

        System.out.println("Is game over?:  " + logik.erSpilletSlut());

        // Loading the right hangman picture & txt
        Picture picture = pictures.getHangmanPicture(pictureNumber);
        Drawable hangmanTopImage = ContextCompat.getDrawable(this,picture.getHangmanPicture());
        hangmanPicture.setImageDrawable(hangmanTopImage);

        wordField.setText("The word is: "+ logik.getSynligtOrd() + " used letters: " + logik.getBrugteBogstaver());
    }

    private ArrayList<HighscoreObj> loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(NAME, null);
        Type type = new TypeToken<ArrayList<HighscoreObj>>() {}.getType();

        if(highscoreObjs == null) {
            highscoreObjs = new ArrayList<>();
        }

        return highscoreObjs = gson.fromJson(json, type);
    }

    private void activitySwitchWinOrLost() {
        Resources resources = getResources();
        String nameKey = resources.getString(R.string.name_key);
        Intent intent = new Intent(GameActivity.this,WonOrLostActivity.class);
        intent.putExtra(nameKey, name);
        startActivity(intent);
    }

    private String getName() {
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name_key));
        if(name == null || name.equals("")) {
            name = "Friend";
            return name;
        }
        System.out.println(name);
        return name;
    }

    private String getGameKey() {
        Intent intent = getIntent();
        gameSelect = intent.getStringExtra(getString(R.string.game_select_key));
        return gameSelect;
    }

    Thread hentRegneArk = new Thread() {
            @Override
            public void run() {
                if(gameSelect.equals("1")) {
                    try {
                        logik.hentOrdFraDr();
                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if(gameSelect.equals("2")) {
                    try {
                        logik.hentOrdFraRegneark("12");
                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    logik.nulstil();
                }
            }
        };
}
