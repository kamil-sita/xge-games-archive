package interactions

import interactions.listeners.InteractionListener

class InteractionManager(private val interactionListener: InteractionListener) {

    private var gameObjects: ArrayList<GameObject>? = null
    private var gameObjectAtIterationStart: ArrayList<GameObject>? = null

    fun update() {
        if (gameObjects == null) return
        val localGameObjects = ArrayList<GameObject>()
        localGameObjects.addAll(gameObjects!!)
        gameObjectAtIterationStart = gameObjects
        var mouseAlreadyHovering = false
        for (i in (localGameObjects.size-1) downTo 0) {
            if (gameObjects == null) return
            if (gameObjects != gameObjectAtIterationStart) return
            val gameObject = gameObjects!![i]
            if (mouseAlreadyHovering) {
                gameObject.updateMouseStates(false, false, interactionListener)
                continue
            }
            val isHovering = interactionListener.isMouseOver(gameObject)
            if (isHovering and gameObject.interestedInMouse) {
                gameObject.updateMouseStates(true, interactionListener.isMouseClicking(), interactionListener)
                mouseAlreadyHovering = true
            } else {
                gameObject.updateMouseStates(false, false, interactionListener)
            }
        }
    }

    fun setGameObjects(gameObjects: ArrayList<GameObject>?) {
        this.gameObjects = gameObjects
    }

    fun getInteractionListener(): InteractionListener {
        return interactionListener
    }
}