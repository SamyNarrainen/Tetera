package com.samynarrainen.tetera.input;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Renderable;
import com.samynarrainen.tetera.components.Turret;
import com.samynarrainen.tetera.systems.TurretFiringSystem;
import com.samynarrainen.tetera.util.Mappers;
import com.samynarrainen.tetera.util.images.TextureManager;
import com.samynarrainen.tetera.util.images.TextureName;
import com.samynarrainen.tetera.util.Util;


/**
 * Created by Samy Narrainen on 29/05/2015.
 * The central point for controlling the response to user input.
 */
public class InputProcessing extends IteratingSystem implements InputProcessor, ApplicationListener, GestureDetector.GestureListener {

    /**
     * Holds the most recent touch location
     */
    Vector2 touchPos = new Vector2();

    /**
     * If there's an update that hasn't been processed.
     */
    Boolean touchUpdate = false;

    public InputProcessing(int priority) {
        //These are the entities which will react to user input.
        super(Family.all(Position.class, Renderable.class, Turret.class).get(), priority);
        Gdx.input.setInputProcessor(this);

    }

    @Override
    protected void processEntity(com.badlogic.ashley.core.Entity entity, float deltaTime) {
        Turret turret = Mappers.turretMapper.get(entity);

        /**
         * Case of tower
         */
        if(turret != null && touchUpdate) {
            Renderable renderable = Mappers.renderableMapper.get(entity);
            Position position = Mappers.positionMapper.get(entity);

            renderable.rotation = Util.angleFromTo(position.getPosition(), touchPos);

            /**
             * Handle fire animation
             */
            renderable.animation = new Animation(0.057f, TextureManager.textureSheetMap.get(TextureName.TURRET_SHOOT));
            renderable.animationTime = 0.0f;


        }




        //The update has been recognised.
        touchUpdate = false;

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchUpdate = true;


        touchPos = new Vector2(screenX, screenY);

        TurretFiringSystem.touchUpdate = true;
        TurretFiringSystem.touchPos = new Vector2(screenX, screenY);



        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
