package stages;


import game.ManagingAI;
import game.Sprites;
import game.UnitColor;
import gameobjects.*;
import gameobjects.buttons.SimpleButton;
import graphics.SpriteLoader;
import system.Settings;
import system.Stage;
import system.StageArguments;

import java.awt.*;

public class GameplayStage extends Stage {

    CanvasImage background;

    Team leftTeam;
    Team rightTeam;
    StageArguments stageArguments;
    Bullets bullets;

    //
    UnitColor selectedBodyColor = UnitColor.red;
    UnitColor selectedGunColor = UnitColor.red;
    SimpleButton bodyColor;
    SimpleButton gunColor;

    SimpleButton spawnDown;
    SimpleButton spawnUp;

    IconAndText tickets = new IconAndText("Tickets: x", null, 600, 30);



    public GameplayStage (StageArguments stageArguments) {
        super(stageArguments.getRenderingManager(), stageArguments.getInteractionManager());
        background = new CanvasImage(Sprites.background);
        add(background);

        int unitCount = 0;
        leftTeam = new Team(unitCount, 20, 90);
        rightTeam = new Team(unitCount, 1000, 270);
        add(leftTeam);
        add(rightTeam);
        this.stageArguments = stageArguments;
        bullets = new Bullets(this);
        add(bullets);

        bodyColor = new SimpleButton("Body color: RED", 10, 10, 16).setVisualHeight(3).setFontColor(Color.RED);
        bodyColor.setClickAction(() -> {
            selectedBodyColor = selectedBodyColor.next();
            bodyColor.setFontColor(selectedBodyColor.getColor());
            bodyColor.setCurrentString("Body color: " + selectedBodyColor.toString().toUpperCase());
        });

        gunColor = new SimpleButton("Gun color: RED", 190, 10, 16).setVisualHeight(3).setFontColor(Color.RED);
        gunColor.setClickAction(() -> {
            selectedGunColor = selectedGunColor.next();
            gunColor.setFontColor(selectedGunColor.getColor());
            gunColor.setCurrentString("Gun color: " + selectedGunColor.toString().toUpperCase());
        });


        spawnUp = new SimpleButton("Spawn here", 10, 200, 16);
        spawnDown = new SimpleButton("Spawn here", 10, 680, 16);
        add(tickets);

        spawnUp.setClickAction(() -> {
            attemptToSpawnTeamLeft(150);
        });

        spawnDown.setClickAction(() -> {
            attemptToSpawnTeamLeft(600);
        });


        add(spawnUp);
        add(spawnDown);
        add(bodyColor);
        add(gunColor);

        if (Settings.additionalEffects) {
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.vignette)));
            add(new NonInteractiveImage(SpriteLoader.load(Sprites.noise)));
        }

        refocus();
    }

    int iteration = 0;

    int leftTickets = 5;
    int rightTickets = 5;

    @Override
    public void update() {
        iteration++;
        leftTeam.action(rightTeam, bullets);
        rightTeam.action(leftTeam, bullets);
        bullets.action();
        if (iteration % (60 * 5) == 0) {
            leftTickets +=2;
            rightTickets+=2;
            System.out.println("+++");
        }
        if (iteration % (60) == 0) {
            Team.TeamReports leftReport = leftTeam.generateRaport();

            rightTickets = ManagingAI.countingAi(rightTeam, leftReport, rightTickets);
            System.out.println("________");
            System.out.println(leftTickets);
            System.out.println(rightTickets);
        }
        tickets.setText("Tickets: " + leftTickets);

    }

    public CanvasImage getBackground() {
        return background;
    }

    private void attemptToSpawnTeamLeft(int y) {
        if (leftTickets > 0) {
            leftTickets--;
            leftTeam.add(selectedBodyColor, selectedGunColor, y);
        }

    }
}
