package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
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
        logik = new Galgelogik();

        submitGuess.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        String guess = guessField.getText().toString();

        logik.gætBogstav(guess);
        loadScreen(logik.getAntalForkerteBogstaver());
    }

    private void loadScreen(int pictureNumber) {
        // Checks if game is lost or won and display screen accordingly
        if(logik.erSpilletVundet()) {
            activitySwitchWinOrLost("1");
        } else if(logik.erSpilletTabt()) {
            activitySwitchWinOrLost("0");
        }

        System.out.println("test " + logik.erSpilletSlut());

        // Loading the right hangman picture
        Picture picture = pictures.getHangmanPicture(pictureNumber);
        Drawable hangmanTopImage = ContextCompat.getDrawable(this,picture.getHangmanPicture());
        hangmanPicture.setImageDrawable(hangmanTopImage);

        wordField.setText(logik.getSynligtOrd());
    }

    private void activitySwitchWinOrLost(String wonOrLost) {
        Resources resources = getResources();
        String statusKey = resources.getString(R.string.won_or_lost);
        Intent intent = new Intent(GameActivity.this,WonOrLostActivity.class);
        intent.putExtra(statusKey, wonOrLost);
        startActivity(intent);
    }

    private String getStringFromMainActivity() {
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name_key));
        return name;
    }


}
