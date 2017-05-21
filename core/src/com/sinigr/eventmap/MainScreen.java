package com.sinigr.eventmap;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MainScreen extends ApplicationAdapter {
	private static MainScreen mainScreen;

	private static TextureHelper textures;
	private OrthographicCamera camera; //Камера
	private Table tableUI, upTable, downTable;
	private ShapeRenderer shapeRenderer;

	//Актёры
	ArrayList<Node> nodes;
	Node draggingActor;
	Node selectedNode;
	Label selectedLabel;
	CustomActor editButton;
	CustomStage stage;
	private TextField name, probability;
	private TextArea description;


	//Переменные
	private int deltaX = 0;
	private int deltaY = 0;
	private int width;
	private int height;
	int currentKeyCode;
	boolean editMode = false;

	/** Общий обработчик для трех текстовых полей */
	private TextField.TextFieldListener textListener = new TextField.TextFieldListener() {
		@Override
		public void keyTyped(TextField textField, char c) {
			Event event = (Event)selectedNode;
			if (textField.equals(name)) {
				event.name = textField.getText();
				selectedLabel.setText(event.name);
			}
			else if (textField.equals(description)) event.description = textField.getText();
			else {
				String s = textField.getText();
				if (s.matches("[-+]?\\d+")) {
					int probability = Integer.valueOf(s);
					event.probability = probability > 100 ? 100: probability;
				}
				else event.probability = 100;
			}
		}
	};

	public static void initInstance(){mainScreen = new MainScreen();}
	public static MainScreen getInstance() {return mainScreen;}

	@Override
	public void create () {
		getGraphicsParams();
		nodes = new ArrayList<Node>();
		TextureHelper.initInstance();
		textures = TextureHelper.getInstance();
		textures.initTextures();
		camera = new OrthographicCamera();
   		camera.setToOrtho(false, width, height);
   		ExtendViewport viewp = new ExtendViewport(width, height, camera);
   		stage = new CustomStage(viewp);
		shapeRenderer = new ShapeRenderer();
   		Gdx.input.setInputProcessor(stage);
   		loadUI();

	}

	private void getGraphicsParams() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}

	private void loadUI() {
		tableUI = new Table();
		tableUI.top().left().setFillParent(true);
		tableUI.add(getUpperTable()).left().expandX().row();
		tableUI.setDebug(true);
   		loadDragObjects();
		loadTextFields();
   		stage.addActor(tableUI);
		tableUI.setZIndex(1);
	}

	/** Кнопки старт и редактирование */
	private Table getUpperTable() {
		upTable = new Table();
		editButton = new CustomActor(textures.connections);
		editButton.addListener(new CustomClickListener(CustomClickListener.Types.CONNECTIONS));
		upTable.add(editButton).padRight(10);

		CustomActor actor = new CustomActor(textures.btnback);
		Label label = new Label("Start", TextureHelper.getInstance().labelStyle);
		label.setAlignment(Align.center);
		Stack stack = new Stack(actor, label);
		stack.addListener(new CustomClickListener(CustomClickListener.Types.STARTCALC));
		upTable.add(stack);
		return upTable;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateCamera();
		updateDragginActor();
		renderShapes();
		stage.act(Gdx.graphics.getDeltaTime());
    	stage.draw();
	}

	/** Отрисовка фигур */
	private void renderShapes() {
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(0,0, width,200);
		renderLines();
		shapeRenderer.end();
	}

	/** Отрисовка соединительных линий*/
	private void renderLines() {
		Line line;
		shapeRenderer.setColor(Color.RED);
		for(Node node: nodes) {
			if(node.out1 != null && checkVisible(node.out1) && checkVisible(node)) {
				line = getLine(node, node.out1);
				shapeRenderer.rectLine(line.x1 + deltaX, line.y1 + deltaY,line.x2 + deltaX,line.y2 + deltaY, 3);
			}
			if(node.out2 != null && checkVisible(node.out2) && checkVisible(node)) {
				line = getLine(node, node.out2);
				shapeRenderer.rectLine(line.x1 + deltaX, line.y1 + deltaY,line.x2 + deltaX,line.y2 + deltaY, 3);
			}
		}
	}

	/** Проверка видимости узла */
	private boolean checkVisible(Node node) {
		if (node instanceof Event) return node.getParent().isVisible();
		else return node.isVisible();
	}

	/** Получить соединительную лнию для отрисовки */
	private Line getLine(Node n1, Node n2) {
		float x1,x2,y1,y2;
		if(n1.type.equals("event")) {
			x1 = n1.getParent().getX();
			y1 = n1.getParent().getY();
		} else {
			x1 = n1.getX();
			y1 = n1.getY();
		}
		if(n2.type.equals("event")) {
			x2 = n2.getParent().getX();
			y2 = n2.getParent().getY();
		} else {
			x2 = n2.getX();
			y2 = n2.getY();
		}
		return new Line(x1 + n1.getWidth()/2, y1 + n1.getHeight()/2, x2 + n2.getWidth()/2, y2 + n2.getHeight()/2);
	}

	/** Загрузка полей для ввода информации */
	private void loadTextFields() {
		name = new TextField("", textures.textStyle);
		name.setTextFieldListener(textListener);
		description = new TextArea("", textures.textStyle);
		description.setTextFieldListener(textListener);
		probability = new TextField("", textures.textStyle);
		probability.setTextFieldListener(textListener);
		probability.setMaxLength(3);
		downTable.add(name).bottom().padLeft(10).padBottom(10).minWidth(300).minHeight(180);
		downTable.add(description).bottom().padLeft(10).padBottom(10).minWidth(300).minHeight(180).fillX().expandX();
		downTable.add(probability).bottom().padLeft(10).padBottom(10).minWidth(50).minHeight(180);
		hideTextFields();
	}

	/** Скрыть поля ввода */
	private void hideTextFields() {
		name.setVisible(false);
		description.setVisible(false);
		probability.setVisible(false);
	}

	/** Добавить поля ввода */
	private void showTextFields() {
		name.setVisible(true);
		description.setVisible(true);
		probability.setVisible(true);
	}

	/** Загрузка объектов для перетаскивания */
	private void loadDragObjects() {
		downTable = new Table();
		downTable.add(addActorToDragGroup(textures.event, "event")).padLeft(10);
		downTable.add(addActorToDragGroup(textures.xor, "xor")).padLeft(10);
		downTable.add(addActorToDragGroup(textures.or, "or")).padLeft(10);
		tableUI.add(downTable).bottom().left().fillX().expandY();
	}

	/** Добавить актёра в группу объектов для перетаскивания */
	private Node addActorToDragGroup(TextureRegion tr, String type) {
		Node actor;
		actor = new Node(tr);
		actor.type = type;
		actor.addListener(new CustomClickListener(CustomClickListener.Types.STARTDRAG));
		return actor;
	}

	/**
	 * Обработка движения камеры по основной сцене
	 */
	private void updateCamera() {
		if(currentKeyCode != 0) {
			if(currentKeyCode == Keys.LEFT) {
				camera.position.x -= 20;
				deltaX +=20;
				tableUI.setX(tableUI.getX()-20);
			}
			if(currentKeyCode == Keys.RIGHT) {
				camera.position.x += 20;
				deltaX -=20;
				tableUI.setX(tableUI.getX()+20);
			}
			if(currentKeyCode == Keys.UP) {
				camera.position.y += 20;
				deltaY -=20;
				tableUI.setY(tableUI.getY()+20);
			}
			if(currentKeyCode == Keys.DOWN) {
				camera.position.y -= 20;
				deltaY +=20;
				tableUI.setY(tableUI.getY()-20);
			}
		}
	}
	
	/** Обновление положения переаскиваемого актёра */
	private void updateDragginActor() {
		if (draggingActor != null) {
			Point location = MouseInfo.getPointerInfo().getLocation();
			draggingActor.setPosition((float)location.getX() - draggingActor.getWidth()/2 - deltaX,
					height-(float)location.getY() - draggingActor.getHeight()/2-deltaY);
		}
	}

	/** Выбрать узел */
	void selectNode(Node node) {
		if(selectedNode!=null) unselectNode(selectedNode);
		selectedNode = node;
		if(node.type.equals("xor"))node.tr = TextureHelper.getInstance().xor_s;
		else if (node.type.equals("or"))node.tr = TextureHelper.getInstance().or_s;
		else {
			Event e = (Event)node;
			node.tr = TextureHelper.getInstance().event_s;
			name.setText(e.name);
			description.setText(e.description);
			probability.setText(String.valueOf(e.probability));
			showTextFields();
		}
	}

	/** Убрать выделение с узла */
	void unselectNode(Node node) {
		if(node == null) return;
		if(node.type.equals("xor"))node.tr = TextureHelper.getInstance().xor;
		else if (node.type.equals("or"))node.tr = TextureHelper.getInstance().or;
		else {
			node.tr = TextureHelper.getInstance().event;
			hideTextFields();
		}
		mainScreen.selectedNode = null;
	}

	/** Соединить два узла */
	void connectNodes(Node n1, Node n2) {
		if(n1.out1 == null) {
			if(n2.in1 == null) {
				n1.out1 = n2; n2.in1 = n1;
			} else if (n2.in2 == null) {
				n1.out1 = n2; n2.in2 = n1;
			}
		} else if(n1.out2 == null) {
			if(n2.in1 == null) {
				n1.out2 = n2; n2.in1 = n1;
			} else if (n2.in2 == null) {
				n1.out2 = n2; n2.in2 = n1;
			}
		}
		mainScreen.unselectNode(selectedNode);
	}

	void createVariants(int count) {
		Table t = new Table();
		for(int i = 0; i < count; i++) t.add(getItem(i + 1));
		ScrollPane scroll = new ScrollPane(t);
		scroll.setFadeScrollBars(false);
		scroll.setScrollingDisabled(false, true);
		upTable.add(scroll).left();
	}

	private Actor getItem(int i) {
		CustomActor actor = new CustomActor(textures.btnback);
		Label label = new Label(String.valueOf(i), TextureHelper.getInstance().labelStyle);
		label.setAlignment(Align.center);
		Stack stack = new Stack(actor, label);
		stack.addListener(new CustomClickListener(CustomClickListener.Types.GETVARIANT));
		return stack;
	}
}
