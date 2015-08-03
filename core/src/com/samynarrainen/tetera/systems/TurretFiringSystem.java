package com.samynarrainen.tetera.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.samynarrainen.tetera.components.Bound;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Projectile;
import com.samynarrainen.tetera.components.Renderable;
import com.samynarrainen.tetera.components.Turret;
import com.samynarrainen.tetera.components.Velocity;
import com.samynarrainen.tetera.util.Mappers;
import com.samynarrainen.tetera.util.audio.AudioManager;
import com.samynarrainen.tetera.util.audio.AudioName;
import com.samynarrainen.tetera.util.images.TextureManager;
import com.samynarrainen.tetera.util.images.TextureName;
import com.samynarrainen.tetera.util.Util;

/**
 * Created by Samy Narrainen on 30/05/2015.
 * Turret response to touch on the screen, firing a projectile
 */
public class TurretFiringSystem extends IteratingSystem {

    /**
     * Main game engine
     */
    public Engine engine;

    /**
     * Whether or not the screen has been touched, triggering a fire response
     */
    public static boolean touchUpdate = false;

    /**
     * The position at which a touch was recognised
     */
    public static Vector2 touchPos;

    /**
     * The distance between projectiles
     */
    public final float SPREAD_DISTANCE = 2.0f;

    public TurretFiringSystem(int priority) {
        super(Family.all(Turret.class).get(), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        this.engine = null;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(touchUpdate) {
            AudioManager.play(AudioName.GUN_BLAST_A);


            Position turretPos = Mappers.positionMapper.get(entity);
            Turret turret = Mappers.turretMapper.get(entity);

            float degrees = Util.angleFromTo(turretPos.getPosition(), touchPos);

//            System.out.println(":@@@@" + turretPos.getPosition().angle(touchPos));
//            System.out.println( "degrees: " + degrees + "\n"  +
//            "x: " + Math.cos(Math.toRadians(degrees)) + "\n" +
//            "y:  " + Math.sin(Math.toRadians(degrees)));

            Vector2 velocity = new Vector2((float) Math.cos(Math.toRadians(degrees)), (float) Math.sin(Math.toRadians(degrees)));


            /*
            engine.addEntity(createProjectile(new Vector2(turretPos.getX(),
                    turretPos.getY()), velocity, 20f, entity));
            */

            /*
            //THIS IS HOW YOU ADD DELAYED BULLETS (ACTUALLY BULLETS IN FRONT
            engine.addEntity(createProjectile(new Vector2(turretPos.getX()+(velocity.x*200),
                    turretPos.getY()+(velocity.y*200)), velocity, 20f, entity));
            */

/*
            Vector2 velocityRot = turretPos.getPosition().cpy().rotate(-2);

            System.out.println("#TurretFiringSystem# " + turretPos.getPosition() + "\n" + velocityRot);

            engine.addEntity(createProjectile(new Vector2(velocityRot.x,
                    velocityRot.y), velocity, 20f, entity));
            */


            /**
             * Generates the right number of projectiles and positions them correctly.
             */
             for(float i = -((turret.spread * SPREAD_DISTANCE) / 2);
                 i < (turret.spread * SPREAD_DISTANCE /2); i += SPREAD_DISTANCE) {
                 engine.addEntity(createProjectile(new Vector2(turretPos.getX(),
                         turretPos.getY()), velocity.cpy().rotate(i), 200f, entity));
             }





        }

        touchUpdate = false;

    }


    public Entity createProjectile(Vector2 position, Vector2 velocity, float speed, Entity spawner) {
        Turret turret = Mappers.turretMapper.get(spawner);


        Entity projectile = new Entity();
        projectile.add(new Position(position.x, position.y));
        projectile.add(new Velocity(velocity.x, velocity.y, turret.projectileSpeed));
        Renderable renderable = new Renderable();
        renderable.state = Renderable.RenderableState.STATIC_TEXTURE;
        renderable.region = TextureManager.textureMap.get(TextureName.TURRET_PROJECTILE);
        projectile.add(renderable);
        projectile.add(new Bound(32/2));
        projectile.add(new Projectile(20, spawner, Projectile.ProjectileType.SINGLE, turret.hopCount));

        return projectile;
    }
}
