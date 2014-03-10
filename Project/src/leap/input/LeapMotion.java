package leap.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Pointable;

public class LeapMotion {
	
	private PianoListener pianoListener = new PianoListener(this);
	private Controller controller = new Controller();
	List<Finger> fingers = new LinkedList<Finger>();
	Set<Gesture> currentGestures = new HashSet<Gesture>();
	
	public LeapMotion() {
		controller.addListener(pianoListener);
	}
	
	public boolean[] processData() {
		Frame frame = controller.frame();
		FingerList newFingers = frame.fingers();
		for(Iterator<Finger> fingersIter = newFingers.iterator(); fingersIter.hasNext();) {
			Finger finger = fingersIter.next();
			if(!fingers.contains(finger)) {
				identifyFingers(newFingers);
				break;
			}
		}
		GestureList newGestures = frame.gestures();
		boolean[] taps = new boolean[10];
		Set<Gesture> update = new HashSet<Gesture>();
		for(Iterator<Gesture> gestureIter = newGestures.iterator(); gestureIter.hasNext();) {
			Gesture gesture = gestureIter.next();
			if(!currentGestures.contains(gesture)) {
				taps[processGesture((KeyTapGesture) gesture)] = true;
			}
			update.add(gesture);
		}
		currentGestures = update;
		return taps;
	}

	private int processGesture(KeyTapGesture gesture) {
		Pointable finger = gesture.pointable();
		return fingers.indexOf(finger);
	}

	private void identifyFingers(FingerList newFingers) {
		HashMap<Float, Finger> fingerMap = new HashMap<Float, Finger>();
		List<Finger> update = new LinkedList<Finger>();
		for(Iterator<Finger> fingers = newFingers.iterator(); fingers.hasNext();) {
			Finger finger = fingers.next();
			fingerMap.put(finger.tipPosition().getX(), finger);
		}
		Float[] positions = (Float[]) fingerMap.keySet().toArray();
		Arrays.sort(positions);
		for(int i = 0; i < positions.length; i++) {
			update.add(fingerMap.get(positions[i]));
		}
		fingers = update;
	}
}
