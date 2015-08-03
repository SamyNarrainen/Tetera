package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.samynarrainen.tetera.Tetera;
import com.samynarrainen.tetera.util.Util;

/**
 * Created by Samy Narrainen on 29/05/2015.
 * The given position of an entity on the map
 */
public class Position extends Component {

    /**
     * Vector to hold x and y positions
     */
    private Vector2 position = new Vector2(0.0f, 0.0f);

    /**
     * Standard 2D, x,y coordinate system.
     * @param x
     * @param y
     */
    public Position(float x, float y) { this.position.set(x,y); }


    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setX(float x) {
        this.position.x = x;
    }

    public void setY(float y) {
        this.position.y = y;
    }

    /**
     * Whether or not this entity should be removed if beyond the cleanup range.
     */
    public boolean cleanable = true;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 unprojectedPosition() {
        return Util.unprojectVector(getPosition());
    }






}
