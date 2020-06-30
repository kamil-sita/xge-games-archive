package gameobjects;

import game.UnitColor;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Unit extends GameObject {

    private UnitColor gunColor;
    private UnitColor bodyColor;
    private double healthPoints = 15.0;
    private double range = 50;
    private double speed = 0.5;

    double xPos;
    double yPos;

    double rotation;
    double gunRotation;

    Team friendlyTeam;

    private boolean isTank = true;

    public Unit(UnitColor gunColor, UnitColor bodyColor, double xPos, double yPos, double rotationDgr, Team friendlyTeam) {
        this.gunColor = gunColor;
        this.bodyColor = bodyColor;

        this.xPos = xPos;
        this.yPos = yPos;
        updateInternalPos();

        this.rotation = rotationDgr;
        this.gunRotation = 0;

        this.friendlyTeam = friendlyTeam;
    }

    public Unit setBase() {
        isTank = false;
        healthPoints = 100;
        return this;
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        if (isTank) renderTank(graphics2D);
    }

    private void renderTank(@NotNull Graphics2D graphics2D) {
        int width = 15;
        int height = 25;

        graphics2D.setStroke(new BasicStroke(1.5f));

        //drawing shadow

        Graphics2D g2dShadow = (Graphics2D) graphics2D.create();


        Rectangle bodyShadow = new Rectangle(-(width/2), -(height/2), width, height);
        g2dShadow.translate(getXPos(),  getYPos() + 5);
        g2dShadow.rotate(Math.toRadians(rotation));
        g2dShadow.setColor(new Color(0,0,0,128));
        g2dShadow.fill(bodyShadow);

        g2dShadow.dispose();

        //drawing body
        Graphics2D g2dcopy = (Graphics2D) graphics2D.create();


        Rectangle bodyRect = new Rectangle(-(width/2), -(height/2), width, height);
        g2dcopy.translate(getXPos(),  getYPos());
        g2dcopy.rotate(Math.toRadians(rotation));
        g2dcopy.setColor(Color.BLACK);
        g2dcopy.draw(bodyRect);
        g2dcopy.setColor(bodyColor.getColor());
        g2dcopy.fill(bodyRect);

        g2dcopy.dispose();

        //drawing gun
        Graphics2D g2dcopy2 = (Graphics2D) graphics2D.create();

        double circleRadius = width/2.0;

        g2dcopy2.setColor(Color.BLACK);
        g2dcopy2.drawOval((int) (xPos - circleRadius), (int) (yPos - circleRadius), (int) (circleRadius * 2), (int) (circleRadius * 2));

        g2dcopy2.setColor(gunColor.getColor());
        g2dcopy2.fillOval((int) (xPos - circleRadius), (int) (yPos - circleRadius), (int) (circleRadius * 2), (int) (circleRadius * 2));


        Rectangle gunRect = new Rectangle(-(width/12), -(height/2) - 5, width/6, height/2);
        g2dcopy2.translate(getXPos(),  getYPos());
        g2dcopy2.rotate(Math.toRadians(gunRotation));
        g2dcopy.setColor(Color.BLACK);
        g2dcopy2.draw(gunRect);
        g2dcopy2.setColor(gunColor.getColor());
        g2dcopy2.fill(gunRect);

        g2dcopy2.dispose();

        //render hp
        graphics2D.setColor(Color.YELLOW);
        int hpBar = (int) (2 * healthPoints);
        graphics2D.fillRect((int) xPos - 25/2, (int) yPos + 10, hpBar, 1);
    }

    public double distance(Unit unit) {
        if (unit == null) return Double.POSITIVE_INFINITY;
        return Math.sqrt(Math.pow(xPos - unit.xPos, 2) + Math.pow(yPos - unit.yPos, 2));
    }


    Unit targetedEnemy = null;
    Team enemyTeam;
    public void action(Team enemyTeam, Bullets bullets) {
        this.enemyTeam = enemyTeam;
        findNewTarget();
        if (targetedEnemy != null) {
            if (targetedEnemy.healthPoints <= 0) {
                targetedEnemy = null;
                return;
            }
            rotateAndMoveTowards(targetedEnemy);
            shoot(targetedEnemy, bullets);
        }
    }

    private void findNewTarget() {
        targetedEnemy = enemyTeam.getClosestTo(this);
    }

    public void dealDamage(double value, UnitColor damageColor) {
        double modifier = 0.085;
        if (damageColor == bodyColor) {
            modifier = 1.0;
        }
        healthPoints -= value * modifier;
    }

    int iteration = 0;
    int fireIteration = 60 + (int) (Math.random() * 5);
    private void shoot(Unit target, Bullets bullets) {
        iteration++;
        if (distance(target) <= 220) {
            if (iteration % fireIteration == 0) {
                bullets.add(new Bullet(xPos, yPos, gunRotation, distance(target), target, gunColor, bullets));
            }
        }
    }

    private void rotateAndMoveTowards(Unit unit) {

        double targetRotation = angleTo(unit);
        gunRotation = targetRotation;
        slowlyRotateBody(targetRotation);
        if (distance(unit) >= 150) {
            if (!friendlyUnitInFront()) {
                moveForward();
            }
        }
    }

    private boolean slowlyRotateBody(double target) {
        final double maxRotSpeed = 0.8;
        if (angleBetweenAngles(target, rotation) < maxRotSpeed) {
            rotation = target;
            return true;
        } else {
            boolean moveAnyways = Math.abs(target - rotation) < 60;
            double testAngle1 = angleBetweenAngles(rotation + maxRotSpeed, target);
            double testAngle2 = angleBetweenAngles(rotation - maxRotSpeed, target);

            if (testAngle1 < testAngle2) {
                rotation += maxRotSpeed;
            } else {
                rotation -= maxRotSpeed;
            }
            return moveAnyways;
        }
    }

    private double angleBetweenAngles(double angle1, double angle2) {
        double a = angle1 - angle2;
        a = Math.abs((a + 180) % 360) - 180;
        return Math.abs(a);
    }

    private void moveForward() {
        double dx = Math.sin(Math.toRadians(rotation));
        double dy = - Math.cos(Math.toRadians(rotation));
        xPos += speed * dx;
        yPos += speed * dy;
        updateInternalPos();
    }

    private boolean friendlyUnitInFront() {
        for (Unit unit : friendlyTeam.getUnits()) {
            if (unit == null) continue;
            if (unit == this) continue;
            double angle = angleBetweenAngles(rotation, angleTo(unit));
            double distance = distance(unit);
            if (angle <= 45) { //in front
                if (distance < 40) {
                    return true;
                }
            }
            if (angle < 90) { //in front
                if (distance < 10) {
                    return true;
                }
            }
        }
        return false;
    }

    private double angleTo(Unit unit) {
        double yDiff = yPos - unit.yPos;
        double xDiff = xPos - unit.xPos;

        double angle = 90 - Math.toDegrees(Math.atan2(yDiff, xDiff));

        return Math.abs(360 - angle)%360;
    }

    private void updateInternalPos() {
        setXPos((int) xPos);
        setYPos((int) yPos);
    }


    public double getHealthPoints() {
        return healthPoints;
    }

    public UnitColor getGunColor() {
        return gunColor;
    }

    public UnitColor getBodyColor() {
        return bodyColor;
    }
}
