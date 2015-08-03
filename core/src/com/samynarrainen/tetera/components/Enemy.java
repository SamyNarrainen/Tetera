package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by Samy Narrainen on 31/05/2015.
 */
public class Enemy extends Component {

    /**
     * The minimum distance a target must be within before this can shoot.
     */
    public float firingDistance = 200;

    /**
     * The damage this shooter is capable of
     */
    public float damage = 10.0f;

    /**
     * The time unit defining the time in between shots (in seconds)
     */
    public float cooldown = 5;

    /**
     * The time incurred since the previous shot
     */
    public float elapsedTime  = 0.0f;

    /**
     * If the enemy has elapsed its cooldown
     * @return
     */
    public Boolean readyToFire() {
        return elapsedTime >= cooldown;
    }





}
