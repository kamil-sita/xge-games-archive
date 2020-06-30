package interactions.listeners

import interactions.GameObject


class InteractionListener {

    var keyListener: KeyListener = KeyListener()
        private set
    var mouseListener: MouseListener = MouseListener()
        private set

    fun mouseX() : Int {
        return mouseListener.x
    }

    fun mouseY() : Int {
        return mouseListener.y
    }

    fun isMouseClicking() = mouseListener.isClicking

    fun isMouseOver(gameObject: GameObject): Boolean {
        //could be inlined for some really small optimization
        val xPos = gameObject.xPos
        val yPos = gameObject.yPos
        val width = gameObject.width
        val height = gameObject.height

        val xCorrect = mouseX() >= xPos && mouseX() < xPos + width
        val yCorrect = mouseY() >= yPos && mouseY() < yPos + height

        return xCorrect && yCorrect
    }
}
