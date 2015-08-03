package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Samy Narrainen on 30/05/2015.
 * Describes an entity which can move
 */
public class Velocity extends Component {

    /**
     * The x,y direction the entity is travelling in
     */
    public Vector2 direction;

    /**
     * The speed, thus velocity at which the entity is moving at
     */
    public float speed;

    /**
     * Whether or not the entity is actually moving, defaulting at true
     */
    public boolean moving = true;


    public Velocity(float x, float y, float speed) {
        this.direction = new Vector2(x,y);
        this.speed = speed;
    }
}

