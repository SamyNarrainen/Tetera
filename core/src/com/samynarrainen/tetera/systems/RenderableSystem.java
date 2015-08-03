package com.samynarrainen.tetera.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.samynarrainen.tetera.components.Killable;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Renderable;
import com.samynarrainen.tetera.util.Mappers;
import com.samynarrainen.tetera.util.images.TextureName;

/**
 * Created by Samy Narrainen on 29/05/2015.
 */
public class RenderableSystem extends IteratingSystem {

    private final Batch batch;
    private final ShapeRenderer renderer;
    /**
     * 2D view camera
     */
    private final OrthographicCamera camera;

    public final Color POSITIVE_HEALTH_COLOR = Color.GREEN;
    public final Color NEGATIVE_HEALTH_COLOR = Color.RED;
    public final Color POSITIVE_SHIELD_COLOR = new Color(0.11373f, 0.60784f, 0.81569f, 1f);
    public final Color NEGATIVE_SHIELD_COLOR = Color.GRAY;


    private final float HEALTH_HEIGHT_POSITION_MODIFIER = 5.0f;

    public RenderableSystem(int priority, final Batch batch, final ShapeRenderer renderer,
                            final OrthographicCamera camera) {
        super(Family.all(Position.class, Renderable.class).get(), priority);

        this.batch = batch;
        this.renderer = renderer;
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Position p = Mappers.positionMapper.get(entity);
        final Renderable r = Mappers.renderableMapper.get(entity);
        final Killable killable = Mappers.killableMapper.get(entity);

        switch (r.state) {
            case SINGLE_ANIMATION: {
                r.animationTime += deltaTime;
                drawFrame(entity, p, r, r.scale, r.animation.getKeyFrame(r.animationTime, false), r.rotation);
                break;
            }

            case ANIMATION: {
                r.animationTime += deltaTime;
                drawFrame(entity, p, r, r.scale, r.animation.getKeyFrame(r.animationTime, true), r.rotation);
                break;
            }

            case STATIC_TEXTURE: {
                drawFrame(entity, p, r, r.scale, r.region, r.rotation);
                break;
            }

            case DESTROY_AFTER_ANIMATE: {
                r.animationTime += deltaTime;
                drawFrame(entity, p, r, r.scale, r.animation.getKeyFrame(r.animationTime, false), r.rotation);
                break;
            }
        }

        /**
         * Rendering associated with killables
         */
        if(Mappers.killableMapper.has(entity)) {
            drawHealthBar(p.getX(), p.getY(), killable, r);

            //Draw shield
            if(killable.shielded) {
                killable.animationTime += deltaTime;
                //The scale is dependant on the entity width to the shield width
                drawFrame(entity, p, r, 1* r.getWidth() / TextureName.SHIELD_PULSE.frameWidth,
                        killable.shieldAnimation.getKeyFrame(killable.animationTime, true), 0);
            }
        }
    }








    private void drawFrame(final Entity entity, final Position p, final Renderable r,
                           float scalingFactor, final TextureRegion region, float angle) {

        final float xOffset = region.getRegionWidth() / 2.0f;
        final float yOffset = region.getRegionHeight() / 2.0f;

        final float x = p.getX() - xOffset;
        final float y = p.getY() - yOffset;

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        batch.draw(region, x, y, region.getRegionWidth() / 2.0f, region.getRegionHeight() / 2.0f,
                region.getRegionWidth(), region.getRegionHeight(), scalingFactor, scalingFactor, angle);

        batch.end();

    }

    private void drawHealthBar(float x, float y, Killable k, Renderable entityRenderable) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        x -= entityRenderable.getWidth() / 2.0f;
        y += entityRenderable.getHeight() / 2.0f;

        if(true) {
            // Draw the positive health
            renderer.setColor(k.shieldActive() ? POSITIVE_SHIELD_COLOR : POSITIVE_HEALTH_COLOR);
            renderer.rect(x, y, entityRenderable.getWidth(), entityRenderable.getHeight() / HEALTH_HEIGHT_POSITION_MODIFIER);

            // Draw the negative health
            renderer.setColor(k.shieldActive() ? NEGATIVE_SHIELD_COLOR : NEGATIVE_HEALTH_COLOR);

            float remainingHealth = k.remainingConsideredHealth();
            // If there's no difference, default to 0
            remainingHealth = remainingHealth >= 1.0f ? 0.0f : 1.0f - remainingHealth;

            //Fixed offset, now health drops naturally.
            renderer.rect(x + entityRenderable.getWidth() - entityRenderable.getWidth() * remainingHealth, y,
                    entityRenderable.getWidth() * remainingHealth, entityRenderable.getHeight()
                    / HEALTH_HEIGHT_POSITION_MODIFIER);
        }


        renderer.end();
    }
}
