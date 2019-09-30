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
    private Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameField = findViewById(R.id.nameContainer);
        startButton = findViewById(R.id.nextPage);

        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View isClicked) {
        String name = nameField.getText().toString();

        if(startButton == isClicked) {
            Resources resources = getResources();
            String key = resources.getString(R.string.name_key);
            Intent intent = new Intent(MainActivity.this,GameActivity.class);
            intent.putExtra(key, name);
            startActivity(intent);
        }
    }
}
