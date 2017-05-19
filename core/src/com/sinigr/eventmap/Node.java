package com.sinigr.eventmap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
class Node extends Actor {

	TextureRegion tr;
	String type;
	Node in1, in2;
	Node out1, out2;
	
	Node(TextureRegion tr) {
		this.tr = tr;
		setSize(tr.getRegionWidth(), tr.getRegionHeight());
		setOrigin(tr.getRegionWidth()/2, tr.getRegionHeight()/2);
	}
	
	Node(Node other) {
		tr = other.tr;
		type = other.type;
		in1 = other.in1;
		in2 = other.in2;
		out1 = other.out1;
		out2 = other.out2;
		setSize(other.getWidth(), other.getHeight());
		setOrigin(other.getWidth()/2,other.getHeight()/2);
		setPosition(other.getX(), other.getY());
	}
	
	@Override
    public void draw(Batch batch, float parentAlpha) {
    	batch.draw(tr, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }	
}