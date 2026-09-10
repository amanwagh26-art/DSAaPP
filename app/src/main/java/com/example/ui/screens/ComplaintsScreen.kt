package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.data.model.TicketStatus
import com.example.data.repository.CivicRepository
import com.example.ui.components.IncidentImageGraphic
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CivicPrimary
import com.example.ui.theme.CivicSecondary

@Composable
fun ComplaintsScreen(
    repository: CivicRepository,
    onComplaintClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val complaints by repository.complaints.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = complaints.filter { complaint ->
        val matchesSearch = searchQuery.isBlank() ||
                complaint.id.contains(searchQuery, ignoreCase = true) ||
                complaint.title.contains(searchQuery, ignoreCase = true) ||
                complaint.locationAddress.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "PENDING" -> complaint.status == TicketStatus.PENDING
            "IN_PROGRESS" -> complaint.status == TicketStatus.IN_PROGRESS
            "RESOLVED" -> complaint.status == TicketStatus.RESOLVED
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Search Bar Module
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by ID, location, or keyword...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CivicPrimary,
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("complaints_search_input")
            )
        }

        // Status Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipItem(
                label = "All (${complaints.size})",
                isSelected = selectedFilter == "ALL",
                onClick = { selectedFilter = "ALL" }
            )
            FilterChipItem(
                label = "Pending Triage (${complaints.count { it.status == TicketStatus.PENDING }})",
                isSelected = selectedFilter == "PENDING",
                onClick = { selectedFilter = "PENDING" }
            )
            FilterChipItem(
                label = "In Progress (${complaints.count { it.status == TicketStatus.IN_PROGRESS }})",
                isSelected = selectedFilter == "IN_PROGRESS",
                onClick = { selectedFilter = "IN_PROGRESS" }
            )
            FilterChipItem(
                label = "Resolved (${complaints.count { it.status == TicketStatus.RESOLVED }})",
                isSelected = selectedFilter == "RESOLVED",
                onClick = { selectedFilter = "RESOLVED" }
            )
        }

        // Complaint Cards List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "No municipal tickets match your search",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                items(filteredList, key = { it.id }) { complaint ->
                    ComplaintListItemCard(
                        complaint = complaint,
                        onCardClick = { onComplaintClick(complaint.id) },
                        onUpvoteClick = { repository.toggleAffectedNeighbor(complaint.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(if (isSelected) CivicPrimary else Color(0xFFEFF3F8))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("filter_chip_$label")
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF475569)
        )
    }
}

@Composable
fun ComplaintListItemCard(
    complaint: Complaint,
    onCardClick: () -> Unit,
    onUpvoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(14.dp))
            .clickable(onClick = onCardClick)
            .testTag("complaint_item_${complaint.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Reference ID + Status Badge + Priority
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
                        text = complaint.id,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicPrimary
                    )
                    Text(
                        text = "• ${complaint.formattedDate}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge(status = complaint.status)
            }

            // Body: Thumbnail + Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 75.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    IncidentImageGraphic(
                        type = complaint.imageType,
                        modifier = Modifier.fillMaxSize(),
                        showFixedBadge = complaint.status == TicketStatus.RESOLVED
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = complaint.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CivicSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = complaint.locationAddress,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    Text(
                        text = complaint.assignedUnit,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            // Progress bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Workflow Stage",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Stage ${complaint.currentStep} of 4",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicPrimary
                    )
                }

                LinearProgressIndicator(
                    progress = { complaint.currentStep / 4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = when (complaint.status) {
                        TicketStatus.RESOLVED -> Color(0xFF003D28)
                        TicketStatus.IN_PROGRESS -> CivicSecondary
                        else -> Color(0xFFD97706)
                    },
                    trackColor = Color(0xFFE2E7FF)
                )
            }

            // Bottom Actions Row: Neighbors affected + View Docket
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${complaint.affectedNeighborsCount} affected",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (complaint.isUserAffected) "• Voted" else "• +1 Me",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (complaint.isUserAffected) Color(0xFF059669) else CivicSecondary,
                        modifier = Modifier
                            .clickable { onUpvoteClick() }
                            .padding(horizontal = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Open Docket",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CivicPrimary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = CivicPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
