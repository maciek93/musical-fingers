package ox.musicalfingers.display;

import java.util.ArrayList;
import java.util.List;

import ox.musicalfingers.game.GameNote;
import ox.musicalfingers.game.GamePianoInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Controller;
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
import ox.musicalfingers.instrument.DiscreteInputDisplay;
import ox.musicalfingers.instrument.DiscreteOutput;
import ox.musicalfingers.instrument.GuitarOutput;
import ox.musicalfingers.instrument.Piano.Piano;
import ox.musicalfingers.instrument.Piano.Piano_FiveKey;
import ox.musicalfingers.instrument.Random.FiveNotes;
import ox.musicalfingers.instrument.guitar.Guitar;
import ox.musicalfingers.leap.GuitarListener;
import ox.musicalfingers.leap.LeapMotion;
import ox.musicalfingers.leap.PianoListener;
import ox.musicalfingers.recording.Recorder;



public class GameScreen implements Screen{
	  private SpriteBatch batch;
	  private BitmapFont font;

	  public class Note {
  		public int note;
  		public int time;
  	
  		 public Note(int a, int b) {
  		       note = a;
  		       time = b;
  		 }
	 }
	
	//Game Variables  
	Note[] song;
	float t;
	private int time;  //init 0
	private int i;     //init 0
	public static int score =0;  //init 0
	boolean Q;
	boolean W;
	boolean E;
	boolean R;
	boolean T;
	Texture piano;
	
	GamePianoInput input;
	
	List<GameNote> gameNotes = new ArrayList<GameNote>();
	
	//Controller for the Leap Motion
	Controller controller;
	
	// Background texture
	Texture rectangle;
	
	// Boolean to go to Winning SCreen after Game
	boolean winScreen= false;
	
	// Boolean to go to Losing SCreen after Game
	boolean loseScreen= false;
	
	// Boolean to go back to menu
	boolean backToMenu =false;
	
	// Boolean to go back to menu
	boolean toReplay =false;
	
	//Boolean to pause game
	boolean pause= false;
	
	TextButton scorer;
	TextButton pauseButton;
	
	// UI
	Stage stager;
	// Recording & Playback stuff
		Texture timer;
		
		// UI
		SelectBox playList;
		int currentSong = 0;
	
		//Array of possible songs
		Note[] []  songs;

