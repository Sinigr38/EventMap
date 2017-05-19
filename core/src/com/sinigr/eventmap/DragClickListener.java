package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class DragClickListener extends ClickListener {
	/**
     * Событие проихсодит при нажатии на элемент интерфейса, который необходимо перетащить
     */
	@Override
    public void clicked(InputEvent event, float x, float y) {
		Node tmpActor = (Node)event.getListenerActor();
		MainScreen main = MainScreen.getInstance();
		if(main.selectedNode!= null) main.unselectNode(main.selectedNode);
		main.draggingActor = new Node(tmpActor);
		main.draggingActor.addListener(new DragStopClickListener());
		main.stage.addActor(main.draggingActor);
    }
}