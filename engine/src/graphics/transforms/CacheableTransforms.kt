package graphics.transforms

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

object CacheableTransforms {

    private var transforms: HashMap<Transform, BufferedImage> = HashMap()

    fun scale(input: BufferedImage, scale: Double, quality: Quality): BufferedImage {
        val transform = Transform(scale, TransformType.Scale, input)

        if (transforms.containsKey(transform)) {
            val output = transforms.get(transform)
            if (output != null) return output
        }

        val interpolationType: Int = when (quality) {
            Quality.Best -> AffineTransformOp.TYPE_BICUBIC
            Quality.Balanced -> AffineTransformOp.TYPE_BILINEAR
            Quality.Fast -> AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        }

        val affineType = AffineTransform()
        affineType.scale(scale, scale)
        val affine = AffineTransformOp(affineType, interpolationType)
        val output = affine.filter(input, null)

        transforms[transform] = output
        return output
    }

    fun blur(input: BufferedImage, radius: Double, quality: Quality): BufferedImage {
        val transform = Transform(radius, TransformType.Blur, input)

        if (transforms.containsKey(transform)) {
            val output = transforms[transform]
            if (output!= null) return output
        }

        val output = Blur.okayBlur(input)
        transforms[transform] = output
        return output
    }

    fun clearCache() {
        transforms = HashMap()
    }

    class Transform(val value: Double, val type: TransformType, val bufferedImage: BufferedImage) {
        override fun hashCode(): Int {
            return value.hashCode() + type.hashCode() + bufferedImage.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Transform

            if (value != other.value) return false
            if (type != other.type) return false
            if (bufferedImage != other.bufferedImage) return false

            return true
        }
    }

    enum class TransformType {
        None,
        Scale,
        Blur
    }

    enum class Quality {
        Fast,
        Balanced,
        Best
    }
}