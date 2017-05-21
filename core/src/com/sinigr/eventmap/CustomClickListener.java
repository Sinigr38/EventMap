package com.sinigr.eventmap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import static com.sinigr.eventmap.Calculations.variants;

public class CustomClickListener extends ClickListener {

    public CustomClickListener(Types type) {
        this.type = type;
    }

    enum Types { EDIT, STOPDRAG, STARTDRAG, CONNECTIONS, STARTCALC, GETVARIANT}
    Types type;

    @Override
    public void clicked(InputEvent event, float x, float y) {
        switch (type) {
            case EDIT: edit(event);break;
            case STOPDRAG: stopDrag(event);break;
            case STARTDRAG: startDrag(event);break;
            case CONNECTIONS: connections();break;
            case STARTCALC: startCalc();break;
            case GETVARIANT: getVariant(event);break;
        }
    }

    /** Обработка нажатия на узел для редактирования */
    private void edit(InputEvent event) {
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

    /** Запуск вычислительного модуля */
    private void startCalc() {
        Calculations.startCalculations();
    }

    /** Вкл/выкл режим установки связей */
    private void connections() {
        MainScreen main = MainScreen.getInstance();
        main.editMode = !main.editMode;
        if(main.editMode) main.editButton.tr = TextureHelper.getInstance().connections_s;
        else main.editButton.tr = TextureHelper.getInstance().connections;
        main.unselectNode(main.selectedNode);

    }

    /** Начать перетаскивание */
    private void startDrag(InputEvent event) {
        Node tmpActor = (Node)event.getListenerActor();
        MainScreen main = MainScreen.getInstance();
        if(main.selectedNode!= null) main.unselectNode(main.selectedNode);
        main.draggingActor = new Node(tmpActor);
        main.draggingActor.addListener(new CustomClickListener(Types.STOPDRAG));
        main.stage.addActor(main.draggingActor);
    }

    /** Закончить перетаскивание */
    private void stopDrag(InputEvent event) {
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
            stack.addListener(new CustomClickListener(Types.EDIT));
            main.stage.addActor(stack);
            main.selectedLabel = label;
            main.nodes.add(e);
            main.selectNode(e);
        } else {
            node = new Node(tmp);
            node.addListener(new CustomClickListener(Types.EDIT));
            node.setPosition(newX, newY);
            main.stage.addActor(node);
            main.nodes.add(node);
        }
        main.selectNode(node);
        node.setZIndex(0);
        main.draggingActor.remove();
        main.draggingActor = null;
    }

    /** Получить сценарий развития событий */
    private void getVariant(InputEvent event) { MainScreen main = MainScreen.getInstance();
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
