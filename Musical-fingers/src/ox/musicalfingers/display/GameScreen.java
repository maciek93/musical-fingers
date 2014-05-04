package ox.musicalfingers.display;

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
  		public float time;
  	
  		 public Note(int a, float b) {
  		        note = a;
  		       time = b;
  		 }
	 }
	
	Note[] song;
	float t;
	float time1;
	float time2;
	float time3;
	float time4;
	float time5;
	int i = 0;
	int score = 0;
	boolean Q;
	boolean W;
	boolean E;
	boolean R;
	boolean T;

	@Override
	public void init() {
		
		batch = new SpriteBatch();    
		circle = MusicalFingers.manager.get("assets/clef.png");
        font = new BitmapFont();
        font.setColor(Color.RED);
        t = System.nanoTime();
        song = new Note[] {new Note(1,10000f),new Note(2,15000f),new Note(3,20000f)};
		
	}

	@Override
	public void update() {
		if(i<song.length){
		if((song[i]).time<(System.nanoTime()-t)/100000-2000) {spawn(song[i].note); i++;}}
		
		if ((Gdx.input.isKeyPressed(Keys.Q)) && !Q) {
			 punish();
	 	}
		if ((Gdx.input.isKeyPressed(Keys.W)) && !W) {
			 punish();
	 	}
		if ((Gdx.input.isKeyPressed(Keys.E)) && !E) {
			 punish();
	 	}
		if ((Gdx.input.isKeyPressed(Keys.R)) && !R) {
			 punish();
	 	}
		if ((Gdx.input.isKeyPressed(Keys.T)) && !T) {
			 punish();
	 	}
		
	}
	
	public void punish() {
		score -= 50;
	}
	
	public void spawn(int x) {
		if(x==1) {time1 = 2; new GameNote(1,2000f,800f);}
		else if (x==2) {time2 = 2; new GameNote(2,2000f,800f);}
		else if (x==3) {time3 = 2; new GameNote(3,2000f,800f);}
		else if (x==4) {time4 = 2; new GameNote(4,2000f,800f);}
		else {time5 = 2; new GameNote(5,2000f,800f);}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.end();
	        batch.begin();
	        batch.setColor(Color.RED);
	        batch.draw(circle, 800, 200, 200, 200, 200, 200, 1, 1);
	        font.draw(batch, Float.toString(time1), 350, 400);
	        font.draw(batch, Float.toString(time2), 450, 400);
	        font.draw(batch, Float.toString(time3), 550, 400);
	        font.draw(batch, Float.toString(time4), 650, 400);
	        font.draw(batch, Float.toString(time5), 750, 400);
	        font.draw(batch, Float.toString((System.nanoTime()-t)/100000),850,400);
		
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
