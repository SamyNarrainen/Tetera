package com.samynarrainen.tetera.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Velocity;
import com.samynarrainen.tetera.util.Mappers;

/**
 * Created by Samy Narrainen on 30/05/2015.
 * Updates the position of moving entities based on their Velocity
 */
public class MovementSystem extends IteratingSystem {


    public MovementSystem(int priority) {
        super(Family.all(Position.class, Velocity.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position position = Mappers.positionMapper.get(entity);
        Velocity velocity = Mappers.velocityMapper.get(entity);

        if(velocity.moving) {
            position.setX(position.getX() + velocity.direction.x * velocity.speed);
            position.setY(position.getY() +  velocity.direction.y * velocity.speed);
        }
    }
}
