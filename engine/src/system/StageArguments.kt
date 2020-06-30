package system

import graphics.RenderingManager
import interactions.InteractionManager

class StageArguments(stageManager: StageManager, renderingManager: RenderingManager, interactionManager: InteractionManager) {
    var stageManager: StageManager
        internal set
    var renderingManager: RenderingManager
        internal set
    var interactionManager: InteractionManager
        internal set

    init {
        this.stageManager = stageManager
        this.renderingManager = renderingManager
        this.interactionManager = interactionManager
    }


}
