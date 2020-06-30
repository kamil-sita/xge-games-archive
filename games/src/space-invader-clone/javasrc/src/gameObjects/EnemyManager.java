package gameObjects;

import java.util.ArrayList;

public class EnemyManager {

    ArrayList<Enemy> enemies = new ArrayList<>();

    public EnemyManager() {

    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void update(PhysicalEntity player, ArrayList<Bullet> bullets) {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(player, bullets);
            if (enemies.get(i).isSetToBeRemoved()) {
                enemies.remove(i);
                i--;
            }
        }
    }

}
