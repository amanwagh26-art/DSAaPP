package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IncidentImageGraphic(
    type: String,
    modifier: Modifier = Modifier,
    showFixedBadge: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE2E8F0))
    ) {
        when (type) {
            "pothole" -> PotholeGraphic()
            "streetlight" -> StreetlightGraphic()
            "garbage" -> GarbageGraphic()
            "water" -> WaterGraphic()
            else -> PotholeGraphic()
        }

        if (showFixedBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF003D28))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Fixed",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PotholeGraphic() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Asphalt road base background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A))
            )
        )

        // Cracked asphalt road texture lines
        drawLine(
            color = Color(0xFF475569),
            start = Offset(0f, height * 0.3f),
            end = Offset(width * 0.4f, height * 0.35f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF475569),
            start = Offset(width * 0.6f, height * 0.7f),
            end = Offset(width, height * 0.65f),
            strokeWidth = 2f
        )

        // The crater hole (dark irregular ellipse)
        val craterPath = Path().apply {
            moveTo(width * 0.2f, height * 0.5f)
            cubicTo(
                width * 0.25f, height * 0.25f,
                width * 0.75f, height * 0.3f,
                width * 0.82f, height * 0.55f
            )
            cubicTo(
                width * 0.85f, height * 0.8f,
                width * 0.3f, height * 0.85f,
                width * 0.2f, height * 0.5f
            )
            close()
        }

        drawPath(
            path = craterPath,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF020617), Color(0xFF0F172A), Color(0xFF1E293B)),
                center = Offset(width * 0.5f, height * 0.55f),
                radius = width * 0.4f
            )
        )

        // Water puddle reflection pool inside the crater
        val waterPath = Path().apply {
            moveTo(width * 0.3f, height * 0.55f)
            cubicTo(
                width * 0.35f, height * 0.45f,
                width * 0.68f, height * 0.48f,
                width * 0.72f, height * 0.6f
            )
            cubicTo(
                width * 0.7f, height * 0.72f,
                width * 0.4f, height * 0.75f,
                width * 0.3f, height * 0.55f
            )
            close()
        }

        drawPath(
            path = waterPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.45f), Color(0xFF0369A1).copy(alpha = 0.6f)),
                start = Offset(width * 0.3f, height * 0.45f),
                end = Offset(width * 0.7f, height * 0.75f)
            )
        )

        // Yellow safety chalk markings around the perimeter
        drawCircle(
            color = Color(0xFFFBBF24).copy(alpha = 0.8f),
            radius = 3.5f,
            center = Offset(width * 0.22f, height * 0.35f)
        )
        drawCircle(
            color = Color(0xFFFBBF24).copy(alpha = 0.8f),
            radius = 3.5f,
            center = Offset(width * 0.78f, height * 0.38f)
        )
        drawLine(
            color = Color(0xFFFBBF24).copy(alpha = 0.7f),
            start = Offset(width * 0.35f, height * 0.82f),
            end = Offset(width * 0.65f, height * 0.84f),
            strokeWidth = 3f
        )
    }
}

@Composable
private fun StreetlightGraphic() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Dusk sky gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155))
            )
        )

        // Ground / sidewalk
        drawRect(
            color = Color(0xFF1E293B),
            topLeft = Offset(0f, height * 0.75f),
            size = Size(width, height * 0.25f)
        )

        // Light pole
        drawLine(
            color = Color(0xFF94A3B8),
            start = Offset(width * 0.6f, height * 0.85f),
            end = Offset(width * 0.6f, height * 0.2f),
            strokeWidth = 5f
        )

        // Arm
        drawLine(
            color = Color(0xFF94A3B8),
            start = Offset(width * 0.6f, height * 0.2f),
            end = Offset(width * 0.45f, height * 0.25f),
            strokeWidth = 4f
        )

        // Lamp housing
        drawRoundRect(
            color = Color(0xFFCBD5E1),
            topLeft = Offset(width * 0.38f, height * 0.24f),
            size = Size(width * 0.16f, height * 0.08f),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Warm flickering amber glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFF59E0B).copy(alpha = 0.85f), Color(0xFFD97706).copy(alpha = 0.3f), Color.Transparent),
                center = Offset(width * 0.46f, height * 0.35f),
                radius = width * 0.35f
            )
        )
    }
}

@Composable
private fun GarbageGraphic() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Clean park background (green foliage + light clean stone pavement)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF065F46), Color(0xFF047857), Color(0xFF10B981))
            ),
            topLeft = Offset.Zero,
            size = Size(width, height * 0.65f)
        )

        // Pavement
        drawRect(
            color = Color(0xFFE2E8F0),
            topLeft = Offset(0f, height * 0.65f),
            size = Size(width, height * 0.35f)
        )

        // 3 Clean Green Municipal Dustbins
        val binWidth = width * 0.22f
        val binHeight = height * 0.38f
        val spacing = width * 0.06f
        var startX = width * 0.12f

        for (i in 0..2) {
            // Bin body
            drawRoundRect(
                color = Color(0xFF064E3B),
                topLeft = Offset(startX, height * 0.48f),
                size = Size(binWidth, binHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Bin lid
            drawRoundRect(
                color = Color(0xFF047857),
                topLeft = Offset(startX - 2f, height * 0.44f),
                size = Size(binWidth + 4f, height * 0.06f),
                cornerRadius = CornerRadius(2f, 2f)
            )
            startX += binWidth + spacing
        }
    }
}

@Composable
private fun WaterGraphic() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE2E7FF)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = Color(0xFF1E40AF),
            modifier = Modifier.size(38.dp)
        )
    }
}
