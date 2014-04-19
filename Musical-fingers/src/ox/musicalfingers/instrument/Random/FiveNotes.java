package ox.musicalfingers.instrument.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import ox.musicalfingers.display.MusicalFingers;
import ox.musicalfingers.instrument.DiscreteOutput;

public class FiveNotes implements DiscreteOutput {
	
	 Sound[] sounds = new Sound[5];
	
	public FiveNotes() {
   	
    	sounds[0] = MusicalFingers.manager.get("assets/sound1.mp3");
    	sounds[1] = MusicalFingers.manager.get("assets/sound2.mp3");
    	sounds[2] = MusicalFingers.manager.get("assets/sound3.mp3");
    	sounds[3] = MusicalFingers.manager.get("assets/sound4.mp3");
    	sounds[4] = MusicalFingers.manager.get("assets/sound5.mp3");
		
	}

	@Override
	public void playNotes(boolean[] notes) {
		for(int i=0; i<5;i++) {
			if(notes[i]) {
				sounds[i].stop();
				sounds[i].play();
			}
		}
		
	}

}
