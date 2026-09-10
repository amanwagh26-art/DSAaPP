package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriorityTier
import com.example.data.model.TicketStatus
import com.example.data.repository.CivicRepository
import com.example.ui.components.IncidentImageGraphic
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CivicPrimary
import com.example.ui.theme.CivicSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GovAdminScreen(
    repository: CivicRepository,
    onBackToCitizenMode: () -> Unit,
    onOpenTicket: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val complaints by repository.complaints.collectAsState()

    var selectedTicketId by remember {
        mutableStateOf(complaints.firstOrNull { it.status == TicketStatus.IN_PROGRESS }?.id ?: complaints.firstOrNull()?.id ?: "")
    }
    val activeTicket = complaints.find { it.id == selectedTicketId } ?: complaints.firstOrNull()

    var selectedPriority by remember(activeTicket) {
        mutableStateOf(activeTicket?.priority ?: PriorityTier.P1_HIGH_HAZARD)
    }

    val crewOptions = listOf(
        "Ward 4 - Rapid Road Infrastructure Crew #3",
        "Ward 4 - Electrical Grid & Illumination Cell",
        "Ward 4 - Solid Waste & Sanitation Division",
        "Ward 4 - Water Supply & Drainage Board",
        "Ward 4 - Emergency Response Flying Squad"
    )
    var selectedCrew by remember(activeTicket) {
        mutableStateOf(activeTicket?.assignedUnit ?: crewOptions[0])
    }
    var isCrewMenuExpanded by remember { mutableStateOf(false) }

    var officialNote by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // GovAdmin Header Banner (Image 7 style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                    )
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00C875)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF0B1C30),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "MUNICIPAL DISPATCH CONSOLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF85F8C4),
                                letterSpacing = 0.05.sp
                            )
                            Text(
                                text = "Ward 4 Operations Desk",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onBackToCitizenMode,
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Exit to Citizen Mode", fontSize = 11.sp)
                    }
                }

                // Ops Key Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF334155))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Total Cases", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            Text("1,248", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF334155))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Pending Triage", fontSize = 10.sp, color = Color(0xFFFBBF24))
                            Text("${complaints.count { it.status == TicketStatus.PENDING }} Active", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF334155))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("SLA Adherence", fontSize = 10.sp, color = Color(0xFF34D399))
                            Text("96.8%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Triage Queue Ticket Selector Row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "INCOMING INCIDENT QUEUE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.05.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                complaints.take(3).forEach { item ->
                    val isSelected = item.id == selectedTicketId
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFE2E7FF) else Color(0xFFF1F5F9))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) CivicPrimary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedTicketId = item.id }
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = item.id,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CivicPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.category.shortName,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Active Ticket Triage Card (Image 7)
        activeTicket?.let { ticket ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .testTag("admin_triage_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header with ID and AI Confidence
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = ticket.id,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CivicPrimary
                                )
                                StatusBadge(status = ticket.status)
                            }
                            Text(
                                text = "${ticket.submittedAt} • by ${ticket.complainantName}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // AI Confidence badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(Color(0xFF85F8C4).copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFF003D28),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "AI ${ticket.aiConfidence}%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF003D28)
                                )
                            }
                        }
                    }

                    // Thumbnail + Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 90.dp, height = 80.dp)
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
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = ticket.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = ticket.locationAddress,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = ticket.aiSeverityDescription,
                                fontSize = 11.sp,
                                color = Color(0xFF059669),
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Triage Priority Level Selector (P1 to P4)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "ASSESS DISPATCH PRIORITY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PriorityTier.entries.forEach { tier ->
                                val isSelected = tier == selectedPriority
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) {
                                                when (tier) {
                                                    PriorityTier.P1_HIGH_HAZARD -> Color(0xFFDC2626)
                                                    PriorityTier.P2_MODERATE_RISK -> Color(0xFFD97706)
                                                    else -> CivicPrimary
                                                }
                                            } else Color(0xFFF1F5F9)
                                        )
                                        .clickable { selectedPriority = tier }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tier.code,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }

                    // Assigned Crew Dropdown Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "ASSIGN MUNICIPAL SQUAD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        ExposedDropdownMenuBox(
                            expanded = isCrewMenuExpanded,
                            onExpandedChange = { isCrewMenuExpanded = !isCrewMenuExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCrew,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCrewMenuExpanded) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Engineering,
                                        contentDescription = null,
                                        tint = CivicPrimary
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CivicPrimary,
                                    unfocusedBorderColor = Color(0xFFCBD5E1),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )

                            ExposedDropdownMenu(
                                expanded = isCrewMenuExpanded,
                                onDismissRequest = { isCrewMenuExpanded = false }
                            ) {
                                crewOptions.forEach { crew ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedCrew = crew
                                                isCrewMenuExpanded = false
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Text(crew, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Officer Dispatch Note
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "OFFICIAL DISPATCH INSTRUCTION NOTE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = officialNote,
                            onValueChange = { officialNote = it },
                            placeholder = { Text("e.g., Road crew dispatched with cold-mix asphalt batch...") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CivicPrimary,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Dispatch Action Buttons
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                repository.updateTicket(
                                    ticketId = ticket.id,
                                    newStatus = TicketStatus.IN_PROGRESS,
                                    newPriority = selectedPriority,
                                    assignedCrew = selectedCrew,
                                    newNote = officialNote.ifBlank { "Squad dispatched via Operations Console." }
                                )
                                Toast.makeText(context, "Dispatched ${selectedCrew} to ${ticket.id}", Toast.LENGTH_SHORT).show()
                                onOpenTicket(ticket.id)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CivicPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("dispatch_crew_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Dispatch Squad Immediately", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    repository.updateTicket(
                                        ticketId = ticket.id,
                                        newStatus = TicketStatus.RESOLVED,
                                        newPriority = selectedPriority,
                                        assignedCrew = selectedCrew,
                                        newNote = "Resolution verified by Municipal Supervisor."
                                    )
                                    Toast.makeText(context, "Marked ${ticket.id} as Resolved", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF003D28),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Mark Resolved", fontSize = 12.sp, color = Color(0xFF003D28))
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    repository.updateTicket(
                                        ticketId = ticket.id,
                                        newStatus = TicketStatus.REJECTED,
                                        newPriority = selectedPriority,
                                        assignedCrew = selectedCrew,
                                        newNote = "Merged as duplicate."
                                    )
                                    Toast.makeText(context, "Closed as duplicate", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = Color(0xFFBA1A1A),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Reject / Dupl", fontSize = 12.sp, color = Color(0xFFBA1A1A))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
