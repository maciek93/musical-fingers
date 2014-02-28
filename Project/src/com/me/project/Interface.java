package com.me.project;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Interface implements ApplicationListener {
    SpriteBatch mBatch;
    Texture mTexture, mMap;
    BitmapFont font;
    float spriteX = 50;
    float spriteY = 50;
    float speed = 50;
    float x = 0;
   
    Sound sound1;
    Sound sound2;
    Sound sound3;
    Sound sound4;
    Sound sound5;

    @Override public void create() {

        mBatch = new SpriteBatch();
        mTexture = new Texture(Gdx.files.internal("data/libgdx.png"));
        mMap = new Texture(Gdx.files.internal("data/logo.png"));  
        font = new BitmapFont();
        font.setColor(Color.RED);
        sound1 = Gdx.audio.newSound(Gdx.files.internal("data/sound1.mp3"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("data/sound2.mp3"));
        sound3 = Gdx.audio.newSound(Gdx.files.internal("data/sound3.mp3"));
        sound4 = Gdx.audio.newSound(Gdx.files.internal("data/sound4.mp3"));
        sound5 = Gdx.audio.newSound(Gdx.files.internal("data/sound5.mp3"));
    }

    @Override public void dispose() {
        mTexture.dispose();
        mMap.dispose();
        font.dispose();
    }

    @Override public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        updateInput();
        mBatch.begin();
        drawD();
        mBatch.end();
    }

    @Override public void resize(final int width, final int height) {}

    @Override public void pause() {}

    @Override public void resume() {}

    public void drawD() {
        mBatch.draw(mMap, -spriteX - (mMap.getWidth() / 2), spriteY - (mMap.getHeight() / 2));
        mBatch.draw(mTexture, 0, 0, 1024, 512);
        font.draw(mBatch, Integer.toString(Gdx.input.getX(0)), 200, 200);
    }

    public void updateInput() {
       /* if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spriteX += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spriteX -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            spriteY += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	spriteY -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	if(spriteX<200)  sound1.play();
        	else if(spriteX<400) sound2.play();
        	else if(spriteX<600) sound3.play();
        	else if(spriteX<800) sound4.play();
        	else sound5.play();
        }*/
    	x = Gdx.input.getX(0);
    	 if (Gdx.input.isTouched()) {
         	if(x<190)  sound1.play();
         	else if(x<414) sound2.play();
         	else if(x<604) sound3.play();
         	else if(x<817) sound4.play();
         	else sound5.play();
    	 }
    }
}