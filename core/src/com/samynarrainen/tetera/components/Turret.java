package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Samy Narrainen on 29/05/2015.
 * The turret, representing the user's main line of control to firing
 */
public class Turret extends Component {

    /**
     * How many projectiles will be spawned on each fire
     */
    public int spread = 3;

    /**
     * The number of times spawned projectiles can hit.
     */
    public int hopCount = 1;

    /**
     * The speed at which projectiles generated by this turret travel at
     */
    public float projectileSpeed = 10.0f; //TODO Apply upper limit to speed since if too fast it will skip over enemies :O

    /**
     * The multiplier for damage incurred on shields.
     */
    public float piercing = 1.0f; //FIXME After dropping shields, the remaining damage hits hp, is this ok with piercing?






}
