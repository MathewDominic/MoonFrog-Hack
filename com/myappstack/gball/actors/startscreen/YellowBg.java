package com.myappstack.gball.actors.startscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class YellowBg extends Actor {
	private Texture bg;
	private Vector2 pos,dims;
	
	public YellowBg(Vector2 pos,Vector2 dims){
		this.pos = pos;
		this.dims = dims;
		bg = new Texture(Gdx.files.internal("yellowbg.png"));
		bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat); 
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(bg, pos.x, pos.y, dims.x, dims.y);
	}
}
