package btm.m.auraplayer.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.highlight.Highlight

/** All player menu surfaces share the same backdrop blur, tint mixing and soft edge light. */
@Composable
internal fun Modifier.playerMenuMaterial(backdrop: Backdrop, tint: Color, radius: androidx.compose.ui.unit.Dp = 26.dp): Modifier {
    val shape = RoundedCornerShape(radius)
    val isLight = tint.luminance() >= 0.5f
    if (!btm.m.auraplayer.ui.LocalAdvancedMaterialEnabled.current) {
        val fallbackBase = if (isLight) Color(0xFFF3F3F7) else Color(0xFF222226)
        val solidColor = if (tint.alpha >= .99f) tint else lerp(fallbackBase, tint.copy(alpha = 1f), tint.alpha)
        return background(solidColor, shape)
    }
    val overlayColor = tint.copy(alpha = if (isLight) .72f else .62f)
    return drawBackdrop(
        backdrop = backdrop,
        shape = { shape },
        effects = {
            colorControls(saturation = 1.12f)
            blur(7.dp.toPx())
        },
        highlight = {
            Highlight.Default.copy(alpha = if (isLight) .55f else .32f)
        },
        onDrawSurface = {
            drawRect(overlayColor)
        },
    )
}

/** Draws a [Highlight] rim using standard Compose outline drawing without RuntimeShaderCache. */
internal fun DrawScope.drawMiuixHighlight(
    highlight: Highlight,
    shape: Shape,
    paint: Paint,
) {
    if (highlight.width <= 0.dp || highlight.alpha <= 0f) return
    if (size.width <= 0f || size.height <= 0f) return
    val strokePx = minOf(highlight.width.toPx(), size.minDimension / 2f)
    val outline = shape.createOutline(size, layoutDirection, this)
    drawOutline(
        outline = outline,
        color = Color.White.copy(alpha = highlight.alpha * 0.45f),
        style = Stroke(width = strokePx),
        blendMode = BlendMode.Plus,
    )
}
