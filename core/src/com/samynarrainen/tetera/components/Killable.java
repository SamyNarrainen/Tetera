package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.samynarrainen.tetera.util.audio.AudioManager;
import com.samynarrainen.tetera.util.audio.AudioName;
import com.samynarrainen.tetera.util.images.TextureManager;
import com.samynarrainen.tetera.util.images.TextureName;
import com.samynarrainen.tetera.util.Util;

/**
 * Created by Samy Narrainen on 31/05/2015.
 */
public class Killable extends Component {

    /**
     * The current health the entity is at
     */
    public float health = 100.0f;

    /**
     * The upper limit of health
     */
    public float maxHealth = 100.0f;

    /**
     * The point at which death occurs (when health <=)
     */
    public final float DEATH_THRESHOLD = 0.0f;

    /**
     * Whether or not an entity has a shield.
     */
    public boolean shielded = true;

    /**
     * The animation for the shield
     */
    public Animation shieldAnimation = new Animation(Util.FRAME_SPEED,
            TextureManager.textureSheetMap.get(TextureName.SHIELD_PULSE));

    /**
     * The timing for the animation of the shield
     */
    public float animationTime = 0.0f;

    /**
     * The actual health of the shield
     */
    public float shieldHealth = 100.0f;

    /**
     * The maximum health the shield can have.
     */
    public float shieldHealthMax = 100.0f;

    /**
     * The number the shield health must reach to be depleted
     */
    public final float SHIELD_THRESHOLD = 0.0f;


    /**
     * Remaining health in relation to the maximum health.
     * @return
     */
    public float remainingHealth() {

        return health / maxHealth;
    }

    /**
     * The remaining health, with factors consisdered such as shield.
     * For example, if shielded, it will return the shield health
     * @return
     */
    public float remainingConsideredHealth() {
        return shieldActive() ? shieldHealth / shieldHealthMax : remainingHealth();
    }

    /**
     * Decrements health, taking into account factors like shield.
     * @param health
     */
    public void removeHealth(float health) {
        if(shielded && shieldHealth > SHIELD_THRESHOLD) {
            shieldHealth -= health;

            //If the shield is broken beyond then remove the remaining from actual hp
            if(shieldHealth <= 0) {
                AudioManager.play(AudioName.SHIELD_DROP);
                this.health -= shieldHealth;
                this.shielded = false;
            }

        } else {
            this.health -= health;

            //If the health is less than the threshold, default back to it.
            this.health = this.health < DEATH_THRESHOLD ? DEATH_THRESHOLD : this.health;
        }
    }

    public boolean shieldActive() {
        return shielded && shieldHealth > SHIELD_THRESHOLD;
    }

    public boolean isDead() {
        return health <= DEATH_THRESHOLD;
    }



    public Killable(float health) {
        this.health = health;
        this.maxHealth = health;
    }



}
