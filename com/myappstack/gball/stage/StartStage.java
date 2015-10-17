package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.actors.startscreen.PlayButton;
import com.myappstack.gball.actors.startscreen.StartScreenBackground;
import com.myappstack.gball.actors.startscreen.YellowBg;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.screens.StartScreen;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class StartStage extends Stage {
	
	private OrthographicCamera camera;
	private Vector2 screenDims;
	
	
	private PlayButton playButton;
	private StartScreenBackground sbg;
	private Skin buttonSkin;
	private TextButton playBtn;
	private Image gName,clickHereTo;
	private Image bBall,rBall;
	
	
	private MyGballGame gGame;
	
	public StartStage(MyGballGame gGame){
		setupCamera();
		
		this.gGame = gGame;
		Gdx.input.setInputProcessor(this);
		setUpUi();	
	}
	
	private void setUpUi(){
		
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT),camera);
		
		Texture bBallT = new Texture(Gdx.files.internal("b2.png"));
		Texture rBallT = new Texture(Gdx.files.internal("b1.png"));
		Texture gNameT = new Texture(Gdx.files.internal("slicetheway.png"));
		Texture clcickT = new Texture(Gdx.files.internal("clickhereto.png"));

		
		bBall = new Image(bBallT);
		rBall = new Image(rBallT);
		gName = new Image(gNameT);
		sbg = new StartScreenBackground(new Vector2(0,0), screenDims);
		clickHereTo = new Image(clcickT);
		
		
		int commonWidth = (int)screenDims.x/2;
		int smallWidth = (int) (screenDims.x*0.35f);
		int xPad = (int)(screenDims.x/2 - commonWidth/2);
		int xPadSmall = (int)(screenDims.x/2 - smallWidth/2);
		int gNameHeigh = WorldUtils.getProportionalHeight(commonWidth, new Vector2(gNameT.getWidth(),gNameT.getHeight()));
		int ballWidth = 3*commonWidth/5;
		int ballHeight = WorldUtils.getProportionalHeight(ballWidth, new Vector2(bBallT.getWidth(),bBallT.getHeight()));
		int clickHeight = WorldUtils.getProportionalHeight(smallWidth, new Vector2(clcickT.getWidth(),clcickT.getHeight()));
		
		
		
		gName.setSize(commonWidth, gNameHeigh);
		gName.setPosition(xPad, screenDims.y/2);
		
		rBall.setSize(ballWidth, ballHeight);
		rBall.setPosition((int)(screenDims.x/2-0.75*ballWidth), screenDims.y/2+gNameHeigh+1);
		bBall.setSize(ballWidth, ballHeight);
		bBall.setPosition((int)(screenDims.x/2-0.25*ballWidth), screenDims.y/2+gNameHeigh+1);
		
		
		//rBall.setPosition(0, 0);
		//rBall.setSize(100, 100);;
		
		Texture btnUp = new Texture(Gdx.files.internal("start.png"));
		Texture btndown = new Texture(Gdx.files.internal("start.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("tek-hed/nbs.fnt"),
                Gdx.files.internal("tek-hed/nbs.png"), false);
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = new Image(btnUp).getDrawable();
		tbs.down = new Image(btndown).getDrawable();
		tbs.font = font;
		int plyBtnHeight = WorldUtils.getProportionalHeight(smallWidth,new Vector2(btnUp.getWidth(), btnUp.getHeight()) );
		playBtn = new TextButton("", tbs);
		playBtn.setPosition(xPadSmall, screenDims.y/8 +plyBtnHeight );
		playBtn.setWidth(smallWidth);
		playBtn.setHeight(plyBtnHeight);
		
		clickHereTo.setSize(smallWidth, clickHeight);
		clickHereTo.setPosition(xPadSmall, screenDims.y/8+2*plyBtnHeight);
		clickHereTo.setColor(1, 1, 1, 0.4f);

		playBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); // ** Usually used to start
													// Game, etc. **//
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("my app", "Released");
				changeScreen();
			}
		});
		
		addActor(sbg);
		addActor(bBall);
		addActor(rBall);
		addActor(gName);
		addActor(clickHereTo);
		addActor(playBtn);
		
	}
	
	
	private void changeScreen(){
		gGame.setScreen(new GameScreen(gGame));
	}
	
	private void setupCamera() {
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}

}
