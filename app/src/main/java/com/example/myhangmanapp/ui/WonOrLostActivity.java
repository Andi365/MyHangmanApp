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

public class WonOrLostActivity extends AppCompatActivity implements OnClickListener {
    private TextView wonOrLostText,triesOrWordText;
    private Button tryAgainButton;
    private ConstraintLayout constraintLayout;
    private String tries;
    Galgelogik logik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wonorlost);

        wonOrLostText = findViewById(R.id.statusText);
        tryAgainButton = findViewById(R.id.newGame);
        constraintLayout = findViewById(R.id.theme);
        triesOrWordText = findViewById(R.id.triesOrWord);

        logik = logik.getInstance();

        //tries = getStringFromGameActivity();
        wonOrLost();

        tryAgainButton.setOnClickListener(this);
    }

    //Could be used instead of just using the logic
    /*private String getStringFromGameActivity() {
        Intent intent = getIntent();
        String wonOrLost = intent.getStringExtra(getString(R.string.tries));
        tries = wonOrLost;

        return tries;
    }*/

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
            triesOrWordText.setText("You won with " + tries + " tries");
        } else {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[1]);
            constraintLayout.setBackgroundColor(color);
            wonOrLostText.setText(R.string.you_lost);
            triesOrWordText.setText("The word you tried to guess was: " + logik.getOrdet());
        }
    }

    @Override
    public void onClick(View isClicked) {

    }
}
