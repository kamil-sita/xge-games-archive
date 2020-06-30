package graphics

import graphics.transforms.Blur
import system.Settings
import java.awt.*
import java.awt.image.BufferedImage

object TextImage {

    private val graphicsConfiguration: GraphicsConfiguration by lazy {GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration}
    private var hashMap = HashMap<TextMnemonic, BufferedImage>()

    fun renderTextWithBlurredBackground(width: Int, height: Int, text: String): BufferedImage {

        val mnemonic = TextMnemonic(text, width, height)

        if (hashMap.containsKey(mnemonic)) {
            return hashMap[mnemonic]!!
        }
        var image = graphicsConfiguration.createCompatibleImage(width, height, Transparency.TRANSLUCENT)
        var g2D = image.createGraphics()
        Settings.applyHighestSettings(g2D)

        //shadow
        g2D.color = Color(210, 210, 210, 220)
        g2D.font = Font("Arial", Font.BOLD, 20)
        var yOffset = 0
        for (line in text.split("\n")) {
            g2D.drawString(line, 15, 30 + yOffset)
            yOffset += g2D.fontMetrics.height + 6
        }
        g2D.dispose()
        image = Blur.okayBlur(image)


        //foreground
        g2D = image.createGraphics()
        Settings.applyHighestSettings(g2D)
        g2D.color = Color(30, 30, 30, 255)
        g2D.font = Font("Arial", Font.BOLD, 20)
        yOffset = 0
        for (line in text.split("\n")) {
            g2D.drawString(line, 15, 30 + yOffset)
            yOffset += g2D.fontMetrics.height + 6
        }

        g2D.dispose()

        hashMap[mnemonic] = image
        return image
    }

    fun clear() {
        hashMap.clear()
    }

    private class TextMnemonic(val text: String, val width: Int, val height: Int) {
        override fun hashCode(): Int {
            return text.hashCode() + width + height
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TextMnemonic

            if (text != other.text) return false
            if (width != other.width) return false
            if (height != other.height) return false

            return true
        }
    }

}