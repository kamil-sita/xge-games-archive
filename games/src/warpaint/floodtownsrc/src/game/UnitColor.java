package game;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public enum UnitColor {
    red, green, blue;

    @NotNull
    public Color getColor() {
        switch (this) {
            case red:
                return Color.RED;
            case green:
                return Color.GREEN;
            case blue:
                return Color.BLUE;
        }
        return Color.WHITE;
    }

    public static UnitColor getRandom() {
        double r = Math.random();
        if (r < 0.3333) {
            return red;
        } else if (r < 0.66666) {
            return blue;
        }
        return green;
    }

    public UnitColor next() {
        switch (this) {
            case red:
                return green;
            case green:
                return blue;
            case blue:
                return red;
        }
        return null;
    }
}
