package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.PowerupsManager;
import com.myappstack.gball.actors.Ball;
import com.myappstack.gball.actors.BallT;
import com.myappstack.gball.actors.BgGameStage;
import com.myappstack.gball.actors.Food;
import com.myappstack.gball.actors.Weapon;
import com.myappstack.gball.actors.Food.FoodType;
import com.myappstack.gball.actors.Line;
import com.myappstack.gball.screens.GameOverScreen;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.screens.StartScreen;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;
//import com.sun.org.apache.bcel.internal.generic.FADD;

public class GameStage extends Stage {

	private static final int VIEWPORT_WIDTH = Constants.VIEWPORT_WIDTH;
	private static final int VIEWPORT_HEIGHT = Constants.VIEWPORT_HEIGHT;
	
	private PowerupsManager pm;
	private MyGballGame gballGame;

	private World world;
	//private Body wallLeft, wallRight, wallBottom, wallTop;
	private Vector2 touchStart;
	private Vector2 touchEnd;

	private Line line;
	private Food food;
	private Ball blueBall;
	//private Ball redBall;
	private BgGameStage bgWood;
	private Texture gun1t, gun2t, electric1t, electric2t, spike1t, spike2t, flamethrower1t, flamethrower2t;
	private Vector2 margins,screenDims, wepDims;
	
	private Integer score;
	private Table table;
	private Label scoreLabel;
	private Label modeLabel;
	private Label timeLabel;
	
	private Image pScoreBg, weapon,gun1, gun2, electric1, electric2, spike1, spike2, flamethrower1, flamethrower2;
	private Button pReplay;
	private Button pMenu;
	private Label pScore;
	private long gun_active_time, fire_active_time, electric_active_time, spike_active_time, startTime, currentTime;
	public Weapon wep;

	//private ParticleEffect redExpo, blueExpo;
	private Sound breakBallSound;
	private Sound pointSound;
	private boolean drawDestroyPeffect, activeGun, activeSpike, activeElectric, activeFire;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	
	private boolean gOver;

	public GameStage( MyGballGame game)
	{
		this.gballGame = game;
		this.gOver = false;
		pm = new PowerupsManager();
		setupCamera();
    	setupWorld();
    	
    	setupBall();
    	setupFood(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2);
    	setupScore();
    	Gdx.input.setInputProcessor(this);
        renderer = new Box2DDebugRenderer();
        
        breakBallSound = Gdx.audio.newSound(Gdx.files.internal("sounds/glass_break.ogg"));
        pointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/point.mp3"));
        
        /*redExpo = new ParticleEffect();
        redExpo.load(Gdx.files.internal("effects/expored.p"), Gdx.files.internal("effects"));
        redExpo.allowCompletion();
        
        blueExpo = new ParticleEffect();
        blueExpo.load(Gdx.files.internal("effects/expoblue.p"), Gdx.files.internal("effects"));
        blueExpo.allowCompletion(); */
        
        drawDestroyPeffect = false;
		activeElectric=false;
		activeGun=false;
		activeSpike=false;
		activeFire=false;
        
        touchStart = touchEnd = null;
		startTime= TimeUtils.millis();
		gun_active_time=2500;
		fire_active_time=2500;
		electric_active_time=2500;
		spike_active_time=2500;


	}


	@Override
	public void act(float delta)
	{
		super.act(delta);
		// Fixed timestep
		accumulator += delta;
		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
		currentTime = (TimeUtils.millis() - startTime)/1000;
		if(currentTime == 60)
			//showGameOverPoupUp();
		if(this.gOver){
			return;
		}
		if(TimeUtils.timeSinceMillis(gun_active_time) < 2000) {
			activeGun=true;
			gun1.remove();
			System.out.println("Gun Active");
			gun1t = new Texture(Gdx.files.internal("gun2.png"));
			gun1 = new Image(gun1t);
			gun1.setSize(wepDims.x, wepDims.y);
			gun1.setPosition(screenDims.x-wepDims.x, screenDims.y-wepDims.y);
			//wep.change(true, Weapon.WeaponType.RED);
			//addActor(gun1);

		}
		else
		{
			if(gun1 == null || activeGun == true)
			{
				gun1t = new Texture(Gdx.files.internal("gun1.png"));
				gun1 = new Image(gun1t);
				gun1.setSize(wepDims.x, wepDims.y);
				gun1.setPosition(screenDims.x-wepDims.x, screenDims.y-wepDims.y);
				addActor(gun1);
				//wep.change(false, Weapon.WeaponType.RED);
				activeGun=false;
			}
		}
		if(TimeUtils.timeSinceMillis(spike_active_time) < 2000) {
			activeSpike = true;
			spike1.remove();
			System.out.println("Spike Active");
			spike1t = new Texture(Gdx.files.internal("spike2.png"));
			spike1 = new Image(spike1t);
			spike1.setSize(wepDims.x, wepDims.y);
			spike1.setPosition(screenDims.x - wepDims.x, 0);
			addActor(spike1);
		}
		else
		{
			if(spike1 == null || activeSpike) {
				spike1t = new Texture(Gdx.files.internal("spike1.png"));
				spike1 = new Image(spike1t);
				spike1.setSize(wepDims.x, wepDims.y);
				spike1.setPosition(screenDims.x-wepDims.x, 0);
				addActor(spike1);
				activeSpike=false;
			}
		}
		if(TimeUtils.timeSinceMillis(electric_active_time) < 2000) {
			activeElectric = true;
			electric1.remove();
			System.out.println("Electric Active");
			electric1t = new Texture(Gdx.files.internal("electric2.png"));
			electric1 = new Image(electric1t);
			electric1.setSize(wepDims.x, wepDims.y);
			electric1.setPosition(0, screenDims.y-wepDims.y);
			addActor(electric1);
		}
		else
		{
			if(electric1 == null || activeElectric) {
				electric1t = new Texture(Gdx.files.internal("electric1.png"));
				electric1 = new Image(electric1t);
				electric1.setSize(wepDims.x, wepDims.y);
				electric1.setPosition(0, screenDims.y-wepDims.y);
				addActor(electric1);
				activeElectric = false;
			}
		}
		if(TimeUtils.timeSinceMillis(fire_active_time) < 2000) {
			activeFire = true;
			flamethrower1.remove();
			//flamethrower2.remove();
			System.out.println("Fire Active");
			flamethrower1t = new Texture(Gdx.files.internal("flamethrowe2.png"));
			flamethrower2t = new Texture(Gdx.files.internal("firegif.gif"));
			flamethrower1 = new Image(flamethrower1t);

			flamethrower2 = new Image(flamethrower2t);


			flamethrower1.setSize(wepDims.x, wepDims.y);

			flamethrower1.setPosition(0, 0);
			addActor(flamethrower1);
			addActor(flamethrower2);
		}
		else
		{
			if(flamethrower2 != null)
				flamethrower2.remove();
			if(flamethrower1 == null || activeFire) {
				flamethrower1t = new Texture(Gdx.files.internal("flamethrower1.png"));
				flamethrower1 = new Image(flamethrower1t);
				flamethrower1.setSize(wepDims.x, wepDims.y);
				flamethrower1.setPosition(0, 0);
				addActor(flamethrower1);
				activeFire = false;
			}
		}
		
		// TODO: Implement interpolation
		
		handleLineCollision();
		//blueBall.getBounds().overlaps(redBall.getBounds())

		//COLISION
		/*
		if(blueBall.collidedWith(redBall.getPos())){
			if(blueBall.getState() == Ball.State.GOTHROUGH || redBall.getState() == Ball.State.GOTHROUGH){
				System.out.println("not out : go through");
			}
			else{
				System.out.println("game over");
				//this.gOver = true;
				showGameOverPoupUp();
				//this.gballGame.setScreen(new GameOverScreen(gballGame,score));
			}
			
		} */
		if (blueBall.getBounds().overlaps(food.getBounds()))
		{
			if(food.getType() == FoodType.BLUE)
			{
				score++;
				scoreLabel.setText(score.toString());
				blueBall.gotFoodAnimation();
				pointSound.play(1f);
			}
			else
			{
				/*expo.setPosition(food.pos.x, food.pos.y);
				redExpo.update(delta);
				redExpo.start(); */
				breakBallSound.play(1.0f);
			}
			System.out.println("Collided");
			if(food.getType() == FoodType.BLUE)
			{
				//t = new Texture(Gdx.files.internal("gun-weapon-picked.png"));
				//Image weapon = new Image(t);
				//weapon.setPosition(705, 388);
				//System.out.println(Gdx.graphics.getHeight() + " " + Gdx.graphics.getWidth());
				//weapon.setSize(208,150);
				gun_active_time=TimeUtils.millis();
				//addActor(weapon);
			}
			else if(food.getType() == FoodType.RED)
			{
				//t = new Texture(Gdx.files.internal("flamethrower-picked.png"));
				//Image weapon = new Image(t);
				//weapon.setPosition(100,100);
				//System.out.println(Gdx.graphics.getHeight() + " " + Gdx.graphics.getWidth());
				//weapon.setSize(208,150);
				fire_active_time=TimeUtils.millis();
				//addActor(weapon);
			}
			else if(food.getType() == FoodType.SPIKE)
			{
				//t = new Texture(Gdx.files.internal("spike-weapon-picked.png"));
				//Image weapon = new Image(t);
				//weapon.setPosition(705,100);
				//System.out.println(Gdx.graphics.getHeight() + " " + Gdx.graphics.getWidth());
				//weapon.setSize(208,150);
				spike_active_time=TimeUtils.millis();
				//addActor(weapon);
			}
			else if(food.getType() == FoodType.ELECTRIC)
			{
				//t = new Texture(Gdx.files.internal("electric-weapon-picked.png"));
				//Image weapon = new Image(t);
				//weapon.setPosition(100, 388);
				//System.out.println(Gdx.graphics.getHeight() + " " + Gdx.graphics.getWidth());
				//weapon.setSize(208,150);
				electric_active_time=TimeUtils.millis();
				//addActor(weapon);
			}

			int newXpos = MathUtils.random(Constants.MARGIN+1, Constants.VIEWPORT_WIDTH
					- Constants.MARGIN- Constants.FOOD_WIDTH - 1);
			int newYpos = MathUtils.random(Constants.MARGIN +1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT- Constants.TOP_MARGIN - 1);
			int foodType = MathUtils.floor(MathUtils.random(0,4));
			System.out.println(foodType);
			if( foodType == 0)
				food.change(newXpos, newYpos,FoodType.RED);
			else if(foodType == 1)
				food.change(newXpos, newYpos,FoodType.BLUE);
			else if(foodType == 2)
				food.change(newXpos, newYpos,FoodType.ELECTRIC);
			else
				food.change(newXpos, newYpos,FoodType.SPIKE);

			//float fontScale = (dims.y-10)/64;
			//Vector2 dims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH-3*Constants.MARGIN,Constants.GP_BOARD), camera);

			//float fontScale = (dims.y-10)/64;

			//scoreLabel.setAlignment(Align.right);

			/*modeLabel = new Label("Mode : Normal",style);
			modeLabel.setText("Mode : Normal");
			modeLabel.setFontScale(fontScale);
			modeLabel.setColor(Color.WHITE); */



		}
		/*else if (redBall.getBounds().overlaps(food.getBounds())) {
			if(food.getType() == FoodType.RED){
				score++;
				scoreLabel.setText(score.toString());
				redBall.gotFoodAnimation();
				pointSound.play(1f);
			}
			else{
				blueExpo.setPosition(food.pos.x, food.pos.y);
				blueExpo.update(delta);
				blueExpo.start();
				breakBallSound.play(1.0f);
			}
			System.out.println("Collided");
			int newXpos = MathUtils.random(Constants.MARGIN + 1, Constants.VIEWPORT_WIDTH
					- Constants.FOOD_WIDTH - Constants.MARGIN - 1);
			int newYpos = MathUtils.random(Constants.MARGIN +1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT - Constants.TOP_MARGIN - 1);
			
			
			food.change(newXpos, newYpos,FoodType.BLUE);

		} */

	}

