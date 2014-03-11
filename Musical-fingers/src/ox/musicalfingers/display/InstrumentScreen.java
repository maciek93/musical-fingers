package ox.musicalfingers.display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import ox.musicalfingers.instrument.DiscreteDisplay;
import ox.musicalfingers.instrument.DiscreteInput;
import ox.musicalfingers.instrument.DiscreteOutput;
import ox.musicalfingers.instrument.Piano.Piano_FiveKey;
import ox.musicalfingers.instrument.Random.FiveNotes;
import ox.musicalfingers.leap.LeapMotion;
import ox.musicalfingers.leap.PianoListener;

public class InstrumentScreen implements Screen {
	 SpriteBatch batch;
	 Texture texture;
	 BitmapFont font;
	
	 boolean[] taps;
	 float x = 0;
	 boolean recording = false;
	 boolean playing = false;
	 Note[] song;
	 ConcurrentLinkedQueue<Note> queue;
	 float t;
    
	 Sound sound1;
	 Sound sound2;
	 Sound sound3;
	 Sound sound4;
	 Sound sound5;
	 
	 Controller controller;
	 DiscreteInput input;
	 DiscreteOutput output;
	 DiscreteDisplay display;
    
	 public class Note {
    		public int note;
    		public float time;
    	
    		 public Note(int a, float b) {
    		        note = a;
    		       time = b;
    		 }
	 }
	 
	@Override
	public void init() {

		controller = new Controller();
		input = new PianoListener();
		output = new FiveNotes();
		display = new Piano_FiveKey();
		
		controller.addListener((Listener) input);
		
		//TEMP 
		//For recording
    	sound1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound1.mp3"));
    	sound2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound2.mp3"));
    	sound3 = Gdx.audio.newSound(Gdx.files.internal("assets/sound3.mp3"));
    	sound4 = Gdx.audio.newSound(Gdx.files.internal("assets/sound4.mp3"));
    	sound5 = Gdx.audio.newSound(Gdx.files.internal("assets/sound5.mp3"));
    	
		
	}

	@Override
	public void update() {
		
		output.playNotes(input.getNotes());
		display.getNotes(input.getNotes());
		display.getFingers(controller.frame().fingers());
		
		//TODO: Move recording stuff to its own class
		
		/*System.out.println("-");
		for(int i = 0; i < 5; i++) {
			if(taps[i]) {System.out.println(taps[i]);};
		}*/
		
		/*
		x = Gdx.input.getX(0);
		
			for(int i = 0; i < 10; i++) {
				if(taps[0])  playnote(1,sound1);
         		else if(taps[1])  playnote(2,sound2);
         		else if(taps[2])  playnote(3,sound3);
         		else if(taps[3])  playnote(4,sound4);
         		else if(taps[4])  playnote(5,sound5);
			}
		*/
		
    	 	/*if (Gdx.input.justTouched() && playing==false) {
         		if(x<205)  playnote(1,sound1);
         		else if(x<410)  playnote(2,sound2);
         		else if(x<615)  playnote(3,sound3);
         		else if(x<820)  playnote(4,sound4);
         		else  playnote(5,sound5);
    	 	}*/
    	 	
    	 	if ((Gdx.input.isKeyPressed(Keys.SPACE)) && recording==false && playing==false) {
    			 t = System.nanoTime();
    			 queue = new ConcurrentLinkedQueue<Note>();
    			 recording = true;
    		 }
    		 
    		 if ((Gdx.input.isKeyPressed(Keys.DOWN)) && recording==true) {
    			 song = queue.toArray(new Note[0]);
    			 recording = false;
    	 	}
    	 
    		 if ((Gdx.input.isKeyPressed(Keys.UP)) && recording==false) {
    			 playing = true;
    			 playback(song);
    	 	}
    	}

	@Override
	public void draw(SpriteBatch batch) {
		
		
		display.draw(batch);
		
		/*
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//updateInput();
        	//batch.begin();
        	batch.draw(texture, 0, 0, 1280, 800);
        	font.draw(batch, Integer.toString(Gdx.input.getX(0)), 200, 200);
        	//batch.end();
        	 * 
        	 */

	}

	@Override
	public int changeStateTo() {
		return -1;
	}

	@Override
	public void dispose() {
		texture.dispose();
		font.dispose();
	}
	
	
	public void playnote(int x , Sound s) {
    		if (recording==true) queue.add(new Note(x,(System.nanoTime()-t)));
    		s.stop(); 
    		s.play();
	 }
	 
    
    	public void playback(Note[] a) {
    		float t = System.nanoTime();
    		int n = a.length;
    		int i = 0;
    		int note = 0;
	    	float time = 0;
	    	while(i < n) {
    			note = (a[i]).note;
    			time = (a[i]).time;
    			while(System.nanoTime()-t < time) {
    			}
    			if(note==1)  playnote(1,sound1);
    			else if(note==2)  playnote(2,sound2);
    			else if(note==3)  playnote(3,sound3);
    			else if(note==4)  playnote(4,sound4);
    			else  playnote(5,sound5);
    			i++;
    		}
    		playing = false;
    	}

}
