package gameObjects;

import java.util.ArrayList;
import java.util.HashMap;

public final class BulletManager {

    private HashMap<String, Bullet> bulletTypes  = new HashMap<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public BulletManager() {
    }

    public void addType(String key, Bullet bullet) {
        bulletTypes.put(key, bullet);
    }

    public Bullet getCopyOfAndAddToList(String key) {
        var bullet = bulletTypes.get(key);
        if (bullet == null) return null;
        var copy = bullet.getCopy();
        bullets.add(copy);
        return copy;
    }

    public void update() {
        for (int i = 0; i < bullets.size(); i++) {
            var bullet = bullets.get(i);
            if (bullet == null) continue;
            bullet.update();
            if (bullet.isSetToBeRemoved()) {
                bullets.remove(bullet);
                i--;
            }
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }


}
