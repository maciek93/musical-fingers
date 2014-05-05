package ox.musicalfingers.game;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteInputDisplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class GamePianoInput extends Listener {
	
	private boolean[] notes = new boolean[5];
	boolean[] pressed = new boolean[5];
	Rectangle[] keys = new Rectangle[5];
	
	Texture fingerPoint;
	
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	public GamePianoInput() {
		//Define keys in normalised InteractionBox co-ords
		for(int i=0;i<5;i++) {
			keys[i] = new Rectangle(i*(1f/5f),0f,(1f/5f),1f);
		}
		
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		sF = 7;
		
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized GamePiano");
        fingerList = controller.frame().fingers();
    }
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	for(Hand hand : controller.frame().hands()) {
    		Vector palmPos = hand.palmPosition();
    		for(Finger finger : hand.fingers()) {
    			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
    			
    			for(int i=0;i<5;i++) {
	    			if (keys[i].contains(fingerPos.getX(), fingerPos.getZ()) &&
	    				(iBox.normalizePoint(palmPos,false).getY() - fingerPos.getY() > 0.001f)) {
	    				if (notes[i] = false) {
	    					pressed[i] = true;
	    				}
	    				notes[i]=true;
	    			} else {
	    				notes[i]=false;
	    			}
    			}
    		}
    	}
    	fingerList = controller.frame().fingers();
    }

	public boolean[] getPressed() {
		return pressed;
	}
	
	public void draw(SpriteBatch batch) {

		for(Finger finger: fingerList) {
			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
			batch.draw(fingerPoint,-6f+80+160f*sF*fingerPos.getX(),-6f+400+((1f-fingerPos.getZ())*40)*sF,12,12);
		}
		
	}

}