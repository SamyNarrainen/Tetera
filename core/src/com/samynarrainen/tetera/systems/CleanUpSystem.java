package com.samynarrainen.tetera.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.samynarrainen.tetera.components.Killable;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Renderable;
import com.samynarrainen.tetera.entities.EntityFactory;
import com.samynarrainen.tetera.util.Mappers;
import com.samynarrainen.tetera.util.Util;
import com.samynarrainen.tetera.util.audio.AudioManager;

/**
 * Created by Samy Narrainen on 30/05/2015.
 * Removes entities which have reached certain removal criteria
 */
public class CleanUpSystem extends IteratingSystem {

    /**
     * The game engine
     */
    public Engine engine;

    /**
     * Defines the limit of which an entity can be beyond the screen
     */
    public final float SAFE_BOUNDARY = Util.SAFE_BOUNDARY;

    /**
     * The screen's maximum width
     */
    public final float SCREEN_X = Gdx.graphics.getWidth();

    /**
     * The screen's maximum height
     */
    public final float SCREEN_Y = Gdx.graphics.getHeight();

    public CleanUpSystem(int priority) {
        super(Family.all(Position.class, Renderable.class).get(), priority);
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
        //System.out.println("#CleanUpSystem# no of entities: " + engine.getEntities().size());
        Position pos = Mappers.positionMapper.get(entity);
        Renderable renderable = Mappers.renderableMapper.get(entity);
        Killable killable = Mappers.killableMapper.get(entity);

        Vector2 position = Util.unprojectVector(pos.getPosition());

        //The entity is beyond the acceptable limit
        if((position.x > SCREEN_X + SAFE_BOUNDARY || position.y > SCREEN_Y + SAFE_BOUNDARY
                || position.x < 0 - SAFE_BOUNDARY || position.y < 0 - SAFE_BOUNDARY) && pos.cleanable)
            engine.removeEntity(entity);

        //If a destroy after animate renderable entity has exceeded its animation.
        if(renderable.state == Renderable.RenderableState.DESTROY_AFTER_ANIMATE &&
                renderable.animation.isAnimationFinished(renderable.animationTime))
            engine.removeEntity(entity);

        //If a killable has died
        if(Mappers.killableMapper.has(entity)) {
            if(killable.isDead()) {
                AudioManager.play(AudioManager.selectEnemyDeath());
                engine.addEntity(EntityFactory.deathExplosion(pos.getPosition(), renderable.getWidth()));
                //TODO SCORING INCREMENT ECT
                engine.removeEntity(entity);
            }

        }


    }
}
