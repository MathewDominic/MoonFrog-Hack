package com.myappstack.gball.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldUtils {

	public static World createWorld() {
		return new World(Constants.WORLD_GRAVITY, true);
	}
	
	
	public static Vector2 screenToViewport(Vector2 v, OrthographicCamera camera){
    	Vector3 vtemp = camera.unproject(new Vector3(v.x,v.y,0));
    	return new Vector2(vtemp.x,vtemp.y);
    }
	
	public static Vector2 viewportToScreen(Vector2 v, OrthographicCamera camera){
    	Vector3 vtemp = camera.project(new Vector3(v.x,v.y,0));
    	return new Vector2(vtemp.x,vtemp.y);
    }
	
	public static Vector2 reflect(Vector2 ray, Vector2 surfaceNpr){
		//r=d−2(d⋅n)n
		//Vector2 surfaceNpr = new Vector2(-surface.y,surface.x).nor().cpy();
		surfaceNpr = surfaceNpr.nor().cpy();
		float dn = ray.dot(surfaceNpr) * -2.0f;
		surfaceNpr.scl(dn);
		surfaceNpr.add(ray);
		
		Vector2 newRay = surfaceNpr.nor().cpy();
		return newRay;
	}
	
	public static int getProportionalHeight(int width, Vector2 dims){
		return (int) ((dims.y*width)/dims.x);
	}

	
	public static Body createWall1(World world, Vector2 pos1, Vector2 pos2, int w, int h) {
		Vector2 pos = new Vector2((pos1.x+pos2.x)/2,(pos1.y+pos2.y)/2);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(pos);
		Body body = world.createBody(bodyDef);
		//PolygonShape shape = new PolygonShape();
		//shape.setAsBox(w, h);
		//body.createFixture(shape, Constants.GROUND_DENSITY);
		
		EdgeShape shape  = new EdgeShape();
		shape.set(pos1, pos2);
		
		

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit
		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();
		return body;
	}
	
	public static Body createWall(World world, Vector2 startPos, Vector2 endPos){
		float posx = (startPos.x + endPos.x)/2f;
		float posy = (startPos.y + endPos.y)/2f;
		float len =(float) Math.sqrt((startPos.x - endPos.x)*(startPos.x-endPos.x) +
				(startPos.y - endPos.y)*(startPos.y - endPos.y));
		
		//System.out.println(len);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(posx, posy));
		EdgeShape shape = new EdgeShape();
		shape.set(-len/2f, 0,len/2f,0);
		//shape.set(startPos, endPos);
		Body line = world.createBody(bodyDef);
		line.setTransform(new Vector2(posx,posy), MathUtils.atan2(startPos.y-endPos.y, startPos.x-endPos.x));
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit
		Fixture fixture = line.createFixture(fixtureDef);
		//line.createFixture(shape,1f);
		shape.dispose();
		return line;
	}
}
