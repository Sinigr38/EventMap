package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

class CustomStage extends Stage {
    CustomStage(ExtendViewport screenViewport) {
        super(screenViewport);
    }

    @Override
    public boolean keyDown(int keyCode) {
        MainScreen.getInstance().currentKeyCode = keyCode;
        return super.keyDown(keyCode);
    }

	@Override
    public boolean keyUp(int keyCode) {
        MainScreen.getInstance().currentKeyCode = 0;
    	return super.keyUp(keyCode);
    }
}   