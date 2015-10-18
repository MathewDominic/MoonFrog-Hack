package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.Align;
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
	private Vector2 touchStart;
	private Vector2 touchEnd;

	private Line line;
	private Food food;
	private Ball blueBall;
	private Weapon electric, flame, gun, spike;

	private BgGameStage bgWood;
	private Vector2 margins,screenDims, wepDims;
	
	private Integer score;
	private Table table;
	private Label scoreLabel;
	private Label timeLabel;

	private long gun_active_time, fire_active_time, electric_active_time, spike_active_time, startTime, currentTime;

	private Image leftScore, rightScore;

	//private ParticleEffect redExpo, blueExpo;
	//private boolean drawDestroyPeffect, activeGun, activeSpike, activeElectric, activeFire;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;
	private int timeActive = 5000;
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
		setUpWep();
    	Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);

		renderer = new Box2DDebugRenderer();
        
        touchStart = touchEnd = null;
		startTime= TimeUtils.millis();

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
			gameOver();

		if(this.gOver){
			return;
		}
		
		// TODO: Implement interpolation
		
		handleLineCollision();

		if (blueBall.getBounds().overlaps(food.getBounds()))
		{
			if(food.getType() == FoodType.GUN)
			{
				score++;
				scoreLabel.setText(score.toString());
				blueBall.gotFoodAnimation();
				//pointSound.play(1f);
			}

			System.out.println("Collided");
			if(food.getType() == FoodType.GUN)
			{
				gun_active_time=TimeUtils.millis();
			}
			else if(food.getType() == FoodType.FLAME)
			{
				fire_active_time=TimeUtils.millis();
			}
			else if(food.getType() == FoodType.SPIKE)
			{

				spike_active_time=TimeUtils.millis();
			}
			else if(food.getType() == FoodType.ELECTRIC)
			{
				electric_active_time=TimeUtils.millis();
			}

			int newXpos = MathUtils.random(Constants.MARGIN+1, Constants.VIEWPORT_WIDTH
					- Constants.MARGIN- Constants.FOOD_WIDTH - 1);
			int newYpos = MathUtils.random(Constants.MARGIN +1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT- Constants.TOP_MARGIN - 1);
			int foodType = MathUtils.floor(MathUtils.random(0,4));
			System.out.println(foodType);
			if( foodType == 0)
				food.change(newXpos, newYpos,FoodType.FLAME);
			else if(foodType == 1)
				food.change(newXpos, newYpos,FoodType.GUN);
			else if(foodType == 2)
				food.change(newXpos, newYpos,FoodType.ELECTRIC);
			else
				food.change(newXpos, newYpos,FoodType.SPIKE);





		}

		if(TimeUtils.timeSinceMillis(gun_active_time) < timeActive) {
			gun.changeState(true);
		}
		else if(gun.isActive()) {
			gun.changeState(false);

		}
		if(TimeUtils.timeSinceMillis(spike_active_time) < timeActive) {
			spike.changeState(true);
		}
		else if(spike.isActive()) {
			spike.changeState(false);

		}
		if(TimeUtils.timeSinceMillis(electric_active_time) < timeActive) {
			electric.changeState(true);
		}
		else if( electric.isActive()) {
			electric.changeState(false);
		}
		if(TimeUtils.timeSinceMillis(fire_active_time) < timeActive) {
			flame.changeState(true);
		}
		else if(flame.isActive()) {
			flame.changeState(false);
		}

		if( blueBall.getBounds().overlaps(gun.getBounds()) && gun.changeScore()){
			score = score + 3;
			blueBall.nearWep(Weapon.WeaponType.GUN);
		}
		if( blueBall.getBounds().overlaps(flame.getBounds()) && flame.changeScore()  ){
			score = score + 3;
			blueBall.nearWep(Weapon.WeaponType.FLAME);
		}
		if( blueBall.getBounds().overlaps(spike.getBounds()) && spike.changeScore() ){
			score = score + 3;
			blueBall.nearWep(Weapon.WeaponType.SPIKE);
		}
		if( blueBall.getBounds().overlaps(electric.getBounds()) && electric.changeScore()){
			score = score + 3;
			blueBall.nearWep(Weapon.WeaponType.ELECTRIC);
		}

		scoreLabel.setText(score.toString());


		timeLabel.setText(String.valueOf(60-currentTime));

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

		timeLabel = new Label("",style);
		timeLabel.setFontScale(fontScale);
		timeLabel.setColor(Color.WHITE);
		
		table = new Table();
		//table.setDebug(true);
		table.setSize(screenDims.x, dims.y);
		table.setPosition(0, pos.y);
		//table.center();
		table.add(timeLabel).expandX().align(Align.center).height(dims.y);
		table.add(scoreLabel).expandX().align(Align.center).height(dims.y);

		
		addActor(table);

		Texture sleftT = new Texture(Gdx.files.internal("score-left.png"));
		Texture srightT = new Texture(Gdx.files.internal("score-right.png"));

		leftScore = new Image(sleftT);
		rightScore = new Image(srightT);

		leftScore.setSize(screenDims.x/2,screenDims.y);
		rightScore.setSize(screenDims.x/2,screenDims.y);

		leftScore.setPosition(-screenDims.x / 2, 0);
		rightScore.setPosition(screenDims.x,0);
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
	
	
	private void gameOver(){
		blueBall.remove();
		scoreLabel.remove();
		timeLabel.remove();
		table.remove();

		gun.remove();
		spike.remove();
		flame.remove();
		electric.remove();

		addActor(leftScore);
		addActor(rightScore);

		leftScore.addAction(Actions.sequence(Actions.moveTo(0, 0, 1.0f), dispScore));
		rightScore.addAction(Actions.sequence(Actions.moveTo(screenDims.x/2,0,1.0f)));

		leftScore.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				replay();
				return false;
			}
		});
	}

	Action dispScore = new Action() {
		@Override
		public boolean act(float delta) {
			Vector2 dims = WorldUtils.viewportToScreen(new Vector2(30,30), camera);
			addActor(scoreLabel);
			float fontScale = (dims.y-10)/64;
			scoreLabel.setFontScale(fontScale);
			scoreLabel.setPosition(screenDims.x / 4, screenDims.y / 2 - 10);

			return false;
		}
	};
	
	private void goToMainMenu(){
		this.gballGame.setScreen(new StartScreen(this.gballGame));
	}
	
	private void replay(){
		this.gballGame.setScreen(new GameScreen(this.gballGame));
	}
	
	 
	private void setupFood(int x, int y) {
		food = new Food(world, camera, x, y, FoodType.GUN);
		addActor(food);	
	}
	/*private void activateWeapon()
	{
		weapon = new Weapon(world, camera, Weapon.WeaponType.SPIKE);
	}*/

	private void setupBall() {
		Texture tBlue = new Texture(Gdx.files.internal("ball_1.png"));
		blueBall = new Ball(tBlue,camera,margins, new Vector2(1,1), Constants.FOOD_WIDTH+1, Constants.FOOD_HEIGHT+Constants.MARGIN);
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

	private void setUpWep(){
		gun = new Weapon(world,camera, Weapon.WeaponType.GUN,new Vector2(screenDims.x-wepDims.x, screenDims.y-wepDims.y),wepDims);
		flame = new Weapon(world,camera, Weapon.WeaponType.FLAME,new Vector2(0,0),wepDims);
		spike = new Weapon(world,camera, Weapon.WeaponType.SPIKE,new Vector2(screenDims.x - wepDims.x, 0),wepDims);
		electric = new Weapon(world,camera, Weapon.WeaponType.ELECTRIC,new Vector2(0, screenDims.y-wepDims.y),wepDims);



		addActor(gun);
		addActor(flame);
		addActor(spike);
		addActor(electric);
	}

	private void setupCamera() {
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}

	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.BACK){
			this.gballGame.setScreen(new StartScreen(this.gballGame));
		}
		return false;
	}

	@Override
	public void draw() {
		super.draw();
		//renderer.render(world, camera.combined);
	}

	public Vector2 screenToViewport(Vector2 v) {
		Vector3 vtemp = camera.unproject(new Vector3(v.x, v.y, 0));
		return new Vector2(vtemp.x, vtemp.y);
	}

}
