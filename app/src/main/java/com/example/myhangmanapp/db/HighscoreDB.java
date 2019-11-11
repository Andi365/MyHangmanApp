package com.example.myhangmanapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhangmanapp.model.Highscore;

public class HighscoreDB {
    SQLiteDatabase db;

    private HighscoreDB() {
    db = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + "/database.db", null);
    db.execSQL("DROP TABLE IF EXISTS highscore;");
    db.execSQL("CREATE TABLE highscore (id INTEGER PRIMARY KEY, name TEXT NOT NULL, score INTEGER);");

    //Adding three default players
    db.execSQL("INSERT INTO highscore (name,score) values ('Knud',40);");
    db.execSQL("INSERT INTO highscore (name,score) values ('Janus',60);");
    db.execSQL("INSERT INTO highscore (name,score) values ('Bo',20);");
    db.execSQL("SELECT * FROM highscore;");
    }

    public void addToDB(Highscore highscore) {
        /*ContentValues row = new ContentValues();
        String name1 = highscore.getName();
        System.out.println(name1);
        int score1 = highscore.getScore();
        System.out.println(score1);
        row.put("name",name1);
        row.put("score",score1);

        db.insert("highscore", null, row);*/

        db.execSQL("INSERT INTO highscore (name,score) values ('Knud',40);");

        //db.execSQL("INSERT INTO highscore (name,score) values ('gab',60);");
        db.close();
    }

    public String[] getTop3() {

        Cursor cursor = db.rawQuery("SELECT * FROM highscore ORDER BY score ASC;", null);
        String[] results = new String[3];
        for (int i = 0; i<=3; i++) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int score = cursor.getInt(2);

            String con = id + ":" + name + ";" + score;
            results[i] = con;
        }

        cursor.close();
        db.close();
        return results;
    }

    private static final HighscoreDB DBinstans = new HighscoreDB();

    public static HighscoreDB getInstance() {
       return DBinstans;
    }
}
