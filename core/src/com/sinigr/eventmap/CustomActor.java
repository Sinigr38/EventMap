package com.sinigr.eventmap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
public class CustomActor extends Actor {

    public TextureRegion tr;

    public CustomActor(TextureRegion tr) {
        this.tr = tr;
        setSize(tr.getRegionWidth(), tr.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
         batch.draw(tr, getX(), getY(), getWidth(), getHeight());
    }
}