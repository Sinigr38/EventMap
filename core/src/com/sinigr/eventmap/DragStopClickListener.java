package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

class DragStopClickListener extends ClickListener {
	/**
	 * Событие происходит при отпускании элемента интерфейса на сцену
	 */
	@Override
    public void clicked(InputEvent event, float x, float y) {
		int newX, newY;
		Node node;
		Node tmp = (Node)event.getListenerActor();
		MainScreen main = MainScreen.getInstance();
		newX = Math.round((tmp.getX())/20)*20;
		newY = Math.round((tmp.getY())/20)*20;
		if(tmp.type.equals("event")) {
			node = new Event(tmp);
			Event e = (Event)node;
			Label label = new Label(e.name, TextureHelper.getInstance().labelStyle);
			label.setAlignment(Align.center);
			Stack stack = new Stack(node, label);
			stack.setPosition(newX,newY);
			stack.setWidth(node.getWidth());
			stack.setHeight(node.getHeight());
			stack.addListener(new EditClickListener());
			main.stage.addActor(stack);
			main.selectedLabel = label;
			main.nodes.add(e);
			main.selectNode(e);
		} else {
			node = new Node(tmp);
			node.addListener(new EditClickListener());
			node.setPosition(newX, newY);
			main.stage.addActor(node);
			main.nodes.add(node);
		}
		main.selectNode(node);
		node.setZIndex(0);
		main.draggingActor.remove();
		main.draggingActor = null;
	}
}