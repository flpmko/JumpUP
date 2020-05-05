package sk.uniza.fri.inf.simko.jump.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sk.uniza.fri.inf.simko.jump.Game;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        
        config.title = "JumpUP";
        config.width = 500;
        config.height = 1000;
        new LwjglApplication(new Game(), config);
    }
}
