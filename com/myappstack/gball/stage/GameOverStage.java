package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
//import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.actors.BgGameStage;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.screens.StartScreen;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class GameOverStage extends Stage{

	private MyGballGame gBallGame;
	private Integer score;
	private Label scoreLabel;
	private Image bg;
	private Texture bgImg;
	
	private Vector2 screenDims;
	
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	
	public GameOverStage(MyGballGame game, int score){
		this.gBallGame = game;
		this.score = score;
		
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		
		setupCamera();
		setupWorld();
		setupScore();
	}
	
	
	private void setupScore() {
		
		Vector2 dims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH/3,Constants.TOP_MARGIN-2), camera);
		Vector2 pos = WorldUtils.viewportToScreen(new Vector2(
				(Constants.VIEWPORT_WIDTH/2)-(Constants.VIEWPORT_WIDTH/6),Constants.VIEWPORT_HEIGHT/2), camera);
		//BitmapFont font = new BitmapFont();
		BitmapFont font = new BitmapFont(Gdx.files.internal("tek-hed/nbs.fnt"),
                Gdx.files.internal("tek-hed/nbs.png"), false);
		
		float scale = 5.0f;
		
		Table table = new Table();
		table.setPosition(0, screenDims.y);
		table.setWidth(screenDims.x);
		//table.setHeight(margins.x);
		//table.align(Align.center|Align.top);
		
		LabelStyle style = new LabelStyle();
		style.font = font;
		scoreLabel = new Label("SCORE : "+score.toString(), style);
		//scoreLabel.setScale(scale);
		scoreLabel.setFontScale(scale);
		scoreLabel.setText("SCORE : "+score.toString());
		table.add(scoreLabel);
		
		addActor(bg);
		addActor(scoreLabel);
		//addActor(table);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Keys.BACK){
			gBallGame.setScreen(new StartScreen(gBallGame));
		}
		return false;
	}
	
	private void setupWorld() {
		//world = WorldUtils.createWorld();
		
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT), camera);
		bgImg = new Texture(Gdx.files.internal("scoreboard_bg.jpg"));
		bg = new Image(bgImg);
		bg.setPosition(0, 0);
		bg.setSize(screenDims.x, screenDims.y);
		//bgWhite = new BgWhiteRep(screenDims, margins,camera);
		//addActor(bgWhite);
	}

	private void setupCamera() {
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}

}
