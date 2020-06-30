package interactions

import interactions.listeners.InteractionListener
import java.awt.Graphics2D

abstract class GameObject {

    var xPos: Int = 0
    var yPos: Int = 0
    var width: Int = 0
    var height: Int = 0

    var interestedInMouse = false

    private var lastMouseOver = true
    private var lastMouseClick = false
    var isClicked = false; private set
    var isMouseOver = false; private set


    var onClickRun: Runnable? = null

    abstract fun render(graphics2D: Graphics2D)

    open fun onHover() {

    }

    open fun whileHover(interactionListener: InteractionListener) {

    }

    open fun onDeHover() {

    }

    open fun onClick() {
        if (onClickRun != null) {
            onClickRun!!.run()
        }
    }

    fun updateMouseStates(hoverState: Boolean, clickState: Boolean, interactionListener: InteractionListener) {
        if (!interestedInMouse) return
        lastMouseClick = isClicked
        lastMouseOver = isMouseOver
        isMouseOver = hoverState
        if (isMouseOver) whileHover(interactionListener)
        if (isMouseOver and !lastMouseOver) {
            onHover()
        } else if (!isMouseOver and lastMouseOver) {
            onDeHover()
        }
        isClicked = (lastMouseClick and !clickState and isMouseOver)
        if (isClicked) {
            onClick()
        }
        isClicked = clickState
    }

}
