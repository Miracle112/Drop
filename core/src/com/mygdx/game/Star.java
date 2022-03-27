package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Star extends Rectangle {
    Texture view;
    Sound dropSound;
    String type;
    public Star(String star) {
        view = new Texture(star + ".png");
        type = star;
    }
}
