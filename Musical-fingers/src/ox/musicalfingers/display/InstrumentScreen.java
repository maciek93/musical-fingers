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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
import ox.musicalfingers.instrument.GuitarOutput;
import ox.musicalfingers.instrument.Piano.Piano_FiveKey;
import ox.musicalfingers.instrument.Random.FiveNotes;
import ox.musicalfingers.leap.GuitarListener;
import ox.musicalfingers.leap.LeapMotion;
import ox.musicalfingers.leap.PianoListener;
import ox.musicalfingers.recording.Recorder;

public class InstrumentScreen implements Screen {

	// Controller for leap motion
	Controller controller;
	// Input processor
	DiscreteInput input;
	// Turns input 'notes' into sounds
	DiscreteOutput output;
	// Displays the instrument and fingers
	DiscreteDisplay display;
	// Output for recorded notes
	DiscreteOutput recordedOutput;

	// Background texture
	Texture rectangle;

	// Stuff for drawing background notes
	List<Integer> noteHeights = new ArrayList<Integer>(16);
	int count = 0;
	Texture note1;
	Texture note2;
	Texture note3;
	Texture clef;

	// Recording & Playback stuff
	Recorder recorder = new Recorder();
	boolean recording = false;
	boolean playingBack = false;
	boolean repeatPlayback = true;
	Texture rec;
	Texture notRec;
	Texture play;
	Texture notPlay;
	
	// UI
	Stage stage;
	SelectBox instruments;
	int currentInstrument = 0;
	
	// Boolean to go back to meny
	boolean backToMenu =false;

