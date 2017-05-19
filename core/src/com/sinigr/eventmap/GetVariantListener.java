package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.sinigr.eventmap.Calculations.variants;

class GetVariantListener extends ClickListener {

    @Override
    public void clicked(InputEvent event, float x, float y) {
        MainScreen main = MainScreen.getInstance();
        Stack tmp = (Stack) event.getListenerActor();
        Label label = (Label)tmp.getChildren().get(1);
        boolean[] array = variants.get(Integer.parseInt(label.getText().toString())-1);
        for(int i = 0; i < main.nodes.size(); i++) {
            Node node = main.nodes.get(i);
            if (node instanceof Event) node.getParent().setVisible(array[i]);
            else node.setVisible(array[i]);
        }
    }
}
