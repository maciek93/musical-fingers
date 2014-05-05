package ox.musicalfingers.instrument.guitar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteInputDisplay;
import ox.musicalfingers.instrument.KeyPos;

public class Guitar extends Listener implements DiscreteInputDisplay{
	
	private KeyPos [] keyPos = new KeyPos[6];
	private boolean[] keys = new boolean[6];
	private FingerList fingers;
	private InteractionBox iBox;
	
	Texture rectangle;
	Texture circle;
	
	Texture piano;
	Texture fingerPoint;
	
	int sF = 1;
	
	public Guitar() {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		
		pixmap = new Pixmap(16, 16, Format.RGBA8888);
		pixmap.setColor(1, 0, 0, 1);
		pixmap.fillCircle(8, 8, 8);
		circle = new Texture(pixmap);
		pixmap.dispose();
		
		piano = MusicalFingers.manager.get("assets/guitar_main.png");
		fingerPoint = MusicalFingers.manager.get("assets/finger.png");
		
		sF = ((MusicalFingers.width/160)-2);
		
		for(int i=0;i<6;i++) {
				float z=1;
				float x=160+(i*16)*sF*z;
				float y=120+2*sF*z;
				float width=32*sF*z;
				float height=67*sF*z;
				keyPos[i]= new KeyPos(x/MusicalFingers.width, y/MusicalFingers.height, width/MusicalFingers.width, 
						height/MusicalFingers.height);
		}
	}

	@Override
	public boolean[] getNotes() {
		return notes;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
		
		batch.draw(piano,160,120,sF*160,sF*80);
		
		batch.setColor(0f,0f,0f,0.5f);
		for(int i=0;i<6;i++) {
			if(notes[i]) {
				float z=1;
				float x=147+2*sF*z;
				float y=150+(i*13)*sF*z;
				float width=160*sF*z;
				float height=5*sF*z;
				
				batch.draw(rectangle,x,y,width,height);
			}
		}
		
		batch.setColor(Color.WHITE);
		for(Finger finger : fingers) {
			
	        Vector Position = finger.tipPosition();
	        Vector nPos = iBox.normalizePoint(Position);

			batch.draw(fingerPoint,nPos.getX()*MusicalFingers.width-6,nPos.getY()*MusicalFingers.height-6,12,12);
			
		}
		
		
	}
	
	private Frame lastFrame = new Frame();
	 
	boolean[] notes = new boolean[12];
    
    public void onInit (Controller controller){
        System.out.println("Initialized GuitarListener");
        fingers = controller.frame().fingers();
        iBox = controller.frame().interactionBox();
    }
    
    public void onFrame(Controller controller) {
    	processData(controller.frame());
    	
    }
	
	private void processData(Frame frame) {
		iBox = frame.interactionBox();
		notes = new boolean[12];
		for(Pointable pointable : frame.pointables()) {
			float z = iBox.normalizePoint(pointable.tipPosition()).getZ();
			float y = iBox.normalizePoint(pointable.tipPosition()).getY();
			Pointable last = lastFrame.pointable(pointable.id());
			if(last.isValid() && y < 5) {
				float lastZ = iBox.normalizePoint(pointable.tipPosition()).getZ();
				int string = processPointable(z, lastZ);
				if(string >= 0) {
					notes[string] = true;
				}
			}
		}
		lastFrame = frame;
	}
	
	 private int processPointable(float z, float lastZ) {
			return FindKey(z, lastZ);
		}
	 
	 public int FindKey(Float x, Float y) {
			for(int i=0;i<6;i++) {
				if (keyPos[i].isInRange(x, y)) {return i;}
			}
			return -1;
		}

}
