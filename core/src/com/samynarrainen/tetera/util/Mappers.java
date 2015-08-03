package com.samynarrainen.tetera.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.samynarrainen.tetera.components.Bound;
import com.samynarrainen.tetera.components.Enemy;
import com.samynarrainen.tetera.components.Killable;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Projectile;
import com.samynarrainen.tetera.components.Renderable;
import com.samynarrainen.tetera.components.Turret;
import com.samynarrainen.tetera.components.Velocity;

/**
 * Created by Samy Narrainen on 29/05/2015.
 * Provides static access to ComponentMappers which only have to be initialised once.
 */
public class Mappers {

    public static final ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
    public static final ComponentMapper<Renderable> renderableMapper = ComponentMapper.getFor(Renderable.class);
    public static final ComponentMapper<Turret> turretMapper = ComponentMapper.getFor(Turret.class);
    public static final ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);
    public static final ComponentMapper<Bound> boundMapper = ComponentMapper.getFor(Bound.class);
    public static final ComponentMapper<Projectile> projectileMapper = ComponentMapper.getFor(Projectile.class);
    public static final ComponentMapper<Killable> killableMapper = ComponentMapper.getFor(Killable.class);
    public static final ComponentMapper<Enemy> enemyMapper = ComponentMapper.getFor(Enemy.class);
}
