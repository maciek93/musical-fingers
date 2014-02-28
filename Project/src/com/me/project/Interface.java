package com.me.project;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.Sound;

public class Interface implements ApplicationListener {
    OrthographicCamera mCamera;
    SpriteBatch mBatch;
    Texture mTexture, mMap;
    float spriteX, spriteY, speed = 50;
   /* Sound sound1 = Gdx.audio.newSound(Gdx.files.internal("data/sound1.mp3"));
    Sound sound2 = Gdx.audio.newSound(Gdx.files.internal("data/sound2.mp3"));
    Sound sound3 = Gdx.audio.newSound(Gdx.files.internal("data/sound3.mp3"));
    Sound sound4 = Gdx.audio.newSound(Gdx.files.internal("data/sound4.mp3"));
    Sound sound5 = Gdx.audio.newSound(Gdx.files.internal("data/sound5.mp3"));*/
    
    final float CAMERA_WIDTH = 1280, CAMERA_HEIGHT = 800;

    @Override public void create() {
        mCamera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);

        mBatch = new SpriteBatch();
        mTexture = new Texture(Gdx.files.internal("data/logo.png"));
        mMap = new Texture(Gdx.files.internal("data/libgdx.png"));
    }

    @Override public void dispose() {
        mTexture.dispose();
        mMap.dispose();
    }

    @Override public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        updateInput();
        mCamera.update();
        mBatch.setProjectionMatrix(mCamera.combined);
        mBatch.begin();
        drawD();
        mBatch.end();
    }

    @Override public void resize(final int width, final int height) {}

    @Override public void pause() {}

    @Override public void resume() {}

    public void drawD() {
        mBatch.draw(mMap, -spriteX - (mMap.getWidth() / 2), spriteY - (mMap.getHeight() / 2));
        mBatch.draw(mTexture, -32, -32, 64, 64);
    }

    public void updateInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spriteX -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spriteX += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            spriteY -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	spriteY += speed;
        }
        /*if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	if(spriteX<250) {
        		sound1.play();
        	}
        	else sound2.play();
        }*/
       
    }
}