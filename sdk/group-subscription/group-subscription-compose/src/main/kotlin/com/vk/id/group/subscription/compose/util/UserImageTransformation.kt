package com.vk.id.group.subscription.compose.util

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import coil.size.Size
import coil.transform.Transformation

internal class UserImageTransformation(
    private val backgroundColor: Int
) : Transformation {

    companion object {
        private const val IMAGE_WIDTH_DP = 24
        private const val WHITE_CIRCLE_OFFSET_DP = 9
    }

    override val cacheKey: String
        get() = backgroundColor.toString()

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = minOf(input.width, input.height)
        val radius = minSize / 2f
        val output = Bitmap.createBitmap(minSize, minSize, input.config ?: error("Null config"))
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)
            drawSurroundingCircle(input, radius)
        }

        return output
    }

    private inline fun Bitmap.applyCanvas(block: Canvas.() -> Unit): Bitmap {
        val c = Canvas(this)
        c.block()
        return this
    }

    private fun Canvas.drawSurroundingCircle(input: Bitmap, radius: Float) {
        drawCircle(
            -input.width / IMAGE_WIDTH_DP.toFloat() * WHITE_CIRCLE_OFFSET_DP,
            input.height / 2f,
            radius,
            Paint().apply {
                color = backgroundColor
                style = Paint.Style.FILL
            }
        )
    }
}
