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
	 //TODO: Move this stuff
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
	 //
	 
	 //Controller for leap motion
	 Controller controller;
	 //Input processor
	 DiscreteInput input;
	 //Turns input 'notes' into sounds
	 DiscreteOutput output;
	 //Displays the instrument and fingers
	 DiscreteDisplay display;
    
	 //TODO: Move this 
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
    	sound1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound1.wav"));
    	sound2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound2.wav"));
    	sound3 = Gdx.audio.newSound(Gdx.files.internal("assets/sound3.wav"));
    	sound4 = Gdx.audio.newSound(Gdx.files.internal("assets/sound4.wav"));
    	sound5 = Gdx.audio.newSound(Gdx.files.internal("assets/sound5.wav"));
    	
		
	}

	@Override
	public void update() {
		
		output.playNotes(input.getNotes());
		display.getNotes(input.getNotes());
		display.getFingers(controller.frame().fingers());
		
		if(recording) {
			playnote(input.getNotes());
		}
		
		//TODO: Move recording stuff to its own class
		    	 	
	 	if ((Gdx.input.isKeyPressed(Keys.SPACE)) && !recording && !playing) {
			 t = System.nanoTime();
			 queue = new ConcurrentLinkedQueue<Note>();
			 recording = true;
		}
		 
		if ((Gdx.input.isKeyPressed(Keys.DOWN)) && recording) {
			 song = queue.toArray(new Note[0]);
			 recording = false;
	 	}
	 
		if ((Gdx.input.isKeyPressed(Keys.UP)) && !recording && !playing) {
			 playing = true;
			 
			 timer = System.nanoTime();
			// playback(song);
	 	}
		if(playing) playback(song);
    }

	@Override
	public void draw(SpriteBatch batch) {
		
		display.draw(batch);
		
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
	
	
	public void playnote(boolean[] notes) {
    		//if (recording==true) {System.out.println("Recorded"); queue.add(new Note(x,(System.nanoTime()-t)));}
    		//s.stop(); 
    		//s.play();
		for(int i=0; i< 5;i++) {
			if(notes[i]) {
				queue.add(new Note(i,System.nanoTime()-t));
			}
		}
	 }
	 float timer = 0;
	 int i = 0;
    
    	public void playback(Note[] a) {
    		//float t = System.nanoTime();
    		int n = a.length;
    		//int i = 0;
    		int note = 0;
	    	float time = 0;
	    	if(i < n) {
    			note = (a[i]).note;
    			time = (a[i]).time;
    			if(System.nanoTime()-timer >= time) {
    				if(note==0) {sound1.stop(); sound1.play();}
        			else if(note==1)  {sound2.stop(); sound2.play();}
        			else if(note==2)  {sound3.stop(); sound3.play();}
        			else if(note==3) {sound4.stop(); sound4.play();}
        			else  {sound5.stop(); sound5.play();}
    				i++;
    			}
    			/*
    			if(note==1)  playnote(1,sound1);
    			else if(note==2)  playnote(2,sound2);
    			else if(note==3)  playnote(3,sound3);
    			else if(note==4)  playnote(4,sound4);
    			else  playnote(5,sound5);
    			i++;
    			*/
    		}
	    	
    		if(i==n) {playing = false; i = 0;}
    	}

}
