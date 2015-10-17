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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;
//import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class BallT extends Actor {

	OrthographicCamera camera;
	
	private int xVal;
	private int yVal;
	public static enum State {
		NORMAL,
		SPEED,
		GOTHROUGH
	}
	
	private Vector2 pos;
	private Vector2 posScreen;
	private float speed;
	private Vector2 direction;
	private boolean inScreen;
	private State state;
	private long stateStartTime;
	
	private Vector2 margins;
	private Vector2 screenDims;
	
	private Sprite sprite;
	private Vector2 dims;
	Rectangle bounds;
	Circle bound;
	
	
	public BallT(OrthographicCamera camera,Vector2 margins,Vector2 direction, int x, int y,String img){
		this.pos = new Vector2(x,y);
		this.camera = camera;
		this.margins = margins;
		this.direction = direction.nor();
		this.inScreen = true;
		this.state = State.NORMAL;
		this.speed = Constants.BALL_SPEED;
		
		dims = WorldUtils.viewportToScreen(new Vector2(2*Constants.BALL_RADIUS,2*Constants.BALL_RADIUS),camera);
		posScreen = WorldUtils.viewportToScreen(pos, camera);
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT), camera);
		
		Texture t = new Texture(Gdx.files.internal(img));
		sprite = new Sprite(t);
		sprite.setPosition(posScreen.x, posScreen.y);
		sprite.setSize(dims.x, dims.y);

		bounds = new Rectangle(pos.x+Constants.BALL_RADIUS,pos.y+dims.y/2,2*Constants.BALL_RADIUS,2*Constants.BALL_RADIUS);
		bound  = new Circle(pos.x+Constants.BALL_RADIUS,pos.y+Constants.BALL_RADIUS,Constants.BALL_RADIUS);
		
		setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}
	
	public void setStateProperties(State state){
		this.state = state;
		this.stateStartTime = System.currentTimeMillis();
		if(this.state == State.NORMAL){
			speed = Constants.BALL_SPEED;
		}
		else if(this.state == State.SPEED){
			speed = Constants.BALL_SPEED2;
		}
	}
	
	@Override
	public void act(float delta) {
		// pos.add(direction.scl(2));
		//System.out.println(pos.toString());
		
		if(this.state != State.NORMAL && (System.currentTimeMillis() - stateStartTime) > 3000){
			setStateProperties(State.NORMAL);
			System.out.println("resetting ball prop");
		}
		
		if((posScreen.x >= margins.y) && (posScreen.x + dims.x <= screenDims.x - margins.y) &&
				(posScreen.y >= margins.y) && (posScreen.y+ dims.y<= screenDims.y-margins.x)){
			inScreen = true;
		}
		else{
			//System.out.println("Out :"+pos +" ");
			inScreen = true;
		}
		

		if (inScreen) {
			if (posScreen.x <= margins.y) {
				// hit left edge
				this.pos.x = Constants.MARGIN;
				//this.posScreen.x = 0;
				this.direction = WorldUtils.reflect(direction,new Vector2(1, 0));
				inScreen = false;
			} else if (posScreen.x +dims.x >= screenDims.x - margins.y) {
				// hit right edge
				this.pos.x = Constants.VIEWPORT_WIDTH  - Constants.MARGIN - 2*Constants.BALL_RADIUS;
				//this.posScreen.x = 
				this.direction = WorldUtils.reflect(direction, new Vector2(-1,0));
				inScreen = false;
			}

			if (posScreen.y <= margins.y) {
				// hit bottom edge
				this.pos.y = Constants.TOP_MARGIN;
				this.direction = WorldUtils.reflect(direction,new Vector2(0, 1));
				inScreen = false;
			} else if (posScreen.y + dims.y >= screenDims.y - margins.x) {
				// hit upper edge
				this.pos.y = Constants.VIEWPORT_HEIGHT - Constants.TOP_MARGIN -2* Constants.BALL_RADIUS;
				this.direction = WorldUtils.reflect(direction, new Vector2(0,-1));
				inScreen = false;
			}

		}
		
		
		pos.add(new Vector2(direction.x* speed, direction.y*speed));
		//System.out.println(direction.scl(2));
		posScreen = WorldUtils.viewportToScreen(pos, camera);
		//sprite.setPosition(posScreen.x, posScreen.y);
		bounds = new Rectangle(pos.x, pos.y, 2*Constants.BALL_RADIUS, 2*Constants.BALL_RADIUS);
		bound.set(pos.x+Constants.BALL_RADIUS, pos.y+Constants.BALL_RADIUS, Constants.BALL_RADIUS);
		
		setPosition(posScreen.x, posScreen.y);
	}
	
	

	@Override
	protected void positionChanged() {
		// TODO Auto-generated method stub
		sprite.setPosition(getX(), getY());
		super.positionChanged();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}
	
	public boolean collidedWith(Vector2 pos2){
		boolean collided = false;
		Vector2 o1 = new Vector2(pos.x+Constants.BALL_RADIUS,pos.y+Constants.BALL_RADIUS);
		Vector2 o2 = new Vector2(pos2.x+Constants.BALL_RADIUS,pos2.y+Constants.BALL_RADIUS);
		float dist = (o1.x - o2.x)*(o1.x -o2.x) + (o1.y - o2.y)*(o1.y-o2.y);
		float colDist = 4*(Constants.BALL_RADIUS - Constants.BALL_RADIUS/4)*(Constants.BALL_RADIUS - Constants.BALL_RADIUS/4);
		if(dist <= colDist ){
			collided = true;
			System.out.println("Collided");
			System.out.println(o1);
			System.out.println(o2);
			System.out.println(dist +" "+ colDist);
		}
		return  collided;
	}
	
	public State getState(){
		return this.state;
	}
	
	public Circle getBounds(){
		return bound;
	}
	
	public void setDirection(Vector2 direction){
		this.direction = direction;
	}
	
	public Vector2 getDirection(){
		return this.direction;
	}
	
	public Vector2 getPos(){
		return pos;
	}

}
