package ox.musicalfingers.game;

import ox.musicalfingers.display.MusicalFingers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameNote {
	 public int note;
	  public float pos;
	  Texture gameNote;
	  public GameNote(int a, float b) {
		  note = a;
		  pos = b;
		  gameNote = MusicalFingers.manager.get("assets/game/GameNote.png");
	  }
	  
	  public void draw(SpriteBatch batch) {
		  switch(note) {
			case 0: 	batch.setColor(Color.RED);
						break;
			case 1:		batch.setColor(Color.BLUE);
						break;
			case 2:  	batch.setColor(Color.GREEN);
						break;
			case 3:		batch.setColor(Color.YELLOW);
						break;
			case 4:		batch.setColor(Color.MAGENTA);
						break;
			default:				throw new Error("not a valid state");
		}
		  batch.draw(gameNote,140+note*224,pos,gameNote.getWidth()*10,gameNote.getHeight()*10);
	  }
}
