package gameobjects

import graphics.TextImage
import interactions.GameObject
import java.awt.BasicStroke
import java.awt.Color
import java.awt.GradientPaint
import java.awt.Graphics2D

class SimpleMessage : GameObject() {

    private var message = "Message here"

    init {
        interestedInMouse = false
    }

    override fun render(graphics2D: Graphics2D) {
        val borderOffset = 5
        val lastPaint = graphics2D.paint
        graphics2D.paint = GradientPaint(0f, 490f, Color(255, 255, 255, 0), 0f, 720f, Color(169, 232, 232, 155))
        graphics2D.fillRect(0 + borderOffset, 550 + borderOffset, 1000 - 2 * borderOffset, 170 - 3 * borderOffset)
        graphics2D.paint = lastPaint

        //text
        val image = TextImage.renderTextWithBlurredBackground(1000, 220, message)
        graphics2D.drawImage(image, 0, 550, null)

        //border
        graphics2D.color = Color(240, 240, 190)
        graphics2D.stroke = BasicStroke(1.7f)
        graphics2D.drawRect(0 + borderOffset, 550 + borderOffset, 1000 - 2 * borderOffset, 170 - 3 * borderOffset)

    }

    fun setString(message: String): SimpleMessage {
        this.message = message
        return this
    }
}
