package com.samynarrainen.tetera.systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.samynarrainen.tetera.components.Bound;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.util.Mappers;

/**
 * Created by Samy Narrainen on 20/04/2015.
 */
public class BoundingDebugSystem extends IteratingSystem {
	private ShapeRenderer renderer;

	@SuppressWarnings("unchecked")
	public BoundingDebugSystem(int priority, final ShapeRenderer renderer) {
		super(Family.all(Position.class, Bound.class).get(), priority);
		this.renderer = renderer;
		System.out.println("BoundingDebugSystem created: are you sure you meant to do this?");
	}

	@Override
	public void update(float deltaTime) {
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(Color.BLACK);

		super.update(deltaTime);

		renderer.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final Bound bound = Mappers.boundMapper.get(entity);
		final Position p = Mappers.positionMapper.get(entity);

		renderer.point(p.getX(), p.getY(), 0.0f);
		renderer.circle(p.getX(), p.getY(), bound.radius);
	}
}
