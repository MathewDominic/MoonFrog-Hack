package com.myappstack.gball.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class Food extends Actor{

	OrthographicCamera camera;
	public static enum FoodType {
		RED,
		BLUE,
		ELECTRIC,
		SPIKE;
	}
	
	private int xVal;
	private int yVal;
	private Sprite sprite;
	private FoodType type;
	Vector2 dims;
	
	Rectangle bounds;
	Circle bound;
	public Vector2 pos;
	
	public Food(World world,OrthographicCamera camera, int x, int y,FoodType type){
		this.xVal = x;
		this.yVal = y;
		this.camera = camera;
		this.type = type;
		
		dims = WorldUtils.viewportToScreen(new Vector2(Constants.FOOD_WIDTH, Constants.FOOD_HEIGHT),camera);
		pos = WorldUtils.viewportToScreen(new Vector2(x,y), camera);
		Texture t = null;
		if(this.type == FoodType.RED){
			t = new Texture(Gdx.files.internal("fire.png"));
		}
		else if(this.type == FoodType.ELECTRIC)
		{
			t = new Texture(Gdx.files.internal("electric.png"));
		}
		else if (this.type == FoodType.SPIKE)
		{
			t = new Texture(Gdx.files.internal("spike.png"));
		}
		else if (this.type == FoodType.BLUE){
			t = new Texture(Gdx.files.internal("gun.png"));
		}
		
		
		
		sprite = new Sprite(t);
		sprite.setPosition(pos.x, pos.y);
		sprite.setSize(dims.x, dims.y);
		bounds = new Rectangle(x,y,Constants.FOOD_WIDTH,Constants.FOOD_HEIGHT);
		bound = new Circle(x+Constants.FOOD_WIDTH/2, y+Constants.FOOD_WIDTH/2, Constants.FOOD_WIDTH);
	}
	
	public Circle getBounds(){
		return bound;
	}
	
	public void change(int xVal, int yVal,FoodType type){
		this.xVal = xVal;
		this.yVal = yVal;
		this.type = type;
		Texture t;
		if(this.type == FoodType.RED)
		{
			t = new Texture(Gdx.files.internal("fire.png"));
		}
		else if(this.type == FoodType.ELECTRIC)
		{
			t = new Texture(Gdx.files.internal("electric.png"));
		}
		else if(this.type == FoodType.SPIKE)
		{
			t = new Texture(Gdx.files.internal("spike.png"));
		}
		else
		{
			t = new Texture(Gdx.files.internal("gun.png"));
		}
		
		sprite = new Sprite(t);
		pos = WorldUtils.viewportToScreen(new Vector2(this.xVal,this.yVal), camera);
		sprite.setPosition(pos.x, pos.y);
		sprite.setSize(dims.x, dims.y);
		bounds.set(this.xVal,this.yVal,Constants.FOOD_WIDTH,Constants.FOOD_HEIGHT);
		bound.set(this.xVal+Constants.FOOD_WIDTH/2, this.yVal+Constants.FOOD_HEIGHT/2, Constants.FOOD_WIDTH);
	}
	
	public FoodType getType(){
		return this.type;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}

}
