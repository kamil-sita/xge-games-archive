package gameobjects;

public class GameResources {
    public int money;
    public int producedPower;
    public int usedPower;
    public int producedFood;
    public int housing;
    public int workingInhabitants;

    public int getAvailableFood() {
        return producedFood - getInhabitants();
    }

    public int getAvailablePower() {
        return producedPower - usedPower;
    }

    public int getInhabitants() {
        return Math.min(housing, producedFood);
    }

    public int getUnemployed() {
        return getInhabitants() - workingInhabitants;
    }

    public void clearBeforeIteration() {
        producedPower = 0;
        producedFood = 0;
        usedPower = 0;
        housing = 0;
        workingInhabitants = 0;
    }

}
