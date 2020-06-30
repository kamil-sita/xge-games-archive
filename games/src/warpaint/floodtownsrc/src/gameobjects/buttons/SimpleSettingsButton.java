package gameobjects.buttons;

public class SimpleSettingsButton extends SimpleButton {

    private Runnable textUpdateAction;

    public SimpleSettingsButton(int xPos, int yPos, Runnable onClickAction) {
        super("set text update action!!", xPos, yPos, 32);
        this.setOnClickRun(onClickAction);
    }

    public void setTextUpdateAction(Runnable textUpdateAction) {
        this.textUpdateAction = textUpdateAction;
        updateText();
    }

    private void updateText() {
        textUpdateAction.run();
    }

    @Override
    public void onClick() {
        if (getOnClickRun() != null) {
            getOnClickRun().run();
        }
        updateText();
    }
}
