package com.sinigr.eventmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextureHelper {
	//Текстуры
	private static TextureHelper textureHelper;

	public TextureAtlas atlas;
	TextureRegion event, xor, or, and;
	TextureRegion event_s, xor_s, or_s, and_s;
	Texture btnback;

	TextField.TextFieldStyle textStyle;
	Label.LabelStyle labelStyle;

	public static void initInstance() { textureHelper = new TextureHelper();}
	public static TextureHelper getInstance() { return textureHelper;}

	/**
	 * Загрузка текстур из файлов 
	 */
	public void initTextures() {
		atlas = new TextureAtlas(Gdx.files.internal("texture.atlas"));
		event =  new TextureRegion(atlas.findRegion("event"));
		xor = new TextureRegion(atlas.findRegion("xor"));
		or = new TextureRegion(atlas.findRegion("or"));
		and = new TextureRegion(atlas.findRegion("and"));
		event_s =  new TextureRegion(atlas.findRegion("event_s"));
		xor_s = new TextureRegion(atlas.findRegion("xor_s"));
		or_s = new TextureRegion(atlas.findRegion("or_s"));
		and_s = new TextureRegion(atlas.findRegion("and_s"));
		btnback = new Texture(Gdx.files.internal("btnback.png"));
		generateTextStyle();
	}

	/** Сгенерировать стиль для полей ввода */
	private void generateTextStyle() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/DroidSerif-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 36;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		textStyle = new TextField.TextFieldStyle();
		textStyle.font = font;
		textStyle.fontColor = Color.GOLD;
		Skin skin = new Skin();
		skin.add("background",new NinePatch(atlas.findRegion("back"), 10, 10, 10, 10));
		textStyle.background = skin.getDrawable("background");
		labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.GOLD;
	}
}


