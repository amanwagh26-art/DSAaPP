package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriorityTier
import com.example.data.model.TicketStatus
import com.example.ui.theme.StatusCriticalBg
import com.example.ui.theme.StatusCriticalDot
import com.example.ui.theme.StatusCriticalText
import com.example.ui.theme.StatusInProgressBg
import com.example.ui.theme.StatusInProgressDot
import com.example.ui.theme.StatusInProgressText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingDot
import com.example.ui.theme.StatusPendingText
import com.example.ui.theme.StatusResolvedBg
import com.example.ui.theme.StatusResolvedDot
import com.example.ui.theme.StatusResolvedText

@Composable
fun StatusBadge(
    status: TicketStatus,
    modifier: Modifier = Modifier
) {
    when (status) {
        TicketStatus.PENDING -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(StatusPendingBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("status_badge_pending"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = StatusPendingText,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Pending Triage",
                    color = StatusPendingText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.02.sp
                )
            }
        }
        TicketStatus.IN_PROGRESS -> {
            val infiniteTransition = rememberInfiniteTransition(label = "spin")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
            )
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(StatusInProgressBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("status_badge_inprogress"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = StatusInProgressText,
                    modifier = Modifier
                        .size(13.dp)
                        .rotate(angle)
                )
                Text(
                    text = "In Progress",
                    color = StatusInProgressText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.02.sp
                )
            }
        }
        TicketStatus.RESOLVED -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(StatusResolvedBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("status_badge_resolved"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusResolvedText,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "Resolved",
                    color = StatusResolvedText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.02.sp
                )
            }
        }
        TicketStatus.REJECTED -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("status_badge_rejected"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rejected",
                    color = Color(0xFF64748B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PriorityBadge(
    priority: PriorityTier,
    modifier: Modifier = Modifier
) {
    when (priority) {
        PriorityTier.P1_HIGH_HAZARD -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFFDC2626))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "HIGH HAZARD",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.05.sp
                )
            }
        }
        PriorityTier.P2_MODERATE_RISK -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFFFEF3C7))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD97706))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MEDIUM PRIORITY",
                    color = Color(0xFF92400E),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        PriorityTier.P3_STANDARD_QUEUE, PriorityTier.P4_ROUTINE_CYCLE -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = priority.label.substringBefore(" -"),
                    color = Color(0xFF475569),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
