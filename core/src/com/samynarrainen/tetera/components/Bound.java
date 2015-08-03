package com.samynarrainen.tetera.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Samy Narrainen on 31/05/2015.
 * Provides a means of collision
 *
 */
public class Bound extends Component {

    /**
     * The bounding radius of the entity,  given by a circle
     */
    public float radius = 0.0f;

    public Bound(float radius) {
        this.radius = radius;
    }

}
