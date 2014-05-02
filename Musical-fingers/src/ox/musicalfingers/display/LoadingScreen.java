package ox.musicalfingers.display;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.openal.Mp3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LoadingScreen implements Screen {
	
	private AssetManager manager;
	
	private float progress = 0f;
	private Texture rectangle;
	private boolean change = false;
	
	public LoadingScreen(AssetManager manager) {
		this.manager = manager;
	}

	@Override
	public void init() {
		//Load rectangle for drawing the progress bar
		Pixmap pixmap = new Pixmap( 1,1, Format.RGBA8888 );
		pixmap.setColor( 1,1,1,1);
		pixmap.fill();
		rectangle = new Texture(pixmap);
		pixmap.dispose();
		
		//Load all assets here
		
		//Instrument Screen stuff
		manager.load("assets/1note.png", Texture.class);
		manager.load("assets/2note.png", Texture.class);
		manager.load("assets/3note.png", Texture.class);
		manager.load("assets/clef.png", Texture.class);
		manager.load("assets/5key_piano.png", Texture.class);
		manager.load("assets/finger.png", Texture.class);
		manager.load("assets/notRecording.png", Texture.class);
		manager.load("assets/recording.png", Texture.class);
		manager.load("assets/notPlaying.png", Texture.class);
		manager.load("assets/playing.png", Texture.class);
		
		//Font
		manager.load("assets/font/pixel.fnt", BitmapFont.class);
		manager.load("assets/font/pixel.png", Texture.class);
		
		//Skin stuff
		//manager.load("assets/ui/buttonUp.png", Texture.class);
		//manager.load("assets/ui/buttonDown.png", Texture.class);
		//manager.load("assets/ui/buttonOver.png", Texture.class);
		manager.load("assets/ui/pixelSkin.json",Skin.class,new SkinLoader.SkinParameter("assets/ui/pixelSkin.pack"));
		
		//Piano sounds
		manager.load("assets/sound1.mp3", Sound.class);
		manager.load("assets/sound2.mp3", Sound.class);
		manager.load("assets/sound3.mp3", Sound.class);
		manager.load("assets/sound4.mp3", Sound.class);
		manager.load("assets/sound5.mp3", Sound.class);

	}

	@Override
	public void update() {
		if(manager.update()) {
			change = true;
		}
		
		progress = manager.getProgress();

	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.RED);
		batch.draw(rectangle,MusicalFingers.width/3f,MusicalFingers.height/2f-100,progress*MusicalFingers.width/3f,200);

	}

	@Override
	public int changeStateTo() {
		if(change) {
			return MusicalFingers.STATE_MENU;
		} else {
			return -1;
		}
	}

	@Override
	public void dispose() {
		manager.dispose();
	}

}
