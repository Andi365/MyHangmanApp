package com.example.myhangmanapp.model;

import com.example.myhangmanapp.R;

public class Pictures {
    private Picture[] hangmanPictures;

    public Pictures() {
        hangmanPictures = new Picture[6];

        hangmanPictures[0] = new Picture(R.drawable.forkert1);
        hangmanPictures[1] = new Picture(R.drawable.forkert2);
        hangmanPictures[2] = new Picture(R.drawable.forkert3);
        hangmanPictures[3] = new Picture(R.drawable.forkert4);
        hangmanPictures[4] = new Picture(R.drawable.forkert5);
        hangmanPictures[5] = new Picture(R.drawable.forkert6);
    }

    public Picture getHangmanPicture(int hangmanPictureNumber) {
        if(hangmanPictureNumber >= hangmanPictures.length) {
            hangmanPictureNumber = 0;
        }
        return hangmanPictures[hangmanPictureNumber];
    }
}
