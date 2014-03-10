package leap.input;

import java.io.IOException;
import java.lang.Math;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;


/** Has a list of gestures (only finger taps) and a list of fingers.
 * The gestures and fingers are public so that they can be accessed by the main program
 * But we may want to create accessors for this instead.  */
class PianoListener extends Listener {
	
    private LeapMotion leap;

    public PianoListener(LeapMotion leap) {
    	this.leap = leap;
    }

    public void onConnect(Controller controller) {
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }
}

