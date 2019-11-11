package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.myhangmanapp.R;
import com.example.myhangmanapp.logic.Galgelogik;
import com.example.myhangmanapp.model.Picture;
import com.example.myhangmanapp.model.Pictures;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

public class GameActivity extends AppCompatActivity implements OnClickListener {
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

        wordField.setText("Ordet er: "+logik.getSynligtOrd() + " bogstaver: " + logik.getBrugteBogstaver());

        submitGuess.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        String guess = guessField.getText().toString();

        logik.gætBogstav(guess);
        loadScreen(logik.getAntalForkerteBogstaver());
        guessField.getText().clear();
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

        wordField.setText("Ordet er: "+logik.getSynligtOrd() + " bogstaver: " + logik.getBrugteBogstaver());
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
        if(name == null) {
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