	@Override
	public void init() {

		controller = new Controller();
		output = new FiveNotes();
		display = new Piano_FiveKey();
		input = new PianoListener(display);

		controller.addListener((Listener) input);

		recordedOutput = new FiveNotes();

		// For background
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		rectangle = new Texture(pixmap);

		// For background notes
		note1 = MusicalFingers.manager.get("assets/1note.png");
		note2 = MusicalFingers.manager.get("assets/2note.png");
		note3 = MusicalFingers.manager.get("assets/3note.png");
		clef = MusicalFingers.manager.get("assets/clef.png");

		Random rnd = new Random();

		for (int i = 0; i < 16; i++) {
			noteHeights.add(rnd.nextInt(MusicalFingers.height - 40));
		}
		
		//Ui elements
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Skin skin = MusicalFingers.manager.get("assets/ui/pixelSkin.json");
		
		skin.getFont("default").setScale(1f);
		
		/*
		Table toolbar = new Table();
		toolbar.setPosition(0, MusicalFingers.height-100f);
		toolbar.setWidth(MusicalFingers.width);
		toolbar.setHeight(100f);
		//For background of table
		pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(0.5f, 0.5f, 0.5f, 1);
		pixmap.fill();
		Texture tempTexture = new Texture(pixmap);
		TextureRegionDrawable t = new TextureRegionDrawable(new TextureRegion(tempTexture));
		toolbar.setBackground(t);		
		*/
		
		TextButton back = new TextButton("back", skin, "small");
		back.setWidth(100f);
		back.setHeight(100f);
		back.setPosition(5, MusicalFingers.height-100f-5f);
		back.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMenu=true;
			}
		}
		);

		final TextButton record = new TextButton("record", skin, "small");
		record.setWidth(150f);
		record.setHeight(100f);
		record.setPosition(MusicalFingers.width/3f, MusicalFingers.height-100f-5f);
		record.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(recording) {
					record.setText("record");
				} else {
					recorder.startRecording();
					record.setText("stop");
					
					if(instruments.getSelectionIndex() == 0) {
						//Piano
						recordedOutput = new FiveNotes();
					} else if(instruments.getSelectionIndex() == 1) {
						//Guitar
						recordedOutput = new GuitarOutput();
					}
				}
				recording = !recording;
			}
		}
		);
		
		final TextButton playButton = new TextButton("play", skin, "small");
		playButton.setWidth(150f);
		playButton.setHeight(100f);
		playButton.setPosition(MusicalFingers.width/3f+250f, MusicalFingers.height-100f-5f);
		playButton.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(playingBack) {
					playButton.setText("play");
				} else {
					recorder.startPlaying();
					playButton.setText("stop");
				}
				playingBack = !playingBack;
			}
		}
		);
		
		String[] instrumentNames = {"     PIANO", "     GUITAR"};
		
		instruments = new SelectBox(instrumentNames, skin);
		instruments.setPosition(MusicalFingers.width-255f, MusicalFingers.height-100f-5f);
		instruments.setWidth(250f);
		instruments.setHeight(100f);	
		
		stage.addActor(instruments);
		stage.addActor(playButton);
		stage.addActor(record);
		stage.addActor(back);
		
		//Ui images
		notRec = MusicalFingers.manager.get("assets/notRecording.png");
		rec = MusicalFingers.manager.get("assets/recording.png");
		notPlay = MusicalFingers.manager.get("assets/notPlaying.png");
		play = MusicalFingers.manager.get("assets/playing.png");
		
		backToMenu = false;
	
	}

	@Override
	public void update() {
		
		//Change instruments?
		if(currentInstrument != instruments.getSelectionIndex()) {
			controller.removeListener((Listener) input);
			if(instruments.getSelectionIndex() == 0) {
				//Piano
				output = new FiveNotes();
				display = new Piano_FiveKey();
				input = new PianoListener(display);
			} else if(instruments.getSelectionIndex() == 1) {
				//Guitar
				output = new GuitarOutput();
				display = new GuitarDisplay();
				input = new GuitarListener(display);
			}
			controller.addListener((Listener) input);
			currentInstrument = instruments.getSelectionIndex();
		}

		
		//Instruments 
		output.playNotes(input.getNotes());
		display.getNotes(input.getNotes());
		display.getFingers(controller.frame().fingers());
		display.getInteractionBox(controller.frame().interactionBox());

		// Update background images
		count++;
		if (count > noteHeights.size() * 100) {
			count = 0;
		}

		//Recording and playback
		
		if (recording) {
			recorder.recordNotes(input.getNotes());
		}

		if (playingBack) {
			if(!recorder.endOfRecording()) {
				recordedOutput.playNotes(recorder.playNotes());
			} else {
				if(repeatPlayback) {
					recorder.startPlaying();
				}
			}
		}
		
		//Update ui
		stage.act();
		
	}

	@Override
	public void draw(SpriteBatch batch) {

		// batch.setColor(147,210,255,1);
		// batch.draw(rectangle,0,0,MusicalFingers.width,MusicalFingers.height);

		// Some notes in the background
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

		//Draw instrument
		display.draw(batch);
		
		//Draw rectangle under toolbar
		batch.setColor(200f/255f,200f/255f,200f/255f,1);
		batch.draw(rectangle,0,MusicalFingers.height-110f,MusicalFingers.width,110f);
		batch.setColor(Color.BLACK);
		batch.draw(rectangle,0,MusicalFingers.height-115f,MusicalFingers.width,5f);
		
		batch.setColor(Color.WHITE);
		//Draw some ui images
		if(recording) {
			batch.draw(rec,MusicalFingers.width/3f-50f,MusicalFingers.height-75,40f,40f);
		} else {
			batch.draw(notRec,MusicalFingers.width/3f-50f,MusicalFingers.height-75,40f,40f);
		}
		if(playingBack) {
			batch.draw(play,MusicalFingers.width/3f+250f-50f,MusicalFingers.height-55f-33f,42f,66f);
		} else {
			batch.draw(notPlay,MusicalFingers.width/3f+250f-50f,MusicalFingers.height-55f-33f,42f,66f);
		}
		
		//Draw ui
		batch.end();
		stage.draw();
		batch.begin();

	}

	@Override
	public int changeStateTo() {
		if(backToMenu) { 
			return MusicalFingers.STATE_MENU;
		} else {
			return -1;
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
