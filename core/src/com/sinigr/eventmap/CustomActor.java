package com.sinigr.eventmap;

        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Batch;
        import com.badlogic.gdx.scenes.scene2d.Actor;

class CustomActor extends Actor {

    private Texture texture;

    CustomActor(Texture tr) {
        texture = tr;
        setSize(texture.getWidth(), texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}