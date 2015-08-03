package com.samynarrainen.tetera;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.samynarrainen.tetera.components.Bound;
import com.samynarrainen.tetera.components.Enemy;
import com.samynarrainen.tetera.components.Killable;
import com.samynarrainen.tetera.components.Position;
import com.samynarrainen.tetera.components.Renderable;
import com.samynarrainen.tetera.components.Turret;
import com.samynarrainen.tetera.entities.EnemyPlacer;
import com.samynarrainen.tetera.input.InputProcessing;
import com.samynarrainen.tetera.systems.CleanUpSystem;
import com.samynarrainen.tetera.systems.CollisionSystem;
import com.samynarrainen.tetera.systems.EnemyFiringSystem;
import com.samynarrainen.tetera.systems.EnemySpawnerSystem;
import com.samynarrainen.tetera.systems.MovementSystem;
import com.samynarrainen.tetera.systems.RenderableSystem;
import com.samynarrainen.tetera.systems.TurretFiringSystem;
import com.samynarrainen.tetera.entities.EntityFactory;
import com.samynarrainen.tetera.util.audio.AudioManager;
import com.samynarrainen.tetera.util.images.TextureManager;
import com.samynarrainen.tetera.util.images.TextureName;
import com.samynarrainen.tetera.util.Util;

public class Tetera extends ApplicationAdapter {
	SpriteBatch batch;

    /**
     * Game libGDX engine
     */
    private final Engine engine;

    /**
     * 2D view camera
     */
    public static OrthographicCamera camera;

    /**
     * Access to the textures used
     */
    public static final TextureManager textureManager = new TextureManager();

    /**
     * Allows us to render shapes
     */
    private ShapeRenderer shapeRenderer;

    /**
     * Allows us to draw textures
     */
    private Batch spriteBatch;



    /**
     * Game Constructor
     */
    public Tetera() {
        super();
        this.engine = new Engine();

    }
	
	@Override
	public void create () {
        /**
         * Settings for the camera
         */
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0.0f);

        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();
        this.textureManager.load();
        AudioManager.load();

		batch = new SpriteBatch();

        Util.WIDTH = Gdx.graphics.getWidth();
        Util.HEIGHT = Gdx.graphics.getHeight();
        Util.CENTRE_X = Util.WIDTH / 2;
        Util.CENTRE_Y = Util.HEIGHT /2;





        /**
         * Add systems to the engine
         */
        engine.addSystem(new RenderableSystem(20000, spriteBatch, shapeRenderer, camera));
        engine.addSystem(new InputProcessing(2000));
        engine.addSystem(new MovementSystem(2000));
        engine.addSystem(new TurretFiringSystem(2000));
        engine.addSystem(new CleanUpSystem(20000));
        //engine.addSystem(new BoundingDebugSystem(2000, shapeRenderer));
        engine.addSystem(new CollisionSystem(2000));
        engine.addSystem(new EnemyFiringSystem(2000));
        //engine.addSystem(new EnemySpawnerSystem(20000));

        /**
         * Add entities to the engine
         */

        //Test Turret
        Entity turret = new Entity();
        turret.add(new Position(Util.WIDTH/2,Util.HEIGHT/2));
        Renderable r = new Renderable();
        r.state = Renderable.RenderableState.SINGLE_ANIMATION;
        r.animation = new Animation(Util.FRAME_SPEED, textureManager.textureSheetMap.get(TextureName.TURRET_OPEN));
        turret.add(r);
        turret.add(new Turret());
        turret.add(new Bound(192/2));
        turret.add(new Killable(100000));



        //test enemy
        Entity enemy = new Entity();
        enemy.add(new Position(200,200));
        enemy.add(new Bound(108/3));
        enemy.add(new Killable(200));

        Renderable r2 = new Renderable();
        r2.state = Renderable.RenderableState.ANIMATION;
        r2.animation = new Animation(Util.FRAME_SPEED, textureManager.textureSheetMap.get(TextureName.SOLDIER_WALKING));


        float originX = Gdx.graphics.getWidth() / 2;
        float originY = Gdx.graphics.getHeight() / 2;

        float rotation = Util.angleFromTo(Util.unprojectVector(new Vector2(200,200)),
                Util.unprojectVector(new Vector2(originX, originY)));

        r2.rotation = rotation;

        enemy.add(r2);
        enemy.add(new Enemy());

        Vector2 velocity = new Vector2((float) Math.cos(Math.toRadians(rotation)), (float) Math.sin(Math.toRadians(rotation)));

        //enemy.add(new Velocity(velocity.x, velocity.y, 2));






        /**
         * Curve
         */
        EnemyPlacer.spawn(engine, EnemyPlacer.arc(5, 0, 90));

        EnemyPlacer.spawn(engine, EnemyPlacer.arc(5, 180, 270));

       // EnemyPlacer.spawn(engine, EnemyPlacer.line(1, 200, 200, 400,400));


        //drawStar(5, Util.WIDTH/2, Util.HEIGHT/2, 900, 900);



/*

        int midX = 500;
        int midY = 340;
        int radius[] = {118,40,90,40};
        int nPoints = 5;
        int[] X = new int[nPoints];
        int[] Y = new int[nPoints];

        for (double current=0.0; current<nPoints; current++)
        {
            int i = (int) current;
            double x = Math.cos(current * ((2 * Math.PI) / 20))*radius[i % 4];
            double y = Math.sin(current * ((2 * Math.PI) / 20))*radius[i % 4];


            X[i] = (int) (Util.WIDTH /2 + x+midX);
            Y[i] = (int) (Util.HEIGHT /2 + y+midY);

            engine.addEntity(EntityFactory.enemy(X[i], Y[i]));

        }

*/



        engine.addEntity(turret);
        engine.addEntity(enemy);
	}

    /**
     * Various states of the game, such as pause
     */
    public static enum GameState {
        PAUSE, RUNNING, RESUME;

    }

    /**
     * The current state of the game
     */
    public static GameState gameState = GameState.RUNNING;

    @Override
	public void render () {

        switch(gameState) {
            case RUNNING: {
                final float deltaTime = Gdx.graphics.getDeltaTime();

                Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

                camera.update();
                engine.update(deltaTime);
                break;
            }

            case PAUSE: {
                camera.update();
                break;
            }
        }



	}


    public double circleX(int sides, int angle) {
        double coeff = (double)angle/(double)sides;
        return Math.cos(2*coeff*Math.PI-(Math.PI/2));
    }

    public double circleY(int sides, int angle) {
        double coeff = (double)angle/(double)sides;
        return Math.sin(2*coeff*Math.PI-(Math.PI/2));
    }

    public void drawStar(int sides, int x, int y, int w, int h) {
        for(int i = 0; i < sides; i++) {
            int x1 = (int)(circleX(sides,i) * (double)(w)) + x;
            int y1 = (int)(circleY(sides,i) * (double)(h)) + y;
            int x2 = (int)(circleX(sides,(i+2)%sides) * (double)(w)) + x;
            int y2 = (int)(circleY(sides,(i+2)%sides) * (double)(h)) + y;

            //EnemyPlacer.spawn(engine, EnemyPlacer.line(2, x1, y1, x2, y2));

            engine.addEntity(EntityFactory.enemy(x1, y1));
        }
    }
}
