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
import com.leapmotion.leap.*;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class Drum extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[5];
	
	Texture drumImg;
	Texture fingerPoint;
	Texture rectangle;
	
	float scaleFactor=1f;
	int sF = 1;
	int timeout=10;
	float Radius=0.5f;
	
	HandList hands;
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
        System.out.println("Initialized Piano");
        pointableList = controller.frame().pointables();
        hands = controller.frame().hands();
    	iBox = controller.frame().interactionBox();
    }
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	timeout-=1;
		for (int i = 0; i < 5; i++) {
			notes[i] = false; // Stop starting previous sound.
		}
    	
    	if (iBox.isValid()) {
    	    hands =controller.frame().hands();
	    	pointableList = controller.frame().pointables();
	    		for (Hand hand : hands) { 
	    			Vector palmPos =iBox.normalizePoint(hand.palmPosition(),false).minus(midPoint);
	    			Vector palmVel =hand.palmVelocity();

	    			
	    			if (palmVel.getY()>150){timeout=0;}
		    		if (palmVel.getY()<-100 &&timeout<=0) {
		    			isAbove = false; // you've gone below the drum.
			    		// radius is how far from the centre the drum was hit.
			    		double radius = Math.sqrt(Math.pow(palmPos.getX(), 2.0) + Math.pow(palmPos.getZ(), 2.0));
			    		if (radius <Radius) { // if you hit the actual drum
			    			// This is currently a would be nice; scale volume according to how hard the drum was hit.
			    			float volume = - palmVel.getY() / 1000f; 
			    			// speed of impact = downward velocity, normalized to a fast drumstick impact speed.
			    			volume = Math.min(1.0f, volume);
			    			if ( volume > 0 ) notes[(int) (10*radius)] = true; 
			    			timeout=10;// start sound from impact.
			    		}

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

		
		batch.draw(drumImg, MusicalFingers.width/2f - drumImg.getWidth()/2f*sF, MusicalFingers.height/6f,drumImg.getWidth()*sF,drumImg.getHeight()*sF);
		
		float mid=0;
		for(Hand hand : hands)	{mid =hand.palmPosition().getX() +mid;}
		mid =mid/2;
		// circle size = 7/8 of image height. 
		for(Hand hand : hands)	{ 
				Vector palmPos = iBox.normalizePoint(hand.palmPosition(),false).minus(midPoint).times(scaleFactor);
				batch.draw(fingerPoint,-6f + (MusicalFingers.width/2f) + (palmPos.getX() * drumImg.getHeight()*sF * 7f / 8f), -6f + (MusicalFingers.height/6f + drumImg.getHeight()*sF / 2f) - (palmPos.getZ() * drumImg.getHeight()*sF * 7f / 8f) ,12,12);
				//batch.draw(fingerPoint,-12f+MusicalFingers.width/2f - (drumImg.getWidth()/2f*sF) +drumImg.getWidth()*sF*palmPos.getX(),-12f+MusicalFingers.height/6f+(6+64*(1f-1f*palmPos.getZ()))*sF,24,24);
				/*if (hands.count()==1) {
					batch.setColor(Color.WHITE);
					batch.draw(rectangle, MusicalFingers.width/2f - 5*10f*sF+sF*25, 49 + palmPos.getY() * 5 * sF,50*sF,sF );
				}
				else if (hand.palmPosition().getX()>mid ) {
					batch.setColor(Color.RED);
					batch.draw(rectangle, MusicalFingers.width/2f - 5*10f*sF+sF*50, 49 + palmPos.getY() * 5 * sF,50*sF,sF );
				}
				else {
					batch.setColor(Color.BLUE);
					batch.draw(rectangle, MusicalFingers.width/2f - 5*10f*sF, 49 + palmPos.getY() * 5 * sF,50*sF,sF );
				} */

		
		}

		// indication of drum level.	
		//batch.setColor(Color.WHITE);
		//batch.draw(rectangle,MusicalFingers.width/2f - 5*10f*sF,49,100*sF,sF);
		
		
	}

	// SHOULD BE REPLACED WITH BUILT IN - Operator. 
	// Eclipse thinks this doesn't exist.
	private Vector vectorMinus( Vector minuend, Vector subtrahend ) {
		return new Vector(minuend.getX() - subtrahend.getX(), minuend.getY() - subtrahend.getY(), minuend.getZ() - subtrahend.getZ());
	}
}
