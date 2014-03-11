package ox.musicalfingers.leap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ox.musicalfingers.display.InstrumentScreen;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Pointable;

public class LeapMotion {
	/*
    private InstrumentScreen screen;
	private PianoListener pianoListener = new PianoListener(this);
	private Controller controller = new Controller();
	List<Integer> fingers = new LinkedList<Integer>();
	Set<Gesture> currentGestures = new HashSet<Gesture>();
	
	public LeapMotion(InstrumentScreen screen) {
		controller.addListener(pianoListener);
		this.screen = screen;
	}
	
	public void processData(Frame frame) {
		//System.out.println("Detected " + frame.gestures().iterator().next().id() + " gesture");
		//System.out.println("Which has " + (new KeyTapGesture(frame.gestures().iterator().next()).pointable().id()) + "Fingers");
		FingerList newFingers = frame.fingers();
		for(Iterator<Finger> fingersIter = newFingers.iterator(); fingersIter.hasNext();) {
			Finger finger = fingersIter.next();
			//System.out.println("Frame has finger " + finger.id());
			if(!fingers.contains(Integer.valueOf(finger.id()))) {
				identifyFingers(newFingers);
				break;
			}
		}
		GestureList newGestures = frame.gestures();
		boolean[] taps = new boolean[10];
		Set<Gesture> update = new HashSet<Gesture>();
		for(Iterator<Gesture> gestureIter = newGestures.iterator(); gestureIter.hasNext();) {
			Gesture gesture = gestureIter.next();
			//if(!currentGestures.contains(gesture)) {
			taps[processGesture(gesture)] = true;
			//}
			update.add(gesture);
		}
		currentGestures = update;
		screen.setTaps(taps);
		screen.update();
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
	*/
}
