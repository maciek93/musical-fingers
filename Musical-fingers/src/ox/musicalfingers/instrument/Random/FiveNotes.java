package ox.musicalfingers.instrument.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import ox.musicalfingers.instrument.DiscreteOutput;

public class FiveNotes implements DiscreteOutput {
	
	 Sound[] sounds = new Sound[5];
	
	public FiveNotes() {
		//TEMPORARY
    	sounds[0] = Gdx.audio.newSound(Gdx.files.internal("assets/sound1.wav"));
    	sounds[1] = Gdx.audio.newSound(Gdx.files.internal("assets/sound2.wav"));
    	sounds[2] = Gdx.audio.newSound(Gdx.files.internal("assets/sound3.wav"));
    	sounds[3] = Gdx.audio.newSound(Gdx.files.internal("assets/sound4.wav"));
    	sounds[4] = Gdx.audio.newSound(Gdx.files.internal("assets/sound5.wav"));
		
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
