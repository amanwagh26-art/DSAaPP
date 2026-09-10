package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun InteractiveGeotagMap(
    isSatelliteMode: Boolean = false,
    onLocationDragged: (Float, Float) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isSatelliteMode) Color(0xFF1E293B) else Color(0xFFE5EBED))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX = (offsetX + dragAmount.x).coerceIn(-120f, 120f)
                    offsetY = (offsetY + dragAmount.y).coerceIn(-120f, 120f)
                    onLocationDragged(offsetX, offsetY)
                }
            }
            .testTag("interactive_geotag_map")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            if (isSatelliteMode) {
                // Dark satellite tone
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF283548))
                    )
                )
            } else {
                // Light vector map background
                drawRect(color = Color(0xFFF1F5F3))

                // Park greens
                val parkPath = Path().apply {
                    moveTo(width * 0.05f, height * 0.05f)
                    cubicTo(width * 0.2f, height * 0.08f, width * 0.28f, height * 0.25f, width * 0.18f, height * 0.35f)
                    cubicTo(width * 0.1f, height * 0.45f, width * 0.02f, height * 0.38f, width * 0.01f, height * 0.25f)
                    close()
                }
                drawPath(path = parkPath, color = Color(0xFFD9EBD8))

                val parkPath2 = Path().apply {
                    moveTo(width * 0.7f, height * 0.6f)
                    cubicTo(width * 0.85f, height * 0.55f, width * 0.95f, height * 0.65f, width * 0.98f, height * 0.8f)
                    cubicTo(width * 0.88f, height * 0.95f, width * 0.72f, height * 0.92f, width * 0.68f, height * 0.75f)
                    close()
                }
                drawPath(path = parkPath2, color = Color(0xFFD9EBD8))

                // City building blocks
                drawRoundRect(
                    color = Color(0xFFE2E8E5),
                    topLeft = Offset(width * 0.1f + offsetX * 0.2f, height * 0.12f + offsetY * 0.2f),
                    size = Size(width * 0.15f, height * 0.12f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFFE2E8E5),
                    topLeft = Offset(width * 0.3f + offsetX * 0.2f, height * 0.1f + offsetY * 0.2f),
                    size = Size(width * 0.2f, height * 0.14f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFFE2E8E5),
                    topLeft = Offset(width * 0.1f + offsetX * 0.2f, height * 0.42f + offsetY * 0.2f),
                    size = Size(width * 0.22f, height * 0.16f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFFE2E8E5),
                    topLeft = Offset(width * 0.65f + offsetX * 0.2f, height * 0.14f + offsetY * 0.2f),
                    size = Size(width * 0.25f, height * 0.18f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFFE2E8E5),
                    topLeft = Offset(width * 0.68f + offsetX * 0.2f, height * 0.38f + offsetY * 0.2f),
                    size = Size(width * 0.28f, height * 0.14f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFFE2E8E5),
                    topLeft = Offset(width * 0.15f + offsetX * 0.2f, height * 0.72f + offsetY * 0.2f),
                    size = Size(width * 0.32f, height * 0.2f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }

            // Streets / Roads grid
            val streetColor = if (isSatelliteMode) Color(0xFF475569) else Color.White
            val streetStroke = 20f

            // Secondary streets
            drawLine(
                color = streetColor,
                start = Offset(0f, height * 0.3f + offsetY * 0.4f),
                end = Offset(width, height * 0.3f + offsetY * 0.4f),
                strokeWidth = streetStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = streetColor,
                start = Offset(0f, height * 0.68f + offsetY * 0.4f),
                end = Offset(width, height * 0.68f + offsetY * 0.4f),
                strokeWidth = streetStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = streetColor,
                start = Offset(width * 0.38f + offsetX * 0.4f, 0f),
                end = Offset(width * 0.38f + offsetX * 0.4f, height),
                strokeWidth = streetStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = streetColor,
                start = Offset(width * 0.62f + offsetX * 0.4f, 0f),
                end = Offset(width * 0.62f + offsetX * 0.4f, height),
                strokeWidth = streetStroke,
                cap = StrokeCap.Round
            )

            // Main Metro Corridor Arterial (MG Road)
            val metroY = height * 0.5f + offsetY * 0.4f
            drawLine(
                color = Color(0xFFFFE082),
                start = Offset(-20f, metroY),
                end = Offset(width + 20f, metroY),
                strokeWidth = 32f
            )
            drawLine(
                color = Color(0xFFFBC02D),
                start = Offset(-20f, metroY),
                end = Offset(width + 20f, metroY),
                strokeWidth = 24f
            )

            // Metro Pillar cross markings
            val pillarSpacing = width / 6f
            for (i in 0..6) {
                val px = i * pillarSpacing + (offsetX * 0.4f % pillarSpacing)
                drawLine(
                    color = Color(0xFF131B2E),
                    start = Offset(px, metroY - 12f),
                    end = Offset(px, metroY + 12f),
                    strokeWidth = 6f
                )
            }
        }

        // Center Location Pin with Multi-stage Radar Pulse
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) },
            contentAlignment = Alignment.Center
        ) {
            // Animated radar ripple
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(Color(0xFF1E40AF).copy(alpha = pulseAlpha))
            )

            // Inner soft halo
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF316BF3).copy(alpha = 0.25f))
            )

            // Pin Shadow
            Box(
                modifier = Modifier
                    .offset(y = 20.dp)
                    .size(width = 36.dp, height = 12.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.2f))
            )

            // The Physical Pin Marker
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF00288E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
