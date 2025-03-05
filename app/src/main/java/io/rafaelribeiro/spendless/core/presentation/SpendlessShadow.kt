package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp

fun Modifier.spendlessShadow(): Modifier {
    return this.shadow(
        elevation = 18.dp,
        shape = RoundedCornerShape(16.dp),
        spotColor = DefaultShadowColor.copy(
            red = 24 / 255f,
            green = 0 / 255f,
            blue = 64 / 255f,
            alpha = 0.4f
        ),
    )
}
