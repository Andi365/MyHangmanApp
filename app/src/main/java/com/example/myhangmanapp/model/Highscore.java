package com.example.myhangmanapp.model;

import com.example.myhangmanapp.logic.Galgelogik;

public class Highscore {
    private Galgelogik logik;

    private String name;
    private int score = 100;

    public Highscore(String name) {
        this.name = name;
        logik = logik.getInstance();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    //TODO: FIX CALCULATION OF PLAYERSCORE SO IT*S NOT JUST DEFAULT 100.
    public int generateScore() {
        int wrongLetters = logik.getAntalForkerteBogstaver();
        boolean isGameWon = logik.erSpilletVundet();
        if(wrongLetters <= 2 && isGameWon) {
            return score = 100 - 20;
        } else if((wrongLetters <= 4 && wrongLetters > 2) && isGameWon) {
            return score = 100-40;
        } else if((wrongLetters <= 6 && wrongLetters > 4) && isGameWon) {
            return score = 100-60;
        } else if(!isGameWon) {
            return score = 0;
        }
        return 0;
    }
}
