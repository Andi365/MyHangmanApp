package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
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
    private EditText guessField;
    private Button submitGuess;
    private Pictures pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        hangmanPicture = findViewById(R.id.gameImageview);
        fixedText = findViewById(R.id.gameStartText);
        guessField = findViewById(R.id.guessContainer);
        submitGuess = findViewById(R.id.guessButton);

        String name = getStringFromMainActivity();

        String fixedTextContainer = String.format("Welcome to this Hangman game, %s", name/*getResources().getString(R.string.name_key)*/);
        System.out.println(fixedTextContainer);
        fixedText.setText(fixedTextContainer);
        pictures = new Pictures();

        submitGuess.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        Galgelogik logik = new Galgelogik();
        String guess = guessField.getText().toString();

        if(submitGuess == isClicked) {
            logik.gætBogstav(guess);
            if(!logik.erSidsteBogstavKorrekt()) {
                if(logik.getAntalForkerteBogstaver() != 0) {
                    loadPicture(logik.getAntalForkerteBogstaver());
                }
            }
        }
    }

    private void loadPicture(int pictureNumber) {
        Picture picture = pictures.getHangmanPicture(pictureNumber);

        Drawable hangmanTopImage = ContextCompat.getDrawable(this,picture.getHangmanPicture());
        hangmanPicture.setImageDrawable(hangmanTopImage);


    }

    private String getStringFromMainActivity() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("key");
        return name;
    }


}
