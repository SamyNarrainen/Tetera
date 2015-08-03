package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by Samy Narrainen on 29/05/2015.
 */
public class Renderable extends Component {

    /**
     * An animation which will occur once.
     */
    public Animation animation;

    /**
     * The 'progress' of the animation.
     */
    public float animationTime = 0.0f;


    /**
     * Scale of which things will be rendered at.
     */
    public float scale = 1.0f;

    /**
     * A mutable state which defines how the Renderable should be drawn.
     */
    public RenderableState state;

    /**
     * The angle the renderable will be drawn at.
     */
    public float rotation = 0.0f;

    /**
     * The texture for static states
     */
    public TextureRegion region;

    /**
     * The possible states of Renderable.
     */
    public static enum RenderableState {
        SINGLE_ANIMATION, ANIMATION, STATIC_TEXTURE, DESTROY_AFTER_ANIMATE;
    }


    public float getWidth() {
        switch (state) {
            case SINGLE_ANIMATION:
                return animation.getKeyFrame(animationTime).getRegionWidth() * scale;

            case ANIMATION:
                return animation.getKeyFrame(animationTime).getRegionWidth() * scale;

            case STATIC_TEXTURE:
                return region.getRegionWidth() * scale;

            case DESTROY_AFTER_ANIMATE:
                return animation.getKeyFrame(animationTime).getRegionWidth() * scale;

            default:
                break;
        }

        throw new GdxRuntimeException("Invalid type in Renderable.getWidth()");
    }

    public float getHeight() {
        switch (state) {
            case SINGLE_ANIMATION:
                return animation.getKeyFrame(animationTime).getRegionHeight() * scale;

            case ANIMATION:
                return animation.getKeyFrame(animationTime).getRegionHeight() * scale;

            case STATIC_TEXTURE:
                return region.getRegionHeight() * scale;

            case DESTROY_AFTER_ANIMATE:
                return animation.getKeyFrame(animationTime).getRegionHeight() * scale;

            default:
                break;
        }

        throw new GdxRuntimeException("Invalid type in Renderable.getHeight()");
    }




}
