package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    Texture  mousetrapImage, cheeseImage;
    Texture bucketImage;
    Sound errSound, finalSound;
    Music afkSound;
    SpriteBatch batch;
    OrthographicCamera camera;
    int i;
    Rectangle bucket;
    Array<Star> raindrops;
    long lastDropTime;
    String[] playGame;
    int count;
    boolean success;
    Drop game;
    TextureRegion backgroundTexture;


    public GameScreen(final Drop gam) {
        this.game = gam;
        count = 0;
        // загрузка изображений для капли и ведра, 64x64 пикселей каждый
        mousetrapImage  = new Texture(Gdx.files.internal("mousetrap.png"));
        cheeseImage  = new Texture(Gdx.files.internal("cheese.png"));

        backgroundTexture = new TextureRegion(new Texture("bg.jpg"), 0, 0, 800, 480);

        bucketImage = new Texture(Gdx.files.internal("mouse.png"));

        // загрузка звукового эффекта падающей капли и фоновой "музыки" дождя
        errSound = Gdx.audio.newSound(Gdx.files.internal("music//mistake.wav"));
        finalSound = Gdx.audio.newSound(Gdx.files.internal("music//final.wav"));
        afkSound = Gdx.audio.newMusic(Gdx.files.internal("music//rude.mp3"));
        afkSound.play();


        // создается камера и SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // создается Rectangle для представления ведра
        bucket = new Rectangle();
        // центрируем ведро по горизонтали
        bucket.x = 800 / 2 - 64 / 2;
        // размещаем на 20 пикселей выше нижней границы экрана.
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        // создает массив капель и возрождает первую

        raindrops = new Array<Star>();
        playGame = new String[]{"cheese", "cheese", "mousetrap","mousetrap","cheese","cheese",
                "cheese", "mousetrap", "cheese", "mousetrap", "cheese", "mousetrap", "mousetrap",
                "cheese", "mousetrap", "cheese", "cheese", " ", " ", "*"};
        int i = 0;
        spawnRaindrop(playGame[i]);
    }

    private void spawnRaindrop(String star) {
        if (star.equals(" ")){
            lastDropTime = TimeUtils.nanoTime();
        }
        else if (star.equals("*")){
            success = true;
            finalSound.play();
            game.setScreen(new Thx(game));
        }
        else{
        Star raindrop = new Star(star);
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();}
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
        game.font.draw(game.batch, "Stars collected " + count, 350, 460);
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for(Star raindrop: raindrops) {
            game.batch.draw(raindrop.view, raindrop.x, raindrop.y);
        }
        game.batch.end();

        // обработка пользовательского ввода
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }
        if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // убедитесь что ведро остается в пределах экрана
        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;

        // проверка, нужно ли создавать новую каплю
        if(TimeUtils.nanoTime() - lastDropTime > 800000000 && !(success)) spawnRaindrop(playGame[++i]);

        // движение капли, удаляем все капли выходящие за границы экрана
        // или те, что попали в ведро. Воспроизведение звукового эффекта
        // при попадании.
        Iterator<Star> iter = raindrops.iterator();
        while(iter.hasNext()) {
            Star raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + 32 < 0){ iter.remove();
            errSound.play();}
            if(raindrop.overlaps(bucket)) {
                iter.remove();
                count++;
            }
        }
    }

    @Override
    public void dispose() {

        // высвобождение всех нативных ресурсов
        bucketImage.dispose();
        errSound.dispose();
        afkSound.dispose();
        batch.dispose();
    }

    @Override
    public void show() {

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
}

