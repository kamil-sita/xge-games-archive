package game;

import gameObjects.*;
import graphics.*;
import main.KeyListener;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public final class GameplayStage implements BaseGameplayStage {

    //private fields
    private KeyListener keyListener;
    private ArrayList<Entity> entities = new ArrayList<>();
    private Camera camera;
    private EnemyManager enemyManager = new EnemyManager();

    private PlayerShip player;

    private ArrayList<ParallaxEntity> stars = new ArrayList<>();
    private ArrayList<Mine> mines = new ArrayList<>();

    private BulletManager bulletManager = new BulletManager();

    private Hud hud;

    //various constants
    private final int STAR_COUNT = 1536;
    private final int MINE_COUNT = 32;
    //endof


    //constructor
    public GameplayStage(KeyListener keyListener, RenderingManager renderingManager, Hud hud) {
        //basic initialization
        this.keyListener = keyListener;
        this.hud = hud;
        camera = new Camera();
        camera.getPosition().y = 600;

        //player entity initialization
        var playerSprite = FlyweightGraphicsLoader.getFileFrom(FlyweightGraphicsLoader.playerShip);
        player = new PlayerShip(this, new Position(BasicGameLogic.WINDOW_WIDTH / 2, BasicGameLogic.WINDOW_HEIGHT / 2 - 100), 100, playerSprite, entities, keyListener);
        camera.setFollowedEntity(player);
        player.setVisible(true);
        player.setRenderOnTop(true);
        entities.add(player);

        //initializing stars
        for (int i = 0; i < STAR_COUNT; i++) {
            var star = new ParallaxEntity(this, new Position(Math.random() * (3 * BasicGameLogic.WINDOW_WIDTH) - BasicGameLogic.WINDOW_WIDTH, 3000 + Math.random() * -6000));
            entities.add(star);
            stars.add(star);
            star.setEntityImage(FlyweightGraphicsLoader.getFileFrom(FlyweightGraphicsLoader.star));
            star.setVisible(true);
        }

        //initializing mines
        for (int i = 0; i < MINE_COUNT; i++) {
            var mine = new Mine(this, new Position(100 * Math.random(), 1000.0 - 2500.0 * ((i * 1.0) / MINE_COUNT * 1.0)));
            if (i % 2 == 0) mine.getPosition().x += 1400;
            mine.setVisible(true);
            mine.setRenderOnTop(true);
            mines.add(mine);
            entities.add(mine);
        }

        //adding weapon to player
        bulletManager.addType("default", new Bullet(this, new Position(), FlyweightGraphicsLoader.getFileFrom(FlyweightGraphicsLoader.blast), 25, 8));
        var weapon = new Weapon("default", bulletManager, "default", 0.1);
        player.addWeapon(weapon);

        //adding lists to rendering manager
        renderingManager.setEntitesToRender(entities);
    }


    //fields used between iterations of updateAnimationAndCheckCollision() method
    private int iteration = 0;

    double cameraSpeedY = -20.5; //vertical speed of camera on game start (intro). After intro, camera follows player's playerShip

    ///method called on every tick (around 60Hz default)
    public StageState update() {
        iteration++;

        player.update();
        starUpdate();
        mineUpdate();
        bulletUpdate();
        enemiesUpdate();

        if (iteration < 100) {
            cameraSpeedY += 0.1;
            camera.getPosition().y += cameraSpeedY;
            return StageState.working;
        }

        if (iteration == 100) {
            camera.setFollowedEntity(player);
            player.setPlayerHasControl(true);
        }
        //end of mini intro

        camera.updatePos();
        updateHudTipsInto();

        //generating asteroids
        if (iteration < 1500) {
            if (iteration > 100) {
                if (iteration % 120 == 0) {
                    var asteroidPosition = new Position(BasicGameLogic.WINDOW_WIDTH / 2, player.getPosition().y - 900);
                    var asteroid = EnemyFactory.createAsteroidEnemy(this, asteroidPosition);
                    enemyManager.addEnemy(asteroid);
                    entities.add(asteroid);
                }
            }
            if (iteration > 400) {
                if (iteration % 100 == 0) {
                    var asteroidPosition = new Position(BasicGameLogic.WINDOW_WIDTH / 2, player.getPosition().y - 900);
                    var asteroid = EnemyFactory.createAsteroidEnemy(this, asteroidPosition);
                    enemyManager.addEnemy(asteroid);
                    entities.add(asteroid);
                }
            }
        } else {
            if (iteration % 65 == 0) {
                var asteroidPosition = new Position(BasicGameLogic.WINDOW_WIDTH / 2, player.getPosition().y - 900);
                var asteroid = EnemyFactory.createAsteroidEnemy(this, asteroidPosition);
                enemyManager.addEnemy(asteroid);
                entities.add(asteroid);
            }
        }

        getAndProcessPlayerInput();

        hud.setHealthPercentageBar(player.getHealthPoints() / player.getMaxHealth(), "", Hud.BarType.hp);

        if (player.getHealthPoints() < 0) {
            return StageState.finished;
        }
        return StageState.working;
    }

    private long lastTimeDebugChanged = System.nanoTime();
    private long lastTimeQualityChanged = System.nanoTime();

    private void getAndProcessPlayerInput() {
        final double WAITING_TIME = 1.0;

        //High quality rendering
        if (keyListener.isKeyPressed(KeyEvent.VK_L) && (System.nanoTime() - lastTimeQualityChanged) / BasicGameLogic.NANOSECONDS_IN_SECOND > WAITING_TIME) {
            Settings.HIGH_QUALITY_RENDERING = !Settings.HIGH_QUALITY_RENDERING;
            lastTimeQualityChanged = System.nanoTime();
            if (Settings.HIGH_QUALITY_RENDERING) {
                hud.addHudInfoToQueue("Enabled high quality rendering", 2);
            } else {
                hud.addHudInfoToQueue("Enabled low quality rendering", 2);
            }
        }

        //Debug mode
        if (keyListener.isKeyPressed(KeyEvent.VK_P) && (System.nanoTime() - lastTimeDebugChanged) / BasicGameLogic.NANOSECONDS_IN_SECOND > WAITING_TIME) {
            hud.addHudInfoToQueue("Enabled/disabled debug rendering", 2);
            lastTimeDebugChanged = System.nanoTime();
            Settings.DEBUG_MODE = !Settings.DEBUG_MODE;
        }
    }

    /**
     * Tips displayed to player on start, starting at given iteration
     */
    private void updateHudTipsInto() {

        if (iteration == 150) {
            hud.addHudInfoToQueue("Move using 'a' and 'd'", 4);
        }

        if (iteration == 250) {
            hud.addHudInfoToQueue("Shoot using space", 4);
        }

        if (iteration == 350) {
            hud.addHudInfoToQueue("Don't let any asteroids or enemies fall behind you", 4);
        }

    }

    ///updating star position should they move far behind player
    private void starUpdate() {
        final double DISTANCE_BEHIND_PLANE_TO_DISAPPEAR = 1400;
        final double MOVE_AFTER_DEATH = 4000;

        for (var star : stars) {
            if (star.getPosition().y - DISTANCE_BEHIND_PLANE_TO_DISAPPEAR > player.getPosition().y) {
                star.getPosition().y -= MOVE_AFTER_DEATH;
                star.getPosition().x = Math.random() * (3 * BasicGameLogic.WINDOW_WIDTH) - BasicGameLogic.WINDOW_WIDTH;
            }
        }
    }

    ///updating mine animation and their positon should they move far behind player
    private void mineUpdate() {
        final double DISTANCE_BEHIND_PLANE_TO_DISAPPEAR = 800;
        final double MOVE_AFTER_DEATH = 2500;

        for (var mine : mines) {
            mine.updateAnimationAndCheckCollision(player);
            if (mine.getPosition().y - DISTANCE_BEHIND_PLANE_TO_DISAPPEAR > player.getPosition().y) {
                mine.getPosition().y -= MOVE_AFTER_DEATH;
            }
        }
    }

    private void bulletUpdate() {
        bulletManager.update();
    }

    private void enemiesUpdate() {
        enemyManager.update(player, bulletManager.getBullets());
    }


    public Camera getCamera() {
        return camera;
    }

    @Override
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
}
