package com.sinigr.eventmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class LevelLoader {
	static TextureHelper textures;
	/**
	public static void LoadLvl(int number) {
		textures = TextureHelper.getInstance();
		FileHandle handle = Gdx.files.absolute("F:/Android/graviball/android/assets/level/"+ number + ".lvl");                  
        Json json = new Json();
        Level level = json.fromJson(Level.class, handle.readString());
        for(Item item: level.items) {
        	loadItems(item);
        }	
    }

	private static void loadRotateSpike(Item item, CustomActor actor) {
		actor.tr = textures.rotate_spike;
		actor.setSize(600, 600);		
	}
	private static void loadSpike(Item item, CustomActor actor) {
		actor.tr = textures.spike;
		actor.setSize(40, 200);	
		actor.setRotation(item.params.get(1));
		actor.setOrigin(20, 100);		
	}
	
	public static void loadItems(Item item) {
		CustomActor actor = new CustomActor();
		actor.objType = item.type;
		for(int i=0; i < item.params.size(); i++) actor.params.add(item.params.get(i));
    	actor.setPosition(item.x*20, item.y*20);
		if(item.type.equals("wall"))loadWall(item, actor);	
		else if(item.type.equals("coin"))loadCoin(item, actor);	
		else if(item.type.equals("teleport"))loadTeleport(item, actor);		
		else if(item.type.equals("modifier"))loadModifier(item, actor);
		else if(item.type.equals("laser"))loadLaser(item, actor);
		else if(item.type.equals("spike"))loadSpike(item, actor);
		else if(item.type.equals("rotate_spike"))loadRotateSpike(item, actor);
		actor.addListener(new DelClickListener());
	    MainScreen.stage.addActor(actor);
	} */
}
