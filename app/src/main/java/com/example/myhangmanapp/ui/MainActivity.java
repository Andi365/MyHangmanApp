package com.example.myhangmanapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import com.example.myhangmanapp.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private EditText nameField;
    private Button normalGame,drGame,spreadsheetGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameField = findViewById(R.id.nameContainer);
        normalGame = findViewById(R.id.normalGame);
        drGame = findViewById(R.id.getListFromDR);
        spreadsheetGame = findViewById(R.id.getListFromSpreadsheet);

        normalGame.setOnClickListener(this);
        drGame.setOnClickListener(this);
        spreadsheetGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        String name = nameField.getText().toString();
        Resources resources = getResources();
        String nameKey = resources.getString(R.string.name_key);
        String gameKey = resources.getString(R.string.game_select_key);
        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        intent.putExtra(nameKey, name);

        if(normalGame == isClicked) {
            startActivity(intent);
        } else if(drGame == isClicked) {
            intent.putExtra(gameKey,"1");
            startActivity(intent);
        } else if(spreadsheetGame == isClicked) {
            intent.putExtra(gameKey,"2");
            startActivity(intent);
        }
    }
}
