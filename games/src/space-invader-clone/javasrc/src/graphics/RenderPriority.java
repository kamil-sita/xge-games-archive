package graphics;

public enum RenderPriority {
    background,
    foregroundBottom,
    foregroundTop,
    hud;

    public static final int minValue = 0;
    public static final int maxValue = 3;

    public int intValue() {
        switch (this) {
            case background:
                return 0;
            case foregroundBottom:
                return 1;
            case foregroundTop:
                return 2;
            case hud:
                return 3;
        }
        return -1;
    }

}
