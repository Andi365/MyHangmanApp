package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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

public class GameActivity extends AppCompatActivity implements OnClickListener {
    private ImageView hangmanPicture;
    private TextView fixedText;
    private TextView wordField;
    private EditText guessField;
    private Button submitGuess;
    private Pictures pictures;
    private String name;
    Galgelogik logik;
    //private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hangmanPicture = findViewById(R.id.gameImageview);
        fixedText = findViewById(R.id.gameStartText);
        guessField = findViewById(R.id.guessContainer);
        submitGuess = findViewById(R.id.guessButton);
        wordField = findViewById(R.id.wordField);

        name = getStringFromMainActivity();

        String fixedTextContainer = String.format("Welcome to this Hangman game, %s", name);
        fixedText.setText(fixedTextContainer);
        pictures = new Pictures();

        logik = logik.getInstance();
        System.out.println("task her");
        //task = new Task();
        hej();
        SystemClock.sleep(2000);
        System.out.println("task færdig her");

        wordField.setText(logik.getSynligtOrd());

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
            activitySwitchWinOrLost(logik.getAntalForkerteBogstaver());
        }

        System.out.println("Is game over?:  " + logik.erSpilletSlut());

        // Loading the right hangman picture & txt
        Picture picture = pictures.getHangmanPicture(pictureNumber);
        Drawable hangmanTopImage = ContextCompat.getDrawable(this,picture.getHangmanPicture());
        hangmanPicture.setImageDrawable(hangmanTopImage);

        wordField.setText(logik.getSynligtOrd());
    }

    private void activitySwitchWinOrLost(int wrongLetters) {
        Resources resources = getResources();
        String tries = resources.getString(R.string.tries);
        String wrongLetter = Integer.toString(wrongLetters);
        Intent intent = new Intent(GameActivity.this,WonOrLostActivity.class);
        intent.putExtra(tries, wrongLetter);
        startActivity(intent);
    }

    private String getStringFromMainActivity() {
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name_key));
        return name;
    }

    public void hej() {
        Thread hentRegneArk = new Thread() {
            @Override
            public void run() {
                try {
                    logik.hentOrdFraRegneark("12");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logik.nulstil();
            }
        };
        hentRegneArk.start();
    }

    /*class Task extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                System.out.println("in doInBackGround");
                logik.hentOrdFraRegneark("12");
                System.out.println("Finished");
                SystemClock.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("Task finished");
        }
    }*/
}
