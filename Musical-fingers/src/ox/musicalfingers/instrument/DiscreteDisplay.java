package ox.musicalfingers.instrument;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;

public interface DiscreteDisplay {
	
	public void getNotes(boolean[] notes);
	
	public void getFingers(FingerList fingers);
	
	public void draw(SpriteBatch batch);

}
