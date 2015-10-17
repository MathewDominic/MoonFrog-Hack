package com.myappstack.gball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.screens.StartScreen;

public class MyGballGame extends Game {
	
	//Screen gScreen;
	public ActionResolver actionResolver;
	
	//public  MyGballGame(ActionResolver actionResolver) {
	public  MyGballGame() {
		// TODO Auto-generated constructor stub
		//this.actionResolver = actionResolver;
	}
	
	@Override
	public void create () {
		//gScreen = new GameScreen(this);
		//gScreen = new StartScreen(this);
		setScreen(new StartScreen(this));
	}
	
	
	public MyGballGame getInst(){
		return this;
	}

}
