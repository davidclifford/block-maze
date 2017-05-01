package block.maze.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import block.maze.game.BlockMaze;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = false; // Setting to false disables vertical sync
        config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
        config.backgroundFPS = 0; // Setting to 0 disables background fps throttling
        config.useGL30 = true;
        config.width = 1920;
        config.height = 1080;
        config.fullscreen = true;
		new LwjglApplication(new BlockMaze(), config);
	}
}
