package graphics;

import java.awt.*;

public interface Renderable {
    void setRotation(double rotation);
    void render(Graphics2D graphics2D, boolean isDebugging);
    boolean isRenderOnTop();
    RenderPriority getRenderPriority();
}
