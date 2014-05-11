package ox.musicalfingers.instrument.drum;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteInputDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class Drum extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[5];
	
	Texture drumImg;
	Texture fingerPoint;
	Texture rectangle;
	
	int sF = 1;
	
	PointableList pointableList;
	InteractionBox iBox;
	
	boolean isAbove = false; // Reflects whether the active drumstick was above the drum skin when last checked.
	static final Vector midPoint = new Vector(0.5f, 0.5f, 0.5f) ;
	
	public Drum() {
		
		drumImg = MusicalFingers.manager.get("assets/drum.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		//ScaleFactor
		sF =  MusicalFingers.width/drumImg.getWidth();
		sF -= 2;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized Drum");
        pointableList = controller.frame().pointables();
    }
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	
		for (int i = 0; i < 5; i++) {
			notes[i] = false; // Stop starting previous sound.
		}
    	
    	if (iBox.isValid()) {
	    	pointableList = controller.frame().pointables();
	    	if (pointableList.count() > 0) {
	    		Pointable pointable = lowestPointable(pointableList); // gets the pointable with the lowest y value.
	    		Vector pointablePos = vectorMinus ( iBox.normalizePoint(pointable.tipPosition(),false) , midPoint ) ;
	    		if (pointablePos.getY() < 0 && isAbove) {
	    			isAbove = false; // you've gone below the drum.
		    		// radius is how far from the centre the drum was hit.
		    		double radius = Math.sqrt(Math.pow(pointablePos.getX(), 2.0) + Math.pow(pointablePos.getZ(), 2.0));
		    		if (radius < 0.5) { // if you hit the actual drum
		    			// This is currently a would be nice; scale volume according to how hard the drum was hit.
		    			float volume = - pointable.tipVelocity().getY() / 4500f; 
		    			// speed of impact = downward velocity, normalized to a fast drumstick impact speed.
		    			volume = Math.min(1.0f, volume);
		    			if ( volume > 0 ) notes[(int) (10*radius)] = true; // start sound from impact.
		    		}
	    		} else if (pointablePos.getY() > 0) {
	    			isAbove = true; // You've gone above the drum.
	    		}
	    	}
    	}	
    }

	@Override
	public boolean[] getNotes() {
		return notes;
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		batch.setColor(Color.WHITE);
		// draw drum background
		// drum centre at left + half width, top + half height
		// x: MusicalFingers.width/2f
		// y: MusicalFingers.height/6f + drumImg.getHeight()*sF / 2f
		
		batch.draw(drumImg, MusicalFingers.width/2f - drumImg.getWidth()/2f*sF, MusicalFingers.height/6f,drumImg.getWidth()*sF,drumImg.getHeight()*sF);

		// circle size = 7/8 of image height. 
		Pointable pointable = lowestPointable(pointableList);
		if (null != iBox && iBox.isValid()) {
		// pointablePos returns a vector indicating the position of the pointable tip such that
		// 	(0,0,0) is the centre of the interaction box, range from -0.5 to 0.5 in all dimensions
		Vector pointablePos = vectorMinus ( iBox.normalizePoint(pointable.tipPosition(),false) , midPoint ) ;
		// batch.draw (fingerPoint, -half Circle Width + centre of image + proportional offset on drum, Similar for height , Circle Width, Circle Height)
		batch.draw(fingerPoint,-6f + (MusicalFingers.width/2f) + (pointablePos.getX() * drumImg.getHeight()*sF * 7f / 8f), -6f + (MusicalFingers.height/6f + drumImg.getHeight()*sF / 2f) - (pointablePos.getZ() * drumImg.getHeight()*sF * 7f / 8f) ,12,12);
		}
		
		// indication of hit point?
		
	}

	// SHOULD BE REPLACED WITH BUILT IN - Operator. 
	// Eclipse thinks this doesn't exist.
	private Vector vectorMinus( Vector minuend, Vector subtrahend ) {
		return new Vector(minuend.getX() - subtrahend.getX(), minuend.getY() - subtrahend.getY(), minuend.getZ() - subtrahend.getZ());
	}
	private Pointable lowestPointable(PointableList PL){
		assert (PL.count() > 0);
		Pointable low = PL.frontmost();
		float lowPos = low.tipPosition().getY();
		for (Pointable p : PL) {
			if (p.tipPosition().getY() < lowPos) {
				low = p;
				lowPos = p.tipPosition().getY();
			}
		}
		return low;
	}
}
