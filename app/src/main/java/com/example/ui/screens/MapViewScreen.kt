package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.data.model.TicketStatus
import com.example.data.repository.CivicRepository
import com.example.ui.components.IncidentImageGraphic
import com.example.ui.components.InteractiveGeotagMap
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CivicPrimary
import com.example.ui.theme.CivicSecondary

@Composable
fun MapViewScreen(
    repository: CivicRepository,
    onTicketClick: (String) -> Unit,
    initialSelectedTicketId: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val complaints by repository.complaints.collectAsState()

    var selectedComplaintId by remember {
        mutableStateOf(initialSelectedTicketId ?: complaints.firstOrNull()?.id)
    }
    val selectedComplaint = complaints.find { it.id == selectedComplaintId }

    var isSatelliteMode by remember { mutableStateOf(false) }
    var showHeatmap by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Map Canvas
        InteractiveGeotagMap(
            isSatelliteMode = isSatelliteMode,
            modifier = Modifier.fillMaxSize()
        )

        // Top Status Floating Bar
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Live Stats Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.95f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00C875))
                        )
                        Text(
                            text = "Ward 4 Live Dispatch Grid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicPrimary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "4 Units Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF065F46)
                        )
                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "${complaints.size} Incidents",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicSecondary
                        )
                    }
                }
            }

            // Quick Pin Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                complaints.take(4).forEach { ticket ->
                    val isSelected = ticket.id == selectedComplaintId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (isSelected) CivicPrimary else Color.White.copy(alpha = 0.9f))
                            .shadow(2.dp, RoundedCornerShape(9999.dp))
                            .clickable { selectedComplaintId = ticket.id }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (ticket.status) {
                                            TicketStatus.IN_PROGRESS -> Color(0xFF2563EB)
                                            TicketStatus.PENDING -> Color(0xFFD97706)
                                            TicketStatus.RESOLVED -> Color(0xFF059669)
                                            else -> Color.Gray
                                        }
                                    )
                            )
                            Text(
                                text = ticket.id,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }

        // Floating Map Controls on Right Side
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Live Sensor / Heatmap Toggle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(if (showHeatmap) Color(0xFF003D28) else Color.White)
                    .clickable {
                        showHeatmap = !showHeatmap
                        Toast.makeText(context, if (showHeatmap) "Hazard Heatmap enabled" else "Heatmap hidden", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Sensors",
                    tint = if (showHeatmap) Color(0xFF85F8C4) else Color(0xFF334155),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Satellite Toggle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(if (isSatelliteMode) CivicPrimary else Color.White)
                    .clickable { isSatelliteMode = !isSatelliteMode },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Layers",
                    tint = if (isSatelliteMode) Color.White else CivicPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Recenter
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {
                        Toast.makeText(context, "Recentered to Ward 4 Metro Corridor", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Recenter",
                    tint = CivicPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Bottom Selected Ticket Callout Card
        AnimatedVisibility(
            visible = selectedComplaint != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(14.dp)
                .padding(bottom = 60.dp)
        ) {
            selectedComplaint?.let { ticket ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .testTag("map_selected_ticket_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = ticket.id,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicPrimary
                                )
                                StatusBadge(status = ticket.status)
                            }

                            IconButton(
                                onClick = { selectedComplaintId = null },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Info Row: Thumbnail + text
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 75.dp, height = 65.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                IncidentImageGraphic(
                                    type = ticket.imageType,
                                    modifier = Modifier.fillMaxSize(),
                                    showFixedBadge = ticket.status == TicketStatus.RESOLVED
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = ticket.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Text(
                                    text = ticket.locationAddress,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Assigned: ${ticket.assignedUnit}",
                                    fontSize = 11.sp,
                                    color = CivicPrimary,
                                    maxLines = 1
                                )
                            }
                        }

                        // Bottom Actions Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onTicketClick(ticket.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CivicPrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("open_docket_from_map_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Open Full Docket Details",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
