package com.sinigr.eventmap.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sinigr.eventmap.MainScreen;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Event Map";
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.width = (int)screenSize.getWidth();
		config.height = (int)screenSize.getHeight();
		config.fullscreen = true;
	    config.vSyncEnabled = true;
		MainScreen.initInstance();
		new LwjglApplication(MainScreen.getInstance(), config);
	}
}