	private void setupScore() {
		score = 0;
		Vector2 dims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH-3*Constants.MARGIN,Constants.GP_BOARD), camera);
		Vector2 pos = WorldUtils.viewportToScreen(new Vector2(Constants.MARGIN,Constants.VIEWPORT_HEIGHT -(Constants.GP_BOARD + Constants.MARGIN+1) ), camera);
		
		float fontScale = (dims.y-10)/64;
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/hobostd.fnt"),
                Gdx.files.internal("fonts/hobostd.png"), false);
		LabelStyle style = new LabelStyle();
		style.font = font;
		scoreLabel = new Label(score.toString(), style);
		scoreLabel.setText(score.toString());
		scoreLabel.setFontScale(fontScale);
		scoreLabel.setColor(Color.WHITE);
		//scoreLabel.setAlignment(Align.right);
		
		modeLabel = new Label("Mode : Normal",style);
		modeLabel.setText("Mode : Normal");
		modeLabel.setFontScale(fontScale);
		modeLabel.setColor(Color.WHITE);
		
		table = new Table();
		//table.setDebug(true);
		table.setSize(dims.x, dims.y);
		table.setPosition(pos.x, pos.y);
		//table.center();
		//table.add(scoreLabel).expandX().align(Align.right).height(dims.y);
		//table.add(modeLabel).expandX().height(dims.y);
		
		addActor(table);
	}



	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// return super.touchDown(screenX, screenY, pointer, button);
		resetLine();
		touchStart = new Vector2(screenX, screenY);
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// return super.touchUp(screenX, screenY, pointer, button);
		touchEnd = new Vector2(screenX, screenY);
		if(!this.gOver){
			drawLineActor();
		}
		
		return super.touchUp(screenX, screenY, pointer, button);
	}

	public void drawLineActor() {
		if (touchStart != null && touchEnd != null) {
			Vector2 start_temp = screenToViewport(touchStart);
			Vector2 end_temp = screenToViewport(touchEnd);
						
			line = new Line(camera, start_temp,end_temp);
			addActor(line);
		}
	}

	public void resetLine() {
		if (line != null) {
			line.removeLine(world);
			line.remove();
			line = null;
			touchStart = touchEnd = null;
		}
	}

	
	
	public void handleLineCollision(){
		if(line != null){
			if(line.getDistanceFromLine(blueBall.getPos())<= Constants.BALL_RADIUS){
				if(line.inSegmentIntrRegion(blueBall.getPos())){
					System.out.println("Line-Blue Ball collision");
					blueBall.setDirection(WorldUtils.reflect(blueBall.getDirection(), line.getNormalVector()));
					resetLine();
				}
			}
		}
		
		/*if(line != null){
			if(line.getDistanceFromLine(redBall.getPos())<= Constants.BALL_RADIUS){
				if(line.inSegmentIntrRegion(redBall.getPos())){
					System.out.println("Line-Red Ball collision");
					redBall.setDirection(WorldUtils.reflect(redBall.getDirection(), line.getNormalVector()));
					resetLine();
				}
			}
		} */
	}
	
	
	
	private void showGameOverPoupUp(){
		Vector2 screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, 
				Constants.VIEWPORT_HEIGHT),camera);
		
		Texture tPScoreBg = new Texture(Gdx.files.internal("whitebg.png"));
		Texture tReplay = new Texture(Gdx.files.internal("score_replay.png"));
		Texture tMenu = new Texture(Gdx.files.internal("score_menu.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/hobostd.fnt"),
                Gdx.files.internal("fonts/hobostd.png"), false);
		
		//redBall.remove();
		blueBall.remove();
		food.remove();
		table.remove();
		
		pScoreBg = new Image(tPScoreBg);
		//pScoreBg.setSize(screenDims.x*.75f, screenDims.y*.75f);
		//pScoreBg.setPosition(screenDims.x/8, screenDims.y/8);
		//addActor(pScoreBg);
		float sq = screenDims.x*.75f;
		
		LabelStyle style = new LabelStyle();
		style.font = font;
		pScore = new Label(score.toString(), style);
		float fontScale = sq > 64 ? sq/(4*64) : 1;
		pScore.setFontScale(fontScale, fontScale);
		
		ButtonStyle bPReplayStyle = new ButtonStyle();
		Image tmp1 = new Image(tReplay);
		pReplay = new Button(tmp1.getDrawable(),tmp1.getDrawable(),tmp1.getDrawable());
		Image tmp2 = new Image(tMenu);
		pMenu = new Button(tmp2.getDrawable(),tmp2.getDrawable(),tmp2.getDrawable());
		
		Table pOptionsTable = new Table();
		pOptionsTable.add(pReplay).expandX();
		pOptionsTable.add(pMenu).expandX();
		
		Table pTable = new Table();
		
		pTable.setSize(sq, sq);
		//pTable.setPosition(screenDims.x/2 - sq/2, screenDims.y/2 - sq/2);
		pTable.setPosition(screenDims.x/2 - sq/2, screenDims.y);
		pTable.setBackground(pScoreBg.getDrawable());
		
		//pTable.add(pScore).height(sq/4).align(Align.center);
		pTable.row();
		pTable.add(pOptionsTable);
		pTable.setDebug(true);
		pTable.setPosition(screenDims.x/2 - sq/2, screenDims.y/2 - sq/2);
		addActor(pTable);
		
		//addActor(pScoreBg);
		
		
		//pTable.addAction(Actions.sequence(Actions.alpha(0f),Actions.fadeIn(4f)));
		/*pTable.addAction(
				Actions.sequence(
						Actions.moveTo(screenDims.x/2 - sq/2, screenDims.y/2 - sq/2 , 0.8f)
						)
		);*/
		
		pReplay.addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			        System.out.println("down");
			        replay();
			        return true;
			    }
		});
		
		pMenu.addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			        System.out.println("down");
			        goToMainMenu();
			        return true;
			    }
		});
		
		pReplay.addAction(Actions.sequence(Actions.alpha(0f),Actions.fadeIn(3f)));
		pMenu.addAction(Actions.sequence(Actions.alpha(0f),Actions.fadeIn(3f)));
		
	}
	
	private void goToMainMenu(){
		this.gballGame.setScreen(new StartScreen(this.gballGame));
	}
	
	private void replay(){
		this.gballGame.setScreen(new GameScreen(this.gballGame));
	}
	
	 
	private void setupFood(int x, int y) {
		food = new Food(world, camera, x, y,Food.FoodType.BLUE);
		addActor(food);	
	}
	/*private void activateWeapon()
	{
		weapon = new Weapon(world, camera, Weapon.WeaponType.SPIKE);
	}*/

	private void setupBall() {
		Texture tBlue = new Texture(Gdx.files.internal("b2.png"));
		Texture tRed = new Texture(Gdx.files.internal("b1.png"));
		blueBall = new Ball(tBlue,camera,margins, new Vector2(1,1), Constants.FOOD_WIDTH+1, Constants.FOOD_HEIGHT+Constants.MARGIN);
		//redBall = new Ball(tRed,camera,margins, new Vector2(1,-1), Constants.FOOD_WIDTH+1, Constants.VIEWPORT_HEIGHT-Constants.FOOD_HEIGHT-Constants.MARGIN);
		
		//addActor(redBall);
		addActor(blueBall);
	}


	private void setupWorld() {
		world = WorldUtils.createWorld();
		margins = WorldUtils.viewportToScreen(new Vector2(Constants.TOP_MARGIN, Constants.MARGIN), camera);
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT), camera);
		wepDims = WorldUtils.viewportToScreen(new Vector2(Constants.WEP_SIZE,Constants.WEP_SIZE), camera);
		bgWood = new BgGameStage(screenDims, margins,camera);
		addActor(bgWood);
	}

	private void setupCamera() {
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}
	

	@Override
	public void draw() {
		super.draw();
		getBatch().begin();
		/*blueExpo.update(Gdx.graphics.getDeltaTime());
		blueExpo.draw(getBatch());
		
		redExpo.update(Gdx.graphics.getDeltaTime());
		redExpo.draw(getBatch()); */
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/hobostd.fnt"),
				Gdx.files.internal("fonts/hobostd.png"), false);
		font.draw(getBatch(), String.valueOf(60-currentTime), 200, 200);

		getBatch().end();
		//renderer.render(world, camera.combined);
	}

	public Vector2 screenToViewport(Vector2 v) {
		Vector3 vtemp = camera.unproject(new Vector3(v.x, v.y, 0));
		return new Vector2(vtemp.x, vtemp.y);
	}

}
