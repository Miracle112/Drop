package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenuScreen implements Screen {
    Drop game;
    OrthographicCamera camera;
    TextureRegion backgroundTexture;
    public MainMenuScreen(Drop gam){
        backgroundTexture = new TextureRegion(new Texture("bg.jpg"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(backgroundTexture, 0, Gdx.graphics.getHeight());
        game.font.draw(game.batch, "Compose a melody!", 350, 250);
        game.font.draw(game.batch, "Click to start", 370, 220);
        game.batch.end();
        if (Gdx.input.isTouched()){
            game.setScreen(new InfinityLvl(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
