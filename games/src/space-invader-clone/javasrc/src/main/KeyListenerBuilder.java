package main;

public final class KeyListenerBuilder {

    private KeyListener keyListener;
    private boolean booleanValue = true;

    public KeyListenerBuilder(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public KeyListenerBuilder isKeyPressed(int keyNumber) {
        var keyListenerBuilder = new KeyListenerBuilder(keyListener);
        keyListenerBuilder.set(keyListener.isKeyPressed(keyNumber));
        return keyListenerBuilder;
    }

    public KeyListenerBuilder and(int keyNumber) {
        var keyListenerBuilder = new KeyListenerBuilder(keyListener);
        keyListenerBuilder.set(evaluate() && keyListener.isKeyPressed(keyNumber));
        return keyListenerBuilder;
    }

    public KeyListenerBuilder or(int keyNumber) {
        var keyListenerBuilder = new KeyListenerBuilder(keyListener);
        keyListenerBuilder.set(evaluate() || keyListener.isKeyPressed(keyNumber));
        return keyListenerBuilder;
    }

    public KeyListenerBuilder andNot(int keyNumber) {
        var keyListenerBuilder = new KeyListenerBuilder(keyListener);
        keyListenerBuilder.set(evaluate() && !keyListener.isKeyPressed(keyNumber));
        return keyListenerBuilder;
    }

    public boolean evaluate() {
        return booleanValue;
    }

    public void set(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public static KeyListenerBuilder get(KeyListener keyListener) {
        return new KeyListenerBuilder(keyListener);
    }
}
