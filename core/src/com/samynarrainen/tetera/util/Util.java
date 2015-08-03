package com.samynarrainen.tetera.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.samynarrainen.tetera.Tetera;

import java.util.Random;

/**
 * Created by Samy Narrainen on 30/05/2015.
 */
public class Util {

    /**
     * The instance of camera used by the game.
     */
    public static final Camera CAMERA = Tetera.camera;

    /**
     * The maximum distance an entity can be beyond the world
     */
    public static final float SAFE_BOUNDARY = 100.0f;

    /**
     * The width and height of the screen
     */
    public static int WIDTH, HEIGHT;

    /**
     * The points which represents the centre of viewable space
     */
    public static int CENTRE_X, CENTRE_Y;

    /**
     * The standardised speed at which animations jump a frame.
     */
    public static final float FRAME_SPEED = 0.057f;


    public static final Random random = new Random();

    /**
     * Calculates the angle in degrees between two Vectors in a 360 format
     * @param from
     * @param to
     * @return
     */
    public static float angleFromTo(Vector2 from, Vector2 to) {
        float x = (float) (to.x - from.x);
        float y = (float) (to.y - from.y);

        float rotation = (float) (Math.atan2(x, y) * (180.0f / Math.PI));
        //270 to account for landscape
        rotation = ((rotation + 270) % 360);

        return rotation;
    }

    /**
     * Calculates the position on the display, useful for instance when having to calculate the
     * angle between entity positions.
     * @param vector
     * @return
     */
    public static Vector2 unprojectVector(Vector2 vector) {
        Vector3 unprojected = CAMERA.unproject(new Vector3(vector.x, vector.y, 0.0f));

        return new Vector2(unprojected.x, unprojected.y);
    }

    /**
     * The distance between two cartesian coordinates
     * @param pos1
     * @param pos2
     * @return
     */
    public static float distance(Vector2 pos1, Vector2 pos2) {
        return (float) (Math.sqrt((Math.pow(pos2.x - pos1.x, 2) + Math.pow(pos2.y - pos1.y, 2))));
    }

}
