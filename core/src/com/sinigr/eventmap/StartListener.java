package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class StartListener extends ClickListener {

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Calculations.startCalculations();
    }
}
