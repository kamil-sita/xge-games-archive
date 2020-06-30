package gameObjects;

import game.BasicGameLogic;
import game.Position;

public class Weapon {

    private String weaponName;
    private BulletManager bulletManager;
    private String bulletKey;
    private double weaponCooldown;

    public Weapon(String weaponName, BulletManager bulletManager, String bulletKey, double weaponCooldown) {
        this.weaponName = weaponName;
        this.bulletManager = bulletManager;
        this.bulletKey = bulletKey;
        this.weaponCooldown = weaponCooldown;
    }

    private long lastTimeBulletEmited = 0;

    public Bullet spawnBullet(Position position) {
        if (System.nanoTime() - lastTimeBulletEmited > weaponCooldown * BasicGameLogic.NANOSECONDS_IN_SECOND) {
            var bullet = bulletManager.getCopyOfAndAddToList(bulletKey);
            bullet.setVisible(true);
            position.copyPositionOnto(bullet.getPosition());
            lastTimeBulletEmited = System.nanoTime();
            return bullet;
        }
        return null;
    }



}
