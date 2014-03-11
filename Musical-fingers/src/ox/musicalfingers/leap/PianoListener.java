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
	
	List<Integer> fingers = new LinkedList<Integer>();
	boolean[] notes = new boolean[10];
    
    public void onInit (Controller controller){
    	controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        System.out.println("Initialized PianoListener");
    }
    
    public void onFrame(Controller controller) {
    	Frame frame = controller.frame();
    	processData(frame);
    }
    
    public boolean[] getNotes() {
    	return notes;
    }
    
    private void processData(Frame frame) {
		//System.out.println("Detected " + frame.gestures().iterator().next().id() + " gesture");
		//System.out.println("Which has " + (new KeyTapGesture(frame.gestures().iterator().next()).pointable().id()) + "Fingers");
		for(Finger finger : frame.fingers()) {
			//System.out.println("Frame has finger " + finger.id());
			if(!fingers.contains(Integer.valueOf(finger.id()))) {
				identifyFingers(frame.fingers());
				break;
			}
		}

		for(Gesture gesture : frame.gestures()) {
			//if(!currentGestures.contains(gesture)) {
			notes[processGesture(gesture)] = true;
			//}

		}
	}

	private int processGesture(Gesture gesture) {
		int fingerInt = new KeyTapGesture(gesture).pointable().id();
		//System.out.println("Found finger " + fingers.indexOf(Integer.valueOf(fingerInt)));
		return fingers.indexOf(Integer.valueOf(fingerInt));
	}

	private void identifyFingers(FingerList newFingers) {
		HashMap<Float, Finger> fingerMap = new HashMap<Float, Finger>();
		List<Integer> update = new LinkedList<Integer>();
		for(Iterator<Finger> fingers = newFingers.iterator(); fingers.hasNext();) {
			Finger finger = fingers.next();
			//System.out.println("Frame has finger " + finger.id());
			fingerMap.put(finger.tipPosition().getX(), finger);
		}
		Object[] positions = fingerMap.keySet().toArray();
		Arrays.sort(positions);
		for(int i = 0; i < positions.length; i++) {
			//System.out.println("Identified finger " + i);
			update.add(fingerMap.get(positions[i]).id());
		}
		fingers = update;
	}
}

