package gameobjects;

import game.TeamRaport;
import game.UnitColor;
import interactions.GameObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class Team extends GameObject {

    private ArrayList<Unit> units = new ArrayList<>();
    private int xBaseLocation;
    private double initialRotation;

    public Team(int unitCount, int xBaseLocation, double rotation) {
        this.initialRotation = rotation;
        this.xBaseLocation = xBaseLocation;
        for (int i = 0; i < unitCount; i++) {
            units.add(unitCreator());
        }
    }

    @NotNull
    @Contract(" -> new")
    private Unit unitCreator() {
        return new Unit(UnitColor.getRandom(), UnitColor.getRandom(), xBaseLocation + 200 * Math.random(), 600 * Math.random() + 60, initialRotation, this);
    }

    public void addRandom(int count) {
        for (int i = 0; i < count; i++) {
            units.add(unitCreator());
        }
    }

    public void add(UnitColor bodyColor, UnitColor gunColor) {
        units.add(new Unit(gunColor, bodyColor, xBaseLocation + 200 * Math.random(), 600 * Math.random(), initialRotation, this));
    }

    public void add(UnitColor bodyColor, UnitColor gunColor, int yPos) {
        units.add(new Unit(gunColor, bodyColor, xBaseLocation + 200 * Math.random(), yPos + 50 * Math.random(), initialRotation, this));
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        ArrayList<Unit> unitsCopy = new ArrayList<>(units);
        for (Unit unit : unitsCopy) unit.render(graphics2D);
    }

    public void action(Team enemyTeam, Bullets bullets) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getHealthPoints() <= 0) {
                units.remove(i);
                i--;
                continue;
            }
            units.get(i).action(enemyTeam, bullets);
        }
    }

    public Unit getClosestTo(Unit unit) {
        Unit closest = null;
        double closestDistance = 0;
        for (Unit unitFromList : units) {
            if (closest == null) {
                closest = unitFromList;
                closestDistance = weightedDistance(unitFromList, unit);
            } else {
                double distance = weightedDistance(unit, unitFromList);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = unitFromList;
                }
            }
        }
        if (closest != null && closest.getHealthPoints() <= 0) {
            return null;
        }
        return closest;
    }

    private double weightedDistance(Unit unit1, Unit unit2) {
        final double Y_AXIS_WEIGHT = 4;
        double colorWeight = 1.0;
        if (unit1.getGunColor() == unit2.getBodyColor()) colorWeight *= 0.55; //wants to kill countered unit
        if (unit1.getBodyColor() == unit2.getGunColor()) colorWeight *= 1.25; //doesn't want to be countered
        return colorWeight * Math.sqrt(Math.pow(unit1.xPos - unit2.xPos, 2) + Y_AXIS_WEIGHT * Math.pow(unit1.yPos - unit2.yPos, 2));
    }

    public ArrayList<Unit> getUnits() {
        return new ArrayList<>(units);
    }

    public int getUnitCount() {
        return units.size();
    }

    public TeamReports generateRaport() {
        TeamReports raports = new TeamReports();

        for (Unit unit : units) {

            TeamRaport raport = raports.lower;

            if (unit.yPos <= 360) {
                raport = raports.upper;
                raports.upperCount++;
            } else {
                raports.lowerCount++;
            }

            switch (unit.getGunColor()) {
                case red:
                    raport.countGunRed++;
                    break;
                case green:
                    raport.countGunGreen++;
                    break;
                case blue:
                    raport.countGunBlue++;
                    break;
            }

            switch (unit.getBodyColor()) {
                case red:
                    raport.countBodyRed++;
                    break;
                case green:
                    raport.countBodyGreen++;
                    break;
                case blue:
                    raport.countBodyBlue++;
                    break;
            }
        }

        return raports;
    }

    public class TeamReports {
        public TeamRaport upper = new TeamRaport();
        public TeamRaport lower = new TeamRaport();
        public int upperCount = 0;
        public int lowerCount = 0;

        public int getUnitCount() {
            return upperCount + lowerCount;
        }
    }
}
