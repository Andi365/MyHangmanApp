package com.example.myhangmanapp.model;

import com.example.myhangmanapp.logic.Galgelogik;

public class HighscoreObj {
    private Galgelogik logik;

    private String name;
    private int score = 100;

    public HighscoreObj(String name, int score) {
        this.name = name;
        this.score = score;
        logik = logik.getInstance();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
