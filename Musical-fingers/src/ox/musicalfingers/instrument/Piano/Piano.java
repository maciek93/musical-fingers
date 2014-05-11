package ox.musicalfingers.instrument.Piano;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

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

public class Piano extends Listener implements DiscreteInputDisplay{
	
	private boolean[] notes = new boolean[5];
	Rectangle[] keys = new Rectangle[5];
	
	Texture piano;
	Texture fingerPoint;
	Texture rectangle;
	
	int sF = 1;
	
	FingerList fingerList;
	InteractionBox iBox;
	
	ArrayList<Finger> fingerListSorted = new ArrayList<Finger>(10);
	
	float pianoLevel = 0.5f;
	
	public Piano() {
		//Define keys in normalised InteractionBox co-ords
		for(int i=0;i<5;i++) {
			keys[i] = new Rectangle(i*(1f/5f),0f,(1f/5f),1f);
		}
		
		piano = MusicalFingers.manager.get("assets/5key_piano.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		//ScaleFactor
		sF = (int) (MusicalFingers.height/piano.getHeight()/1.5f);
		//sF-=2;
		
		//Rectangle
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
	}
	
    public void onInit (Controller controller) {
        System.out.println("Initialized Piano");
        fingerList = controller.frame().fingers();
        iBox = controller.frame().interactionBox();
    }
    
    
    public void onFrame(Controller controller) {
    	
    	boolean[] noteCopy = new boolean[5];
    	iBox = controller.frame().interactionBox();
    	//Set notes to false
    	for(int i=0;i<5;i++) { noteCopy[i]=false;}
    	
    	//for(Hand hand : controller.frame().hands()) {
    		for(Finger finger : controller.frame().fingers()) {
    			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
    			
    			for(int i=0;i<5;i++) {
    				/*
	    			if (keys[i].contains(fingerPos.getX(), fingerPos.getZ()) && (iBox.normalizePoint(palmPos,false).getY() - fingerPos.getY() > 0.001f)) {
	    				notes[i] = true;
	    			}
	    				//*/
    				///*
    				if (keys[i].contains(fingerPos.getX(), fingerPos.getZ()) && (fingerPos.getY() < pianoLevel)) {
    					noteCopy[i] = true;
	    			}
    				/*
    				else {
	    				notes[i]=false;
	    			}
	    			*/
    			}
    		}
    	//}
    	
    	this.setNotes(noteCopy);
    	fingerList = controller.frame().fingers();
    }
    
    private void sortFingerList(FingerList fingers) {
    	fingerListSorted.clear();
    	for(Finger finger : fingers) {
    		fingerListSorted.add(finger);
    	}
    	Collections.sort(fingerListSorted, new Comparator<Finger>() {
    		public int compare(Finger f1, Finger f2) {
    			return (int) (f1.tipPosition().getX() - f2.tipPosition().getX());
    		}
    	});
    }
    
    public synchronized void setNotes(boolean[] note) {
    	notes = note;
    }

	@Override
	public synchronized boolean[] getNotes() {
		return notes;
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		//Piano
		batch.setColor(Color.WHITE);
		batch.draw(piano, MusicalFingers.width/2f - piano.getWidth()/2f*sF,MusicalFingers.height/6f,piano.getWidth()*sF,piano.getHeight()*sF);
		
		//Keys
		batch.setColor(0f, 0f, 0f, 0.5f);
		for (int i = 0; i < 5; i++) {
			if (notes[i]) {
				batch.draw(rectangle, MusicalFingers.width/2f - piano.getWidth()/2f*sF + (i*32)*sF,MusicalFingers.height/6f + 2*sF, 32*sF,67*sF);
			}
		}
		
		sortFingerList(fingerList);
		
		//Draw the levels of the fingers
		batch.setColor(Color.RED);
		for(int i=0; i < fingerListSorted.size(); i++) {
			Finger finger = fingerListSorted.get(i);
			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),true);
			
			batch.draw(rectangle,MusicalFingers.width/2f - fingerListSorted.size()/2f*10f*sF+sF*10f*i,50+(fingerPos.getY()-pianoLevel)*50f,10*sF,sF);
		}
		
		batch.setColor(Color.BLACK);
		batch.draw(rectangle,MusicalFingers.width/2f - 5*10f*sF,49,100*sF,sF);
		
		//Fingers
		batch.setColor(Color.WHITE);
		for(Finger finger: fingerList) {
			Vector fingerPos = iBox.normalizePoint(finger.tipPosition(),false);
			batch.draw(fingerPoint,-18f+MusicalFingers.width/2f - (piano.getWidth()/2f*sF) +piano.getWidth()*sF*fingerPos.getX(),-18f+MusicalFingers.height/6f+(6+64*(1f-1f*fingerPos.getZ()))*sF,36,36);
		}
		
	}

}
