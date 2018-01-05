package com.ai.fighters.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ai.fighters.AIFighters;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = AIFighters.WIDTH;
		config.height = AIFighters.HEIGHT;
		new LwjglApplication(new AIFighters(), config);
	}
}
