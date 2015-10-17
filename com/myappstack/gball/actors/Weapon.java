package com.myappstack.gball.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    public Weapon(World world,OrthographicCamera camera,WeaponType type)
    {
        this.camera = camera;
        this.type = type;

        //dims = WorldUtils.viewportToScreen(new Vector2(Constants.FOOD_WIDTH, Constants.FOOD_HEIGHT), camera);
        //pos = WorldUtils.viewportToScreen(new Vector2(x,y), camera);
        Texture t = null;
        if(this.type == WeaponType.RED){
            t = new Texture(Gdx.files.internal("flamethrower-picked.png"));
        }
        else if(this.type == WeaponType.ELECTRIC)
        {
            t = new Texture(Gdx.files.internal("electric-weapon-picked.png"));
        }
        else if(this.type == WeaponType.SPIKE)
        {
            t = new Texture(Gdx.files.internal("spike-weapon-picked.png"));
            System.out.println("Spike Activate");
        }
        else
        {
            t = new Texture(Gdx.files.internal("gun-weapon-picked.png"));
        }





    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }
}
