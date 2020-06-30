package gameobjects;


public enum BonusType {
    none,
    superCows,
    superCoal,
    goodEconomy;

    public static  BonusType getRandomBonus() {
        double r = Math.random();
        if (r < 0.333333) {
            return superCows;
        } else if (r < 0.66666) {
            return goodEconomy;
        }
        return superCoal;
    }
}
