package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
	private Vector2 screenDims,pbtnDims;
	
	
	private StartScreenBackground sbg;
	private TextButton playBtn;
	private Image sleft,sright;
	
	
	private MyGballGame gGame;
	
	public StartStage(MyGballGame gGame){
		setupCamera();
		
		this.gGame = gGame;
		Gdx.input.setInputProcessor(this);
		setUpUi();	
	}
	
	private void setUpUi(){
		
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT),camera);
		pbtnDims = WorldUtils.viewportToScreen(new Vector2(Constants.PLAYBTN_WID, Constants.PLAYBTN_HIG),camera);

		
		Texture sleftT = new Texture(Gdx.files.internal("splash-left.png"));
		Texture srightT = new Texture(Gdx.files.internal("splash-right.png"));


		sleft = new Image(sleftT);
		sright = new Image(srightT);
		sbg = new StartScreenBackground(new Vector2(0,0), screenDims);

		
		int commonWidth = (int)screenDims.x/2;
		int smallWidth = (int) (screenDims.x*0.35f);

		
		

		sleft.setSize(screenDims.x/2, screenDims.y);
		sleft.setPosition(0,0);
		sright.setSize(screenDims.x/2, screenDims.y);
		sright.setPosition(screenDims.x/2,0);
		
		
		//rBall.setPosition(0, 0);
		//rBall.setSize(100, 100);;
		
		Texture btnUp = new Texture(Gdx.files.internal("button normal.png"));
		Texture btndown = new Texture(Gdx.files.internal("button pressed.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("tek-hed/nbs.fnt"),
                Gdx.files.internal("tek-hed/nbs.png"), false);
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = new Image(btnUp).getDrawable();
		tbs.down = new Image(btndown).getDrawable();
		tbs.font = font;
		int plyBtnHeight = WorldUtils.getProportionalHeight(smallWidth,new Vector2(btnUp.getWidth(), btnUp.getHeight()) );
		playBtn = new TextButton("", tbs);
		playBtn.setPosition(screenDims.x/2 - pbtnDims.x/2, screenDims.y / 4 - pbtnDims.y);
		playBtn.setWidth(pbtnDims.x);
		playBtn.setHeight(pbtnDims.y);


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
				//changeScreen();
				slideOut();
			}
		});
		
		addActor(sbg);
		addActor(sleft);
		addActor(sright);
		addActor(playBtn);
		
	}

	private void slideOut(){
		sright.addAction(Actions.moveTo(screenDims.x,0,1.0f));
		sleft.addAction(Actions.sequence(Actions.moveTo(-screenDims.x / 2, 0,1.0f), completedSlide));
	}

	Action completedSlide = new Action() {
		@Override
		public boolean act(float delta) {
			changeScreen();
			return true;
		}
	};
	
	
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
