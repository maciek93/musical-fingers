package ox.musicalfingers.display;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//Temporary name
		config.title = "Musical Fingers";
		config.useGL20 = true;
		//Fixed Size Window
		config.width = 1280;
		config.height = 800;
		//config.resizable = false;
		//Fullscreen
		//config.fullscreen=true;
		
		new LwjglApplication(new MusicalFingers(), config);
	}

}
