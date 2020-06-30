package gameobjects.buttons;

import game.Sprites;
import graphics.SpriteLoader;
import interactions.GameObject;
import org.jetbrains.annotations.NotNull;
import sound.FloodtownMusicPlayer;
import sound.SoundEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class SimpleButton extends GameObject {

    int yHoverOffset = 0;
    protected String currentString;

    private int fontSize;
    private Color fontColor = new Color(240, 240, 156, 216);

    private Color shadow = new Color(0, 0, 0, 164);
    private Color lightShadow = new Color(0, 0, 0, 96);

    public SimpleButton(String defaultStirng, int xPos, int yPos, int fontSize, Runnable onClickAction) {
        this(defaultStirng, xPos, yPos, fontSize);
        setClickAction(onClickAction);
    }

    public SimpleButton(String defaultString, int xPos, int yPos, int fontSize) {
        this.currentString = defaultString;
        setInterestedInMouse(true);
        this.fontSize = fontSize;
        this.setXPos(xPos);
        this.setYPos(yPos);
    }

    @Override
    public void onHover() {
        FloodtownMusicPlayer.INSTANCE.play(SoundEffect.hover);
        yHoverOffset = -3;
    }

    @Override
    public void onDeHover() {
        yHoverOffset = 0;
    }

    public SimpleButton setClickAction(Runnable runnable) {
        setOnClickRun(runnable);
        return this;
    }

    @Override
    public void render(@NotNull Graphics2D graphics2D) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(getXPos(), getYPos() + yHoverOffset);

        Font font = new Font("Arial", Font.BOLD, fontSize);
        graphics2D.setFont(font);

        Rectangle2D bounds = font.getStringBounds(getCurrentString(), graphics2D.getFontRenderContext());
        int space = 4;
        Rectangle rectBounds = bounds.getBounds();

        //additional space, so text won't be that close to edges
        rectBounds.width = rectBounds.width + 2 * space;
        rectBounds.height = rectBounds.height + 2 * space;

        setWidth(rectBounds.width);
        setHeight(rectBounds.height);

        //shadow
        graphics2D.setColor(shadow);
        graphics2D.fillRect(getXPos() + space - yHoverOffset, getYPos() + space - yHoverOffset, getWidth(), getHeight() + 10);

        //main button
        Paint paint = graphics2D.getPaint();
        graphics2D.setPaint(new TexturePaint(SpriteLoader.load(Sprites.wood), new Rectangle(getXPos(), getYPos() + yHoverOffset, 700, 700)));
        graphics2D.fillRect(getXPos(), getYPos() + yHoverOffset, getWidth(), getHeight() + 10);
        graphics2D.setPaint(paint);

        //shadow on edges of the button
        Stroke stroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(2.3f));
        graphics2D.setColor(lightShadow);
        graphics2D.drawRect(getXPos(), getYPos() + yHoverOffset, getWidth(), getHeight() - 1);
        graphics2D.drawRect(getXPos(), getYPos() + getHeight() + yHoverOffset, getWidth(), 10);
        graphics2D.setStroke(stroke);

        //shadow on bottom of the button
        graphics2D.setColor(lightShadow);
        graphics2D.fillRect(getXPos(), getYPos() + getHeight() + yHoverOffset, getWidth(), 10);

        //drawing string
        graphics2D.setColor(fontColor);
        graphics2D.drawString(getCurrentString(), getXPos() + space + 1, getYPos() + getHeight() - 3 * space + yHoverOffset + 1);


    }

    public String getCurrentString() {
        return currentString;
    }

    public void setCurrentString(String currentString) {
        this.currentString = currentString;
    }

}
