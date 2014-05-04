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
	float time0;
	float time1;
	float time2;
	float time3;
	float time4;
	int time = 0;
	int i = 0;
	int score = 0;
	boolean Q;
	boolean W;
	boolean E;
	boolean R;
	boolean T;
	
	List<GameNote> gameNotes = new ArrayList<GameNote>();

	@Override
	public void init() {
		
		batch = new SpriteBatch();    
		circle = MusicalFingers.manager.get("assets/clef.png");
        font = new BitmapFont();
        font.setColor(Color.RED);
        t = System.nanoTime();
        song = new Note[] {new Note(0,60),new Note(1,120),new Note(2,180),new Note(3,240),new Note(4,300)};
		
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
	        batch.setColor(Color.RED);
	        batch.draw(circle, 800, 200, 200, 200, 200, 200, 1, 1);
	        font.draw(batch, Float.toString(time0), 350, 400);
	        font.draw(batch, Float.toString(time1), 450, 400);
	        font.draw(batch, Float.toString(time2), 550, 400);
	        font.draw(batch, Float.toString(time3), 650, 400);
	        font.draw(batch, Float.toString(time4), 750, 400);
	        font.draw(batch, Float.toString((System.nanoTime()-t)/100000),850,400);
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