	@Override
	public void init() {
		 
		controller = new Controller();
		
		//init variables
		 time = -80;
		 i = 0;
		 score = 0; 
		 winScreen=false;
		 loseScreen=false;
		
		batch = new SpriteBatch();    
        font = new BitmapFont();
        font.setColor(Color.RED);
        t = System.nanoTime();
        song = new Note[] {new Note(2,30),new Note(2,60),new Note(2,90),new Note(0,120),new Note(1,150),new Note(1,180),new Note(0,210),new 
        		Note(4,270),new Note(4,300),new Note(3,330),new Note(3,360),new Note(2,390),new Note(0,480)};
		songs = new Note [] [] {song,song,song,song,song,song,song,song,song};
        
        piano = MusicalFingers.manager.get("assets/game/game_piano.png");
        int offsetLeft =83;
        		
		// For background
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		rectangle = new Texture(pixmap);

        
		//Ui elements
		stager = new Stage();
		Gdx.input.setInputProcessor(stager);
		
		Skin skin = MusicalFingers.manager.get("assets/ui/pixelSkin.json");
		skin.getFont("default").setScale(1f);
        
		
		scorer = new TextButton("score: "+score, skin, "small");
		scorer.setWidth(250);
		scorer.setHeight(100f);
		scorer.setPosition(offsetLeft, MusicalFingers.height-105f);
		
		
        TextButton back = new TextButton("back", skin, "small");
		back.setWidth(100f);
		back.setHeight(100f);
		back.setPosition(300+offsetLeft, MusicalFingers.height-105f);
		back.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToMenu=true;
			}
		}
		);
		
		TextButton replayer = new TextButton("replay", skin, "small");
		replayer.setWidth(150f);
		replayer.setHeight(100f);
		replayer.setPosition(650+offsetLeft, MusicalFingers.height-105f);
		replayer.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				toReplay=true;
			}
		}
		);
		
		final TextButton pauseButton = new TextButton("pause", skin, "small");
		pauseButton.setWidth(150f);
		pauseButton.setHeight(100f);
		pauseButton.setPosition(450+offsetLeft, MusicalFingers.height-105f);
		pauseButton.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!pause) {
					pauseButton.setText("play");
				} else {
					// method recorder.startPlaying();
					pauseButton.setText("pause");
				}
				pause = !pause;
			}
		}
		);
		
	
		String[] songNames = {"     SONG  1"	, "     SONG  2" , "     SONG  3" , "     SONG  4" , "     SONG  5"	, "     SONG  6" , "     SONG  7" , "     SONG  8" , "     SONG  9"}	;					
		
		playList = new SelectBox (songNames, skin);
		playList.setPosition(850+offsetLeft , MusicalFingers.height-105f);
		playList.setWidth(250f);
		playList.setHeight(100f);
		

		stager.addActor(playList);
		stager.addActor(pauseButton);
		stager.addActor(replayer);
		stager.addActor(back);
		stager.addActor(scorer);
		
		backToMenu = false;
        
        input = new GamePianoInput();
        
        controller.addListener(input);
		
	}
	


	@Override
	public void update() {
		
		//replay song?
		if (toReplay) {
			 time = -80;
			 i = 0;
			 score = 0; 
			toReplay=false;
		}
		
		//Change instruments?
		if(currentSong != playList.getSelectionIndex()) { 
					time=-80;
					i=0;
					score=0;
					currentSong=playList.getSelectionIndex();
					song= songs[currentSong];
					
		}
			
		if (!pause) {
			for(GameNote note:gameNotes) {
				note.pos += 10;
			}
			
			
			score++;
			time++;
			
			if(i>=song.length) {
				winScreen=true;
				
			}

			
			if (score<0){loseScreen=true;}
			
			if(i<song.length){
					
				
			//System.out.println(new int [] {i,song.length});
			if((song[i]).time<(time)) {spawn(song[i].note); i++;}}
			
	
			if ((Gdx.input.isKeyPressed(Keys.Q)) && !Q) {
				if(Q) {
					reward();
				} else {
					punish();
				}
		 	}
			if ((Gdx.input.isKeyPressed(Keys.W)) && !W) {
				if(W) {
					reward();
				} else {
					punish();
				}
		 	}
			if ((Gdx.input.isKeyPressed(Keys.E)) && !E) {
				if(E) {
					reward();
				} else {
					punish();
				}
		 	}
			if ((Gdx.input.isKeyPressed(Keys.R)) && !R) {
				if(R) {
					reward();
				} else {
					punish();
				}
		 	}
			if ((Gdx.input.isKeyPressed(Keys.T)) && !T) {
				if(T) {
					reward();
				} else {
					punish();
				}
		 	}
			
			
		}
		stager.act();
		
	}
	
	public void reward() {
		score +=250;
	}
	
	public void punish() {
		score -= 500;
	}
	
	public void spawn(int x) {
		gameNotes.add(new GameNote(x,-110f));
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
	    batch.draw(piano,80,400,1120,240);
	    input.draw(batch);
	    for(GameNote note:gameNotes) {
			note.draw(batch);
		}
	    
	    
	    scorer.setText("score: "+score);
		//Draw rectangle under toolbar
		batch.setColor(200f/255f,200f/255f,200f/255f,1);
		batch.draw(rectangle,0,MusicalFingers.height-110f,MusicalFingers.width,110f);
		batch.setColor(Color.BLACK);
		batch.draw(rectangle,0,MusicalFingers.height-115f,MusicalFingers.width,5f);
		
		batch.end();
		stager.draw();
		batch.begin();


	  	
		
	}
	
	


	@Override
	public int changeStateTo() {
		if(backToMenu) { 
			return MusicalFingers.STATE_MENU;
		} else if (winScreen){
			return MusicalFingers.STATE_WIN;
		} else if (loseScreen){
			return MusicalFingers.STATE_LOSE;
		}
		else {
			return -1;
		}
	}

	@Override
	public void dispose() {
		 batch.dispose();
	        font.dispose();
	        stager.dispose();
	}

}
