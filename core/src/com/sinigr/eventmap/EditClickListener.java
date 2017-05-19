package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class EditClickListener extends ClickListener {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Node node;
        MainScreen main = MainScreen.getInstance();
        Actor tmp = event.getListenerActor();
        if(tmp instanceof Stack) {
            Stack stack = (Stack)tmp;
            node = (Node)stack.getChildren().get(0);
        } else node = (Node) tmp;
        if(!main.editMode) {
            main.selectNode(node);
            if(tmp instanceof Stack) {
                Stack stack = (Stack)tmp;
                main.selectedLabel = (Label) stack.getChildren().get(1);
            }
        } else {
            if(main.selectedNode == null) main.selectNode(node);
            else main.connectNodes(main.selectedNode, node);
        }
    }
}