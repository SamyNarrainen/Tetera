package com.samynarrainen.tetera.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.samynarrainen.tetera.components.Bound;
import com.samynarrainen.tetera.components.Enemy;
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
 * Created by Samy Narrainen on 31/05/2015.
 */
public class EnemyFiringSystem extends IteratingSystem {

    public Engine engine;

    public EnemyFiringSystem(int priority) {
        super(Family.all(Enemy.class, Position.class, Velocity.class, Renderable.class).get(), priority);
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
        Position enemyPosition = Mappers.positionMapper.get(entity);
        Enemy enemy = Mappers.enemyMapper.get(entity);
        Velocity velocity = Mappers.velocityMapper.get(entity);
        Renderable renderable = Mappers.renderableMapper.get(entity);

        //Turrets to compare to
        ImmutableArray<Entity> turrets = engine.getEntitiesFor(
                Family.all(Turret.class, Position.class).get());

        for (Entity turret : turrets) {
            Position turretPosition = Mappers.positionMapper.get(turret);

            if(enemyPosition.unprojectedPosition().dst(turretPosition.unprojectedPosition()) < enemy.firingDistance) {

                //The enemy is still going towards the turret
                if(velocity.moving) {
                    renderable.region = TextureManager.textureMap.get(TextureName.SOLDIER_FIRE_IDLE);
                    renderable.state = Renderable.RenderableState.STATIC_TEXTURE;

                    //Stop movement
                    velocity.moving = false;

                //The enemy is already in firing position
                } else if(!velocity.moving && enemy.readyToFire()) {
                    AudioManager.play(AudioName.GUN_BLAST_B);
                    enemy.elapsedTime = 0.0f;

                    float degrees = Util.angleFromTo(enemyPosition.unprojectedPosition(), turretPosition.unprojectedPosition());

                    Vector2 velVector = new Vector2((float) Math.cos(Math.toRadians(degrees)), (float) Math.sin(Math.toRadians(degrees)));

                    engine.addEntity(createProjectile(new Vector2(enemyPosition.getX(),
                            enemyPosition.getY()), velVector, 20f, entity));


                    renderable.animation = new Animation(Util.FRAME_SPEED, TextureManager.textureSheetMap.get(TextureName.SOLDIER_SHOOT));
                    renderable.state = Renderable.RenderableState.SINGLE_ANIMATION;
                    renderable.animationTime = 0.0f;


                }

                //Increment elapsed time
                enemy.elapsedTime += deltaTime;

            }

        }
    }

    public Entity createProjectile(Vector2 position, Vector2 velocity, float speed, Entity spawner) {
        Entity projectile = new Entity();
        projectile.add(new Position(position.x, position.y));
        projectile.add(new Velocity(velocity.x, velocity.y, speed));
        Renderable renderable = new Renderable();
        renderable.state = Renderable.RenderableState.STATIC_TEXTURE;
        renderable.region = TextureManager.textureMap.get(TextureName.TURRET_PROJECTILE);
        projectile.add(renderable);
        projectile.add(new Bound(32/2));
        Projectile proj = new Projectile(200, spawner, Projectile.ProjectileType.SINGLE, 1);
        proj.friendly = false;
        projectile.add(proj);


        return projectile;
    }
}
