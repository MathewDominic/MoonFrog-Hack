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
    Image weapon;
    Rectangle bounds;
    Circle bound;
    public static enum WeaponType {
        RED,
        BLUE,
        ELECTRIC,
        SPIKE;
    }
    public Vector2 pos;
    private int xVal;
    private int yVal;
    Vector2 dims;
    Texture gun, gunPick, fire, firePick, electric, electricPick, spike, spikePick;
    public Weapon(World world,OrthographicCamera camera,WeaponType type)
    {
        this.camera = camera;
        this.type = type;

        //dims = WorldUtils.viewportToScreen(new Vector2(Constants.FOOD_WIDTH, Constants.FOOD_HEIGHT), camera);
        //pos = WorldUtils.viewportToScreen(new Vector2(x,y), camera);
        Texture t = null;
        if(this.type == WeaponType.RED)
        {
            fire = new Texture(Gdx.files.internal("flamethrower1.png"));
            firePick = new Texture(Gdx.files.internal("flamethrowe2.png"));
        }
        else if(this.type == WeaponType.ELECTRIC)
        {
            electric = new Texture(Gdx.files.internal("electric1.png"));
            electricPick = new Texture(Gdx.files.internal("electric2.png"));
        }
        else if(this.type == WeaponType.SPIKE)
        {
            spike = new Texture(Gdx.files.internal("spike1.png"));
            spikePick = new Texture(Gdx.files.internal("spike2.png"));
        }
        else
        {
            gun = new Texture(Gdx.files.internal("gun1.png"));
            gunPick = new Texture(Gdx.files.internal("gun2.png"));
        }





    }
    public void change(boolean isPicked, WeaponType type)
    {
        if(isPicked == false)
        {
            if (type == WeaponType.RED) {
                sprite = new Sprite(fire);
            } else if (type == WeaponType.SPIKE) {
                sprite = new Sprite(spike);
            } else if (type == WeaponType.ELECTRIC) {
                sprite = new Sprite(electric);
            } else
            {
                sprite = new Sprite(gun);
            }
        }
        else
        {
            if (type == WeaponType.RED) {
                sprite = new Sprite(fire);
            } else if (type == WeaponType.SPIKE) {
                sprite = new Sprite(spike);
            } else if (type == WeaponType.ELECTRIC) {
                sprite = new Sprite(electric);
            } else
            {
                sprite = new Sprite(gun);
            }
        }
        pos = WorldUtils.viewportToScreen(new Vector2(this.xVal,this.yVal), camera);
        sprite.setPosition(pos.x, pos.y);
        sprite.setSize(dims.x, dims.y);
        bounds.set(this.xVal,this.yVal,Constants.WEP_SIZE,Constants.WEP_SIZE);
        bound.set(this.xVal+Constants.WEP_SIZE/2, this.yVal+Constants.WEP_SIZE/2, Constants.WEP_SIZE);
    }



    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.draw(batch);
    }
}
