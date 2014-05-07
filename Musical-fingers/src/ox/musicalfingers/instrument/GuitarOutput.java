package ox.musicalfingers.instrument;

import ox.musicalfingers.display.MusicalFingers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;

public class GuitarOutput implements DiscreteOutput {
	 Sound[] sounds = new Sound[12];
		
	public GuitarOutput() {
		
   	for(int i = 1; i < 13; i++) {
   		sounds[i-1] = MusicalFingers.manager.get("assets/guitar"+ i +".wav");
   	}
	}

	@Override
	public void playNotes(boolean[] notes) {
		for(int i = 0; i < 12; i++) {
			if(notes[i]) {
				sounds[i].stop();
				sounds[i].play();
			}
		}
		
	}
}
