package ox.musicalfingers.display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
	 
	 //Background texture
	 Texture rectangle;
	 
	 //Stuff for drawing background notes
	 List<Integer> noteHeights = new ArrayList<Integer>(16);
	 int count = 0;
	 Texture note1;
	 Texture note2;
	 Texture note3;
	 Texture clef;
    
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
		
		//For background
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		//For background notes
		note1 = MusicalFingers.manager.get("assets/1note.png");
		note2 = MusicalFingers.manager.get("assets/2note.png");
		note3 = MusicalFingers.manager.get("assets/3note.png");
		clef = MusicalFingers.manager.get("assets/clef.png");
		
		for(int i=0;i<16;i++) {
			noteHeights.add(new Random().nextInt(MusicalFingers.height-40));
		}
		
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
		
		//Update background images
		count++;
		if(count>noteHeights.size()*100) {
			count = 0;
		}
		
		//TODO: Move recording stuff to its own class
		    	 	
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
		
		batch.setColor(147,210,255,1);
		batch.draw(rectangle,0,0,MusicalFingers.width,MusicalFingers.height);
		
		//Some notes in the background
		for(int i=0;i<noteHeights.size();i++) {
			if(i%4==0) {
				batch.draw(clef,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),clef.getWidth()*4f,clef.getHeight()*4f);
			} else if(i%4==1) {
				batch.draw(note1,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),note1.getWidth()*4f,note1.getHeight()*4f);
			} else if(i%4==2) {
				batch.draw(note2,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),note2.getWidth()*4f,note2.getHeight()*4f);
			} else {
				//batch.draw(note3,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i));
				batch.draw(note3,MusicalFingers.width-(((i*100)+count)%(noteHeights.size()*100)),noteHeights.get(i),note3.getWidth()*4f,note3.getHeight()*4f);
			}
		}
		
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
