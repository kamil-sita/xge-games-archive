package system;

import graphics.transforms.CacheableTransforms;

import java.awt.*;

public class Settings {
    static public boolean highQualityRendering = true;
    static public boolean showDebug = false;
    static public int windowWidth = 1280;
    static public int windowHeight = 720;
    static public int targetFramerate = 60;
    static public int targetUpdateRate = 60;
    static public boolean additionalEffects = true;

    public static void applySettings(Graphics2D graphics) {
        applySettings(graphics, false);
    }

    public static void applyHighestSettings(Graphics2D graphics2D) {
        applySettings(graphics2D, true);
    }

    private static void applySettings(Graphics2D graphics, boolean overrideToHighest) {
        if (Settings.highQualityRendering || overrideToHighest) {
            graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        }
    }
}
