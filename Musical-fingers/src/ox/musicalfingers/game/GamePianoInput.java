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
	private boolean[] prevNotes = new boolean[5];
	boolean[] pressed = new boolean[5];
	Rectangle[] keys = new Rectangle[5];
	Rectangle[] bounds = new Rectangle[5];
	
	Texture fingerPoint;
	
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	Texture piano;
	Texture rectangle;
	
	float pianoLevel =0.5f;
	
	public GamePianoInput() {
		
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		piano = MusicalFingers.manager.get("assets/game/game_piano.png");
		
		//Looks better this way
		sF = 1280/piano.getWidth() - 3;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		
		//Define keys in normalised InteractionBox co-ords
		for(int i=0;i<5;i++) {
			keys[i] = new Rectangle(i*(1f/5f),0f,(1f/5f),1f);
			bounds[i] = new Rectangle(MusicalFingers.width/2f - piano.getWidth()/2f*sF+32*sF*i,MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f))+3*4,32*sF,67*sF);
		}
		
	}
	
	public Rectangle getBounds(int i) {
		return bounds[i];
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized GamePiano");
        fingerList = controller.frame().fingers();
    }
    
    public void onFrame(Controller controller) {
    	iBox = controller.frame().interactionBox();
    	//Set notes to false
    	for(int i=0;i<5;i++) { notes[i]=false;}
  
    	//new method
    	for (Hand hand : controller.frame().hands()){
    		//Hand right = controller.frame().hands().rightmost();
    		Vector norm= hand.palmNormal();
    		for(Finger finger : hand.fingers()) {
    			Vector fingerEnd = finger.tipPosition().minus(finger.direction().normalized().times(finger.length()));
    			float LRangle =fingerEnd.minus(hand.palmPosition()).angleTo(hand.direction());
		    	 double j=1.1;

    			
    			System.out.println(LRangle+"," +(fingerEnd.getX()-hand.palmPosition().getX()));

    			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
    			//Vector fingerPos =finger.tipPosition();
    			 for(int i=0;i<5;i++) {
    				if (keys[i].contains(fingerPos.getX(), fingerPos.getZ())){
    					Vector fingerDir = finger.direction();
    					      float cosAngle = fingerDir.angleTo(norm);
    					      //System.out.println(cosAngle);
    					      if (cosAngle< j*5*0.9*Math.PI/16){
    					    	  //System.out.println(cosAngle);
    					    	  if(prevNotes[i]==false) {
    		    						pressed[i]=true;
    		    					}
    			    				notes[i] = true;
 
    					      }
    				}
    			 }
    				
    	
    		}
    	}
    
    	
    	System.arraycopy(notes, 0, prevNotes, 0, notes.length);
    	fingerList = controller.frame().fingers();
    }

	public boolean[] getPressed() {
		return pressed;
	}
	
	public void draw(SpriteBatch batch) {
		
		batch.draw(piano, MusicalFingers.width/2f - piano.getWidth()/2f*sF,MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f)),piano.getWidth()*sF,piano.getHeight()*(4f));

		
		batch.setColor(0f, 0f, 0f, 0.5f);
		for (int i = 0; i < 5; i++) {
			if (notes[i]) {
				batch.draw(rectangle, MusicalFingers.width/2f - piano.getWidth()/2f*sF + (i*32)*sF,MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f)) + 2*4, 32*sF,67*4);
			}
		}
		
		
		batch.setColor(Color.BLACK);
		for(Finger finger: fingerList) {
			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
			
			batch.draw(fingerPoint,-6f+MusicalFingers.width/2f - piano.getWidth()/2f*sF+160f*sF*fingerPos.getX(),MusicalFingers.height- 110f - 10f - (piano.getHeight()*(4f))+((1f-fingerPos.getZ())*67)*sF,12,12);
		}
		
		//TODO: Add the reference levels for fingers and the piano
		
	}

}