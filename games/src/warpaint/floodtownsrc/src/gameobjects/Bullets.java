package gameobjects;

import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import stages.GameplayStage;

import java.awt.*;
import java.util.ArrayList;

public class Bullets extends GameObject {

    private ArrayList<Bullet> bullets = new ArrayList<>();
    private GameplayStage gameplayStage;

    public Bullets(GameplayStage gameplayStage) {
        this.gameplayStage = gameplayStage;
    }

    public void add(Bullet bullet) {
        bullets.add(bullet);
    }

    public void remove(Bullet bullet) {
        bullets.remove(bullet);
    }

    public void action() {
        ArrayList<Bullet> bulletsCopy = new ArrayList<>(bullets);
        for (Bullet bullet : bulletsCopy) {
            bullet.action();
        }
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        ArrayList<Bullet> bulletsCopy = new ArrayList<>(bullets);
        for (Bullet bullet : bulletsCopy) {
            if (bullet != null) { //for whatever reason, it can happen
                bullet.render(graphics2D);
            }
        }
    }

    public GameplayStage getGameplayStage() {
        return gameplayStage;
    }
}
