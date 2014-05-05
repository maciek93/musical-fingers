package ox.musicalfingers.display;

import java.util.ArrayList;
import java.util.List;

import ox.musicalfingers.game.GameNote;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen{
	  private SpriteBatch batch;
	  private BitmapFont font;
	  private Texture circle;	  
	  public class Note {
  		public int note;
  		public int time;
  	
  		 public Note(int a, int b) {
  		        note = a;
  		       time = b;
  		 }
	 }
	
	Note[] song;
	float t;
	int time = 0;
	int i = 0;
	int score = 0;
	boolean Q;
	boolean W;
	boolean E;
	boolean R;
	boolean T;
	Texture piano;
	
	List<GameNote> gameNotes = new ArrayList<GameNote>();

	@Override
	public void init() {
		
		batch = new SpriteBatch();    
        font = new BitmapFont();
        font.setColor(Color.RED);
        t = System.nanoTime();
        song = new Note[] {new Note(2,30),new Note(2,60),new Note(2,90),new Note(0,120),new Note(1,150),new Note(1,180),new Note(0,210),new Note(4,270),new Note(4,300),new Note(3,330),new Note(3,360),new Note(2,390),new Note(0,480)};
        piano = MusicalFingers.manager.get("assets/game/game_piano.png");
		
	}

	@Override
	public void update() {
		for(GameNote note:gameNotes) {
			note.pos += 10;
		}
		time++;
		if(i<song.length){
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
	    for(GameNote note:gameNotes) {
			note.draw(batch);
		}
		
	}

	@Override
	public int changeStateTo() {
		return -1;
	}

	@Override
	public void dispose() {
		 batch.dispose();
	        font.dispose();
	}

}
