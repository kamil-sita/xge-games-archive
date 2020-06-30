package graphics.transforms

import java.awt.image.BufferedImage
import java.awt.image.ConvolveOp
import java.awt.image.Kernel

object Blur {
    fun okayBlur(input: BufferedImage): BufferedImage {
        val convolveOp = ConvolveOp(Kernel(5, 5,
                floatArrayOf(
                        0f, 0f, 0.05f, 0f, 0f,
                        0f, 0.05f, 0.1f, 0.05f, 0f,
                        0.05f, 0.1f, 0.2f, 0.1f, 0.05f,
                        0f, 0.05f, 0.1f, 0.05f, 0f,
                        0f, 0f, 0.05f, 0f, 0f
                )
        ))


        return convolveOp.filter(input, null)
    }

    fun smallBlur(input: BufferedImage): BufferedImage {
        val convolveOp = ConvolveOp(Kernel(3, 3,
                floatArrayOf(
                        0f, 0.2f, 0.2f,
                        0.2f, 0.2f, 0.2f,
                        0f, 0.2f, 0f
                )
        ))


        return convolveOp.filter(input, null)
    }
}