package ox.musicalfingers.leap;

import java.io.IOException;
import java.lang.Math;

import ox.musicalfingers.display.InstrumentScreen;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;


/** Has a list of gestures (only finger taps) and a list of fingers.
 * The gestures and fingers are public so that they can be accessed by the main program
 * But we may want to create accessors for this instead.  */
class PianoListener extends Listener {
	
    private LeapMotion leap;
    private InstrumentScreen screen;

    public PianoListener(LeapMotion leap) {
    	this.leap = leap;
    }
    
    public void onInit (Controller controller){
    	controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        System.out.println("Initialized");
    }
    
    public void onFrame(Controller controller) {
    	Frame frame = controller.frame();
    	leap.processData(frame);
    }
}

