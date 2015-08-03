package com.samynarrainen.tetera.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.samynarrainen.tetera.components.Bound;
import com.samynarrainen.tetera.components.Killable;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Projectile;
import com.samynarrainen.tetera.entities.EntityFactory;
import com.samynarrainen.tetera.util.Mappers;
import com.samynarrainen.tetera.util.Util;
import com.samynarrainen.tetera.util.audio.AudioManager;
import com.samynarrainen.tetera.util.audio.AudioName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Samy Narrainen on 31/05/2015.
 * Checks for collisions with entities based on their position and bounds
 */
public class CollisionSystem extends EntitySystem {

    /**
     * Game's engine
     */
    private Engine engine;

    /**
     * Indicator that the system is still using the engine
     */
    boolean processing = false;

    public CollisionSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        processing = true;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        this.engine = null;
        processing = false;
    }

    @Override
    public boolean checkProcessing() {
        return processing;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void update(float deltaTime) {
        //Get all collidable entities
        ImmutableArray<Entity> entities = engine.getEntitiesFor(
                Family.all(Position.class, Bound.class).get());


        //Construct the circles to represent the bounds
        final List<Circle> bounds = new ArrayList<Circle>();

        for(Entity entity : entities) {
            final Vector2 position = Util.unprojectVector(Mappers.positionMapper.get(entity).getPosition());
            final Bound bound = Mappers.boundMapper.get(entity);
            bounds.add(new Circle(position.x, position.y, bound.radius)); //FIXME might have to consider unprojected coordinates.
        }

        checkA: for(int i = 0; i < bounds.size(); i++) {
            Entity entityA = entities.get(i);
            Circle circleA = bounds.get(i);

            checkB : for(int j = 0; j < bounds.size(); j++) {
                Entity entityB = entities.get(j);
                Circle circleB = bounds.get(j);

                if(circleA.overlaps(circleB)) {
                    //System.out.println("#CollisionSystem# COLLISION!!!!");
                    if(Mappers.projectileMapper.has(entityA) && Mappers.killableMapper.has(entityB)) {
                        //System.out.println("#CollisionSystem# Projectile collided with killable!!!!");

                        Projectile projectile = Mappers.projectileMapper.get(entityA);
                        Killable killable = Mappers.killableMapper.get(entityB);

                        if(projectile.friendly && Mappers.enemyMapper.has(entityB) ||
                                !projectile.friendly && Mappers.turretMapper.has(entityB)) {

                            AudioManager.play(AudioName.HIT_IMPACT);

                            switch (projectile.type) {
                                case SINGLE: {
                                    engine.addEntity(EntityFactory.explosion(Mappers.positionMapper.get(entityA).getPosition(), 1f));
                                    killable.removeHealth(projectile.damage);
                                    //Decrement the hopcount and only remove if 0;
                                    projectile.hopCount -= 1;
                                    if(projectile.hopCount <= 0) engine.removeEntity(entityA);

                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }

    }

}
