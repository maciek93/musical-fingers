package ox.musicalfingers.instrument.Piano;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;

import ox.musicalfingers.instrument.DiscreteDisplay;

public class Piano_FiveKey implements DiscreteDisplay{
	
	private boolean[] keys = new boolean[5];
	private FingerList fingers;
	
	Texture rectangle;
	Texture circle;
	
	public Piano_FiveKey() {
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		pixmap = new Pixmap(64,64,Format.RGBA8888);
		pixmap.setColor(1,0,0,1);
		pixmap.fillCircle( 32, 32, 32 );
		circle = new Texture( pixmap );
		pixmap.dispose();
	}

	@Override
	public void getNotes(boolean[] notes) {
		for(int i=0;i<5;i++) {
			keys[i] = notes[i];
		}
		
	}

	@Override
	public void getFingers(FingerList fingers) {
		this.fingers = fingers;
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		//Maybe not the best representation of a 5-key piano...
		batch.setColor(Color.BLUE);
		batch.draw(rectangle,385,100,510,300);
		
		for(int i=0;i<5;i++) {
			if(keys[i]) {
				batch.setColor(Color.CYAN);
			} else {
				batch.setColor(Color.GREEN);
			}
			batch.draw(rectangle,390+i*100,110,98,290);
		}
		
		for(Finger finger : fingers) {
			batch.setColor(Color.RED);
			batch.draw(circle,finger.tipPosition().getX()-32,finger.tipPosition().getY()-32);
		}
		
	}

}
