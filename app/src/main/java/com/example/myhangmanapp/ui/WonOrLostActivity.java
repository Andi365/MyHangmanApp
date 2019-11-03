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

public class WonOrLostActivity extends AppCompatActivity implements OnClickListener {
    private TextView wonOrLostText;
    private Button tryAgainButton;
    private ConstraintLayout constraintLayout;
    int statusKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wonorlost);

        wonOrLostText = findViewById(R.id.statusText);
        tryAgainButton = findViewById(R.id.newGame);
        constraintLayout = findViewById(R.id.theme);

        statusKey = getStringFromGameActivity();
        wonOrLost(statusKey);

        tryAgainButton.setOnClickListener(this);
    }

    private int getStringFromGameActivity() {
        Intent intent = getIntent();
        String wonOrLost = intent.getStringExtra(getString(R.string.won_or_lost));
        statusKey = Integer.parseInt(wonOrLost);
        return statusKey;
    }

    private void wonOrLost(int statusKey) {
        int color;
        String[] colors;
        Resources resources;
        if(statusKey == 1) {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[0]);
            constraintLayout.setBackgroundColor(color);
            wonOrLostText.setText(R.string.you_won);
        } else {
            resources = getResources();
            colors = resources.getStringArray(R.array.colors);
            color = Color.parseColor(colors[1]);
            constraintLayout.setBackgroundColor(color);
            wonOrLostText.setText(R.string.you_lost);
        }
    }

    @Override
    public void onClick(View isClicked) {

    }


}
