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
	//private int i;     //init 0
	private int currentNote;
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
	
	// Boolean to replay song from begining
	boolean toReplay =false;
	
	//Boolean to pause game
	boolean pause= false;
	
	//TextButton scorer;
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
		
	//Notespeed 
	float noteSpeed = 8f;
	
    //Vars for playing back music
	 FiveNotes musicPlayer;

	//Vars to tell what notes are currently being played out loud
	 boolean [] pressed;
	 
	 //cheerMessage
	 private String cheerMessage;
	 
	 //anything to cheer?
	 private boolean cheerBool;


	@Override
	public void init() {
		
		controller = new Controller();
		
		//init variables
		 time = -80;
		 currentNote = 0;
		 score = 0; 
		 winScreen=false;
		 loseScreen=false;
		 gameNotes.clear();
		 cheerMessage="";
		 cheerBool=false;
		 pressed= new boolean []{false,false,false,false,false};
		
		batch = new SpriteBatch();    
        font = new BitmapFont();
        font.setColor(Color.RED);
        t = System.nanoTime();
        song = new Note[] {new Note(2,30),new Note(2,60),new Note(2,90),new Note(0,120),new Note(1,150),new Note(1,180),new Note(0,210),new 
        		Note(4,270),new Note(4,300),new Note(3,330),new Note(3,360),new Note(2,390),new Note(0,480)};
		songs = new Note [] [] {song,song,song,song,song,song,song,song,song};
		musicPlayer = new FiveNotes();
		
        piano = MusicalFingers.manager.get("assets/game/game_piano.png");
        		
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
		

		
        TextButton back = new TextButton("back", skin, "small");
		back.setWidth(100f);
		back.setHeight(100f);
		back.setPosition(5, MusicalFingers.height-105f);
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
		replayer.setPosition(MusicalFingers.width/2f+50f, MusicalFingers.height-105f);
		replayer.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				toReplay=true;
			}
		}
		);
		
		final TextButton pauseButton = new TextButton("play", skin, "small");
		pauseButton.setWidth(150f);
		pauseButton.setHeight(100f);
		pauseButton.setPosition(MusicalFingers.width/2f-50f-150f, MusicalFingers.height-105f);
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
		playList.setPosition(MusicalFingers.width-255f , MusicalFingers.height-105f);
		playList.setWidth(250f);
		playList.setHeight(100f);
		

		stager.addActor(playList);
		stager.addActor(pauseButton);
		stager.addActor(replayer);
		stager.addActor(back);
		
		backToMenu = false;
		pause = true;
        
        input = new GamePianoInput();
        
        controller.addListener(input);
        
        font = MusicalFingers.manager.get("assets/font/pixel.fnt");
		
	}
	


	@Override
	public void update() {
		
		//replay song?
		if (toReplay) {
			 time = -80;
			 currentNote = 0;
			 score = 0; 
			toReplay=false;
		}
		
		//Change song?
		if(currentSong != playList.getSelectionIndex()) { 
					time=-80;
					currentNote=0;
					score=0;
					currentSong=playList.getSelectionIndex();
					song= songs[currentSong];
					
		}
			
		if (!pause) {
			//score++;
			time++;
			
			if(currentNote>=song.length) {
				//End of song
				
				if(score >= 0) {
					winScreen = true;
				} else {
					loseScreen = true;
				}
				
			}
			
			if(currentNote<song.length){
				//Not end of song	
				
				//Add the note at the correct time
				if((song[currentNote]).time<(time)) {
					spawn(song[currentNote].note); currentNote++;
				}
				
		
				for(int i=0;i<5;i++) {
					
					
					if(input.getPressed()[i]){
						boolean wasNote =false;
						for(int j=0;j<gameNotes.size();j++) {
							GameNote note = gameNotes.get(j);
							
							if( input.getBounds(i).contains(note.bounds())) {
								//Note is inside the bounds
								
								reward(note.getX(), note.getY(),1f);
								input.getPressed()[i]=false;
								gameNotes.remove(j);
								j--;
								wasNote = true;
							} else if (input.getBounds(i).overlaps(note.bounds())) {
								//Note on ede of key
								reward(note.getX(), note.getY(),0.5f);
								input.getPressed()[i]=false;
								gameNotes.remove(j);
								j--;
								wasNote = true;
							} 
							
						}
						
						input.getPressed()[i]=false;
						
						if(!wasNote) {
							punish((int)(input.getBounds(i).x+input.getBounds(i).width/2f),(int)(input.getBounds(i).y+input.getBounds(i).height/2f),0.5f);
						}
					}

				}
				
				// play the sounds of the notes
				boolean [] tobePlayed = new  boolean[5]; 
				for(int i=0;i<gameNotes.size();i++) {
					GameNote note = gameNotes.get(i);
					
					// changes to keys that have been pressed
					for(int j=0;j<5;j++) { 
					//if( input.getBounds(i).contains(note.bounds())) {
      			    if( input.getBounds(j).y > note.getY() && input.getBounds(j).y < note.getY()+5 
      			    		&& input.getBounds(j).x < note.getX() && input.getBounds(j).x+40> note.getX() ) {
						  if (!pressed[j]) {
							  tobePlayed[j]=true;
							  pressed[j]=true;
						  }
					  }
					  else {
						pressed[j] = false;
						tobePlayed[j]=false;
					  }	  
					  

					}
					musicPlayer.playNotes1(tobePlayed);

				}

				
				
				for(int i=0;i<gameNotes.size();i++) {
					GameNote note = gameNotes.get(i);
					

					
					note.pos += noteSpeed;
					if(note.pos>=MusicalFingers.height-110f) {
						//Missed note
						punish(note.getX(), note.getY(),1f);
						gameNotes.remove(i);
						i--;
					}
				}
				

				
				
			}
				
		
			/*
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
		 	*/
				
			
		} else {
			for(int i=0;i<5;i++) {
				//input.getPressed()[i]=false;
			}
		}
		stager.act();
		
	}
	
	public void reward(int x, int y, float multiplier) {
		if (cheerMessage == "WOOH!") {cheerMessage= "";} else {cheerMessage = "WOOH!";}
		
		//Graphical affects will go here
		
		
		score +=250*multiplier;
	}
	
	public void punish(int x, int y, float multiplier) {
		if (cheerMessage == "BOO !") {cheerMessage= "";} else {cheerMessage = "BOO !";}

		//Graphical affects will go here
		
		
		score -= 500*multiplier;
	}
	
	public void spawn(int x) {
		gameNotes.add(new GameNote(x,-110f));
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
	    //batch.draw(piano,80,400,1120,240);
	    input.draw(batch);
	    for(GameNote note:gameNotes) {
			note.draw(batch);
		}
	    
	    
	    
	    //Draw score here
	    if (cheerMessage =="WOOH!") {
	    	//batch.setColor(Color.WHITE);
	    	//batch.draw(rectangle,MusicalFingers.width/2-200,50,400, 100);
	    	font.setColor(1, 1,1,1);
	    }
	    else {
	    	//batch.setColor(Color.RED);
	    	//batch.draw(rectangle,MusicalFingers.width/2-200,50,400,100);
	    	font.setColor(1,0,0,1);
	    	}
		font.setScale(2.5f);
		font.draw(batch, cheerMessage, MusicalFingers.width/2-85,150);
	    
		
		
		//Draw the Punish/ reward box here 
	    //Draw score here
	    font.setColor(1,1,1,1);
		//font.setScale(1,-1);
	    font.setScale(1f);
		font.draw(batch, "SCORE: "+score, MusicalFingers.width-400,30);
		
		
	    
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
