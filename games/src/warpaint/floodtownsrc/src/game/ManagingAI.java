package game;

import gameobjects.Team;
import gameobjects.Unit;

import static game.UnitColor.*;

public class ManagingAI {

    public static int countingAi(Team team, Team.TeamReports enemyTeamReport, int tickets) {

        int target = calculateTarget(team, enemyTeamReport, tickets);
        splitLanes(team, enemyTeamReport, target);

        return tickets - target;
    }

    private static int calculateTarget(Team team, Team.TeamReports enemyTeamRaport, int tickets) {
        int target = 0;
        if (team.getUnitCount() < 2) {
            target = 2 - team.getUnitCount();
        }

        int teamUnitCount = team.getUnitCount();
        int enemyUnitCount = enemyTeamRaport.getUnitCount();


        if (teamUnitCount < enemyUnitCount * 1.5) {
            target = (int) ((enemyUnitCount - teamUnitCount) * 0.6);
        }

        if (target < tickets / 5) {
            target = tickets/ 5;
        }

        if (target > tickets) {
            target = tickets;
        }
        return target;
    }

    private static void splitLanes(Team team, Team.TeamReports enemyTeamRaport, int tickets) {
        int teamUpperCount = 0;
        int teamLowerCount = 0;

        for (Unit unit : team.getUnits()) {
            if (unit.getYPos() <= 360) {
                teamUpperCount++;
            } else {
                teamLowerCount++;
            }
        }

        double enemyToTeamUpper = enemyTeamRaport.upperCount / (teamUpperCount + 1.0);
        double enemyToTeamLower = enemyTeamRaport.lowerCount / (teamLowerCount + 1.0);

        int ticketsUpper = (int) (tickets * (enemyToTeamUpper) / (enemyToTeamLower + enemyToTeamUpper));
        int ticketsLower = tickets - ticketsUpper;

        spawn(team, enemyTeamRaport.upper, ticketsUpper, 170);
        spawn(team, enemyTeamRaport.lower, ticketsLower, 640);
    }

    private static void spawn(Team team, TeamRaport enemyTeamRaport, int tickets, int yPos) {
        UnitColor mostPopularBodyColor = getRandom();
        UnitColor leastPopularGunColor = getRandom();
        //body
        if (enemyTeamRaport.countBodyBlue >= enemyTeamRaport.countGunGreen && enemyTeamRaport.countBodyBlue >= enemyTeamRaport.countGunRed) mostPopularBodyColor = blue;
        if (enemyTeamRaport.countBodyRed >= enemyTeamRaport.countGunGreen && enemyTeamRaport.countBodyRed >= enemyTeamRaport.countGunBlue) mostPopularBodyColor = red;
        if (enemyTeamRaport.countBodyGreen >= enemyTeamRaport.countGunBlue && enemyTeamRaport.countBodyGreen >= enemyTeamRaport.countGunRed) mostPopularBodyColor = green;
        //gun
        if (enemyTeamRaport.countGunBlue <= enemyTeamRaport.countGunGreen && enemyTeamRaport.countGunBlue <= enemyTeamRaport.countGunRed) leastPopularGunColor = blue;
        if (enemyTeamRaport.countGunRed <= enemyTeamRaport.countGunGreen && enemyTeamRaport.countGunRed <= enemyTeamRaport.countGunBlue) leastPopularGunColor = red;
        if (enemyTeamRaport.countGunGreen <= enemyTeamRaport.countGunBlue && enemyTeamRaport.countGunGreen <= enemyTeamRaport.countGunRed) leastPopularGunColor = green;


        for (int i = 0; i < tickets; i++) {
            UnitColor gunColor = getRandom();
            UnitColor bodyColor = getRandom();
            if (Math.random() > 0.25) {
                gunColor = mostPopularBodyColor;
            }
            if (Math.random() > 0.25) {
                bodyColor = leastPopularGunColor;
            }
            team.add(bodyColor, gunColor, yPos);
        }
    }

}
