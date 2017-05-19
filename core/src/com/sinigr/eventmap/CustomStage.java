package com.sinigr.eventmap;

import com.badlogic.gdx.Input;
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
        checkKeyC(keyCode);
    	return super.keyUp(keyCode);
    }

   private void checkKeyC(int keyCode) {
    	if(keyCode == Input.Keys.C) {
            MainScreen main = MainScreen.getInstance();
            main.editMode = !main.editMode;
            main.unselectNode(main.selectedNode);
    	}
    }
}   