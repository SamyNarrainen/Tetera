package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Samy Narrainen on 30/05/2015.
 * Component that the turret shoots
 */
public class Projectile extends Component {

    /**
     * Damage this projectile will cause on impact.
     */
    public float damage = 0.0f;

    /**
     * The entity which created the projectile.
     */
    public Entity spawner;

    public ProjectileType type;

    /**
     * Dictates whether or not this projectile will damage enemies or towers.
     */
    public boolean friendly = true;

    /**
     * The number of times this projectile can hit before removal.
     */
    public int hopCount = 1;

    public static enum ProjectileType {
        SINGLE, PERSIST; //FIXME these 2 are obsolete from hopCount
    }


    public Projectile(float damage, Entity spawner, ProjectileType type, int hopCount) {
        this.damage = damage;
        this.spawner = spawner;
        this.type = type;
        this.hopCount = hopCount;
    }


}
