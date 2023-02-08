package ucc.team9.nanometro;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Demo");
		config.setWindowedMode(900, 900);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new Main(), config);
	}
}
