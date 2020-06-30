package graphics

import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

object SimpleRenderer {
    fun renderImage(graphics2D: Graphics2D, bufferedImage: BufferedImage, x: Double, y: Double, scaling: Double) {
        val affineTransform = AffineTransform()
        affineTransform.translate(x, y)
        if (scaling != 1.0) {
            affineTransform.scale(scaling, scaling)
        }
        graphics2D.drawImage(bufferedImage, affineTransform, null)
    }


}
