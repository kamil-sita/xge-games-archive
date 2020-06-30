package gameobjects.containers;

import game.BonusType;
import gameobjects.IconAndText;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class GlobalBonusContainer extends GameObject {

    private BonusType[] bonusTypes;
    private IconAndText[] iconAndTexts;

    public GlobalBonusContainer(int randomBonusCount) {
        bonusTypes = new BonusType[randomBonusCount];
        for (int i = 0; i < randomBonusCount; i++) {
            bonusTypes[i] = BonusType.getRandomBonus();
        }
        setInterestedInMouse(false);
        recreateIconAndTexts();
    }

    public void nextBonus() {
        System.arraycopy(bonusTypes, 1, bonusTypes, 0, 2);
        bonusTypes[2] = BonusType.none;
        recreateIconAndTexts();
    }

    public BonusType getBonus() {
        return bonusTypes[0];
    }

    private void recreateIconAndTexts() {
        iconAndTexts = new IconAndText[bonusTypes.length];
        for (int i = 0; i < iconAndTexts.length; i++) {
            BonusType bonus = bonusTypes[i];
            if (i > 0) {
                iconAndTexts[i] = new IconAndText(bonus.getText(), bonus.getSprite(), 10, 420 + i * 32);
                iconAndTexts[i].setScaling(0.1);
            } else {
                iconAndTexts[i] = new IconAndText(bonus.getText(), bonus.getSprite(), 10, 380);
            }
        }
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        for (IconAndText iconAndText : iconAndTexts) {
            iconAndText.render(graphics2D);
        }
    }
}
