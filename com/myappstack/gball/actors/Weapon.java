package com.myappstack.gball.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
/**
 * Created by mdputhoo on 17-Oct-15.
 */
public class Weapon extends Actor
{
    OrthographicCamera camera;
    private WeaponType type;
    private Sprite sprite;
    private Sprite active;
    private Sprite normal;
    private boolean isPicked;

    Rectangle bounds;
    Circle bound;
    public static enum WeaponType {
        FLAME,
        GUN,
        ELECTRIC,
        SPIKE;
    }
    public Vector2 pos;
    private int xVal;
    private int yVal;
    Vector2 dims;
    Texture actveT, normalT;

    public Weapon(World world,OrthographicCamera camera,WeaponType type,Vector2 pos, Vector2 dims)
    {
        this.camera = camera;
        this.type = type;
        this.pos = pos;
        this.dims = dims;
        this.isPicked = false;
        this.bounds = new Rectangle();
        this.bound = new Circle();

        //dims = WorldUtils.viewportToScreen(new Vector2(Constants.FOOD_WIDTH, Constants.FOOD_HEIGHT), camera);
        //pos = WorldUtils.viewportToScreen(new Vector2(x,y), camera);
        Texture t = null;
        if(this.type == WeaponType.FLAME)
        {
            actveT = new Texture(Gdx.files.internal("flamethrower1.png"));
            normalT = new Texture(Gdx.files.internal("flamethrowe2.png"));
        }
        else if(this.type == WeaponType.ELECTRIC)
        {
            actveT = new Texture(Gdx.files.internal("electric1.png"));
            normalT = new Texture(Gdx.files.internal("electric2.png"));
        }
        else if(this.type == WeaponType.SPIKE)
        {
            actveT = new Texture(Gdx.files.internal("spike1.png"));
            normalT = new Texture(Gdx.files.internal("spike2.png"));
        }
        else
        {
            actveT = new Texture(Gdx.files.internal("gun1.png"));
            normalT = new Texture(Gdx.files.internal("gun2.png"));
        }

        active = new Sprite(actveT);
        normal = new Sprite(normalT);

        active.setPosition(pos.x, pos.y);
        active.setSize(dims.x, dims.y);

        normal.setPosition(pos.x, pos.y);
        normal.setSize(dims.x, dims.y);

        Vector2 xyval = WorldUtils.screenToViewport(pos,camera);
        this.xVal = (int) xyval.x;
        this.yVal = (int) xyval.y;

        bounds.set(this.xVal,this.yVal,Constants.WEP_SIZE,Constants.WEP_SIZE);
        bound.set(this.xVal+Constants.WEP_SIZE/2, this.yVal+Constants.WEP_SIZE/2, Constants.WEP_SIZE);
    }


    public void changeState(boolean isPicked)
    {
        this.isPicked = isPicked;
    }

    public boolean isActive(){
        return  this.isPicked;
    }



    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(isPicked){
            active.draw(batch);
        }
        else{
            normal.draw(batch);
        }
    }
}
