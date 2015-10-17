package com.myappstack.gball.actors;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class Line extends Actor {
	
	
	private OrthographicCamera camera;
	
	public Vector2 startPos;
	public Vector2 endPos;
	
	public Vector2 stp,edp;
	
	ShapeRenderer shapeRenderer;
	private Texture texture;
    private Sprite sprite;
	
	
	
	
	public Line(OrthographicCamera camera,Vector2 startPos, Vector2 endPos){
		this.camera = camera;
		
		this.stp = startPos;
		this.edp = endPos;
		
		this.startPos = WorldUtils.viewportToScreen(startPos, camera);
		this.endPos = WorldUtils.viewportToScreen(endPos, camera);
		this.shapeRenderer = new ShapeRenderer();	
		this.texture = new Texture(Gdx.files.internal("line.png"));
		this.sprite = new Sprite(this.texture);
		setSpriteProps();
		
		//setLineEquation();
	}
	
	public void setSpriteProps(){
		
		Vector2 a,b;
		if(startPos.x < endPos.x){
			a = new Vector2(startPos);
			b = new Vector2(endPos);
		}
		else{
			a = new Vector2(endPos);
			b = new Vector2(startPos);
		}
		System.out.println(endPos.x + " " + endPos.y);
		float width = (float)Math.sqrt((a.x-b.x)*(a.x-b.x) + 
				(a.y-b.y)*(a.y-b.y));
		float angle =(float)( MathUtils.atan2(b.y-a.y, b.x - a.x) * 180.0d / Math.PI);
		sprite.setOrigin(0, 0);
		sprite.setRotation(angle);
		sprite.setPosition(a.x, a.y);
		sprite.setSize(width, 20);
		
	}
	
	public void removeLine(World w){
		//world.destroyBody(line);
		
	}
	
	public Vector2 getNormalVector(){
		//Vector2 one = WorldUtils.screenToViewport(startPos, camera);
		//Vector2 two = WorldUtils.screenToViewport(endPos, camera);
		//Vector2 normal = new Vector2(-(two.y-one.y),two.x-one.x);
		Vector2 normal = new Vector2(-(edp.y-stp.y),edp.x-stp.x);
		return normal.nor().cpy();
	}
	
	/*
	 * ax + by + c = 0
	 * 
	 * m = (y2-y1) / (x2-x1)
	 *  
	 * (y-y1) = m(x-x1)
	 * y = y1 + m(x-x1)
	 * y = mx + y1-mx1
	 * 
	 * mx -y + (y1-mx1)
	 * 
	 *
	 */
	
	private float a,b,c;
	private Float m;
	private void setLineEquation(){
		if(endPos.x == startPos.x){
			a = 1;
			b = 0;
			c = -(int)startPos.x;
			m = null;
		}
		else{
			m = (endPos.y = startPos.y)/ (endPos.x - startPos.x);
			a = m;
			b = -1;
			c = startPos.y - m*startPos.x;
		}
		
	}
	
	public float getDistanceFromLine(Vector2 point){
		//Vector2 one = WorldUtils.screenToViewport(startPos, camera);
		//Vector2 two = WorldUtils.screenToViewport(endPos, camera);
		Vector2 one = stp;
		Vector2 two = edp;
		
		float div = (float) (Math.sqrt((two.y-one.y)*(two.y-one.y) + (two.x-one.x)*(two.x-one.x)));
		
		float distance = (float)((two.y-one.y)*point.x - (two.x - one.x)*point.y + two.x*one.y - two.y*one.x)/div;
		if(distance < 0){
			distance = distance * -1;
		}
		
		return distance;
				
	}
	
	public boolean inSegmentIntrRegion(Vector2 point){
		
		Vector2 one,two;
		
		//Vector2 oneT = WorldUtils.screenToViewport(startPos, camera);
		//Vector2 twoT = WorldUtils.screenToViewport(endPos, camera);
		Vector2 oneT = stp;
		Vector2 twoT = edp;
		
		boolean ret = false;
		if(oneT.x < twoT.x){
			one = oneT;
			two = twoT;
		}
		else{
			one = twoT;
			two = oneT;
		}
		if(point.x >= one.x && point.x <= two.x){
			ret = true;
		}
		
		if(oneT.y < twoT.y){
			one = oneT;
			two = twoT;
		}
		else{
			one = twoT;
			two = oneT;
		}
		if(point.y >= one.y && point.y <= two.y){
			ret = true;
		}
		
		return ret;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		//super.draw(batch, parentAlpha);
		//spr.draw(batch);
		/*batch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
	    shapeRenderer.setColor(Color.RED);
	    shapeRenderer.line(startPos, endPos);
	    shapeRenderer.end();
	    
	    batch.begin();
	    */
		sprite.draw(batch);
	}
	
}
