package ox.musicalfingers.display;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
		manager.load("assets/1note.png", Texture.class);
		manager.load("assets/2note.png", Texture.class);
		manager.load("assets/3note.png", Texture.class);
		manager.load("assets/clef.png", Texture.class);
		manager.load("assets/5key_piano.png", Texture.class);
		manager.load("assets/finger.png", Texture.class);
		
		//Font
		manager.load("assets/font/pixel.fnt", BitmapFont.class);
		manager.load("assets/font/pixel.png", Texture.class);

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
