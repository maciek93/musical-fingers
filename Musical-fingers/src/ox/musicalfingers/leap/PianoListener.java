package ox.musicalfingers.leap;

import java.io.IOException;
import java.lang.Math;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ox.musicalfingers.display.InstrumentScreen;
import ox.musicalfingers.instrument.DiscreteInput;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;


/** Has a list of gestures (only finger taps) and a list of fingers.
 * The gestures and fingers are public so that they can be accessed by the main program
 * But we may want to create accessors for this instead.  */
public class PianoListener extends Listener implements DiscreteInput{
	
	Set<Gesture> currentGestures = new HashSet<Gesture>();
	boolean[] notes = new boolean[10];
    
    public void onInit (Controller controller){
    	controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        System.out.println("Initialized PianoListener");
    }
    
    public void onFrame(Controller controller) {
    	processData(controller.frame());
    }
    
    public boolean[] getNotes() {
    	return notes;
    }
    
    private void processData(Frame frame) {
		boolean[] taps = new boolean[6];
		Set<Gesture> update = new HashSet<Gesture>();
		for(Gesture gesture : frame.gestures()) {
			if(!currentGestures.contains(gesture)) {
				int key = processGesture(gesture);
				if(key >= 0) {
					taps[key] = true;
				}
			}
			update.add(gesture);
		}
		currentGestures = update;
		notes = taps;
	}

    private int processGesture(Gesture gesture) {
		KeyTapGesture tap = new KeyTapGesture(gesture);
		float x = tap.position().getX();
		float y = tap.position().getY();
		double angles = Math.toDegrees(Math.atan2(x,y));
		int area = (int) Math.round(angles);
		if(-90 <= area && area < -60) {
			return 0;
		} else if (-60 <= area && area < -30) {
			return 1;
		} else if (-30 <= area && area < 0) {
			return 2;
		} else if (0 <= area && area < 30) {
			return 3;
		} else if (30 <= area && area < 60) {
			return 4;
		} else if (60 <= area && area < 90) {
			return 5;
		} else {
			return -1;
		}
	}
}

