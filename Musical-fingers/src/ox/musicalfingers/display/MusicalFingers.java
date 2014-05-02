package ox.musicalfingers.display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class MusicalFingers implements ApplicationListener{
	
	//Drawing 
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	
	//Screen info
	public static int width = 0;
	public static int height = 0;
	
	//To show fps
	private long time = 0;
	private int fps = 0;
	private int showfps = 0;
	
	//States
	public static final int STATE_LOADING = 0;
	public static final int STATE_MENU = 1;
	public static final int STATE_INSTRUMENT = 2;
	private int current_state = 0;
	
	//Different 'Screens' for the app
	Screen loading;
	Screen menu;
	Screen instrument;
	//Current screen, used for convenience 
	Screen current;
	
	//AssetManager for storing assets
	public static AssetManager manager = new AssetManager();
	

	@Override
	public void create() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		Gdx.graphics.setVSync(true);
		camera = new OrthographicCamera(width,height);
		//Set y-axis pointing down
		camera.setToOrtho(false);
		batch = new SpriteBatch();
		//Temporary font for drawing fps in top left corner
		font = new BitmapFont(Gdx.files.internal("assets/font/pixel.fnt"), Gdx.files.internal("assets/font/pixel.png"), false);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		time = TimeUtils.nanoTime();
		
		init();
	}
	
	//Initialise stuff for the app here
	private void init() {
		
		//Create screen objects
		loading = new LoadingScreen(manager);
		menu = new MenuScreen();
		instrument = new InstrumentScreen();
		
		//Set the state to loading
		changeState(STATE_LOADING);
		
		
	}
	
	private void changeState(int changeTo) {
		
		switch(changeTo) {
			case STATE_LOADING: 	loading.init();
									current_state = STATE_LOADING;
									current = loading;
									break;
			case STATE_MENU:		menu.init();
									current_state = STATE_MENU;
									current = menu;
									break;
			case STATE_INSTRUMENT:  instrument.init();
									current_state = STATE_INSTRUMENT;
									current = instrument;
									break;
			default:				throw new Error(changeTo + " is not a valid state");
		}
		
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		camera = new OrthographicCamera(width,height);
		camera.setToOrtho(false);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
	}

	@Override
	public void render() {
		update();
		
		//Clear screen with black background
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(147f/255f,210f/255f,255f/255f,1);
		
		batch.begin();
		
		draw(batch);
		
		//Draw fps in top left corner
		font.setColor(1,1,1,1);
		//font.setScale(1,-1);
		font.draw(batch, "FPS  "+showfps, 0,30);
		
		batch.end();
		
		//Calculate the fps for the previous second
		fps++;
		if(TimeUtils.nanoTime() - time >  (1000000000 - 1)) {
			showfps = fps;
			fps = 0;
			time = TimeUtils.nanoTime();
		}
	}
	
	//Main loop for the app
	private void update() {
		//Check if a screen wants to change state
		if(current.changeStateTo() != -1) {
			changeState(current.changeStateTo());
		}
		
		//Otherwise update it
		current.update();
		
	}
	
	//Draw stuff for the here
	private void draw(SpriteBatch batch) {
		current.draw(batch);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		manager.dispose();
		
	}

}
