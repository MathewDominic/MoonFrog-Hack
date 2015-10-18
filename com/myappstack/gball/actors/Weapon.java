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
import com.badlogic.gdx.utils.TimeUtils;
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
    private boolean isPicked, canChangeScore1, hasEffect;

    private Sprite effect;

    Circle bounds;
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
    Texture actveT, normalT, effectT;

    public Weapon(World world,OrthographicCamera camera,WeaponType type,Vector2 pos, Vector2 dims)
    {
        this.camera = camera;
        this.type = type;
        this.pos = pos;
        this.dims = dims;
        this.isPicked = false;
        this.canChangeScore1 = false;
        this.bounds = new Circle();


        //dims = WorldUtils.viewportToScreen(new Vector2(Constants.FOOD_WIDTH, Constants.FOOD_HEIGHT), camera);
        //pos = WorldUtils.viewportToScreen(new Vector2(x,y), camera);
        Texture t = null;
        if(this.type == WeaponType.FLAME)
        {
            actveT = new Texture(Gdx.files.internal("flamethrower1.png"));
            normalT = new Texture(Gdx.files.internal("flamethrowe2.png"));
            effectT = new Texture(Gdx.files.internal("fire_eff.png"));
            this.hasEffect = true;

            effect = new Sprite(effectT);
            effect.setPosition(dims.x/2,dims.y/2);
            effect.setSize(dims.x,dims.y);
        }
        else if(this.type == WeaponType.ELECTRIC)
        {
            actveT = new Texture(Gdx.files.internal("electric1.png"));
            normalT = new Texture(Gdx.files.internal("electric2.png"));
            effectT = new Texture(Gdx.files.internal("shock.png"));
            this.hasEffect = true;

            effect = new Sprite(effectT);
            effect.setPosition(dims.x/2,pos.y-dims.y/2);
            effect.setSize(dims.x,dims.y);
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

        bounds.set(this.xVal,this.yVal,Constants.WEP_SIZE);
    }

    public Circle getBounds(){
        return bounds;
    }

    public boolean changeScore(){
        boolean ret = false;
        if(this.isPicked){
            ret = true;
            this.isPicked = false;
        }
        return ret;
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

        if(this.hasEffect && this.isActive()){
            effect.draw(batch);
        }
    }
}
