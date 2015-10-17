package com.myappstack.gball.actors.startscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.myappstack.gball.utils.WorldUtils;

public class PlayButton extends Actor{
	private Sprite spr;
	private Vector2 screenDims;
	private Vector2 oSpriteDims;
	private Vector2 nSpriteDims;
	private OrthographicCamera camera;
	
	public PlayButton(Vector2 screenDims,OrthographicCamera camera){
		this.screenDims = screenDims;
		this.camera = camera;
		spr = new Sprite (new Texture(Gdx.files.internal("play.png")));
		oSpriteDims = new Vector2(spr.getWidth(),spr.getHeight());
		setSize();
		setPosition();
				
	}
	
	public void setPosition(){
		int x = (int)(screenDims.x/2 - nSpriteDims.x/2);
		int y = (int)(screenDims.y/4 + nSpriteDims.y/2);
		spr.setPosition(x, y);
		
		//Vector2 vPos = WorldUtils.screenToViewport(new Vector2(x,y), camera);
		//Vector2 vDims = WorldUtils.screenToViewport(nSpriteDims, camera);
		//this.setBounds(vPos.x, vPos.y, vDims.x, vDims.y);
		setBounds(x, y, nSpriteDims.x, nSpriteDims.y);
	}
	
	public void setSize(){
		int gNameWid = (int)screenDims.x/4;
		int gNameHei = WorldUtils.getProportionalHeight(gNameWid, oSpriteDims);
		spr.setSize(gNameWid, gNameHei);
		nSpriteDims = new Vector2(gNameWid,gNameHei);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		spr.draw(batch);
	}
}
