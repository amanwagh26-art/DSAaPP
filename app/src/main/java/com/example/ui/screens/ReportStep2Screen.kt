package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.model.IssueCategory
import com.example.data.repository.CivicRepository
import com.example.ui.components.InteractiveGeotagMap
import com.example.ui.theme.CivicPrimary
import com.example.ui.theme.CivicSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReportStep2Screen(
    category: IssueCategory,
    title: String,
    description: String,
    repository: CivicRepository,
    onBackClick: () -> Unit,
    onSubmitSuccess: (complaintId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isSubmitting by remember { mutableStateOf(false) }
    var isSatelliteMode by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(1f) }

    var address by remember { mutableStateOf("MG Road, Pillar 142, Sector 4") }
    var landmark by remember { mutableStateOf("Near Metro Pillar 142 • Landmark: City Mart Junction") }
    var isEditingAddress by remember { mutableStateOf(false) }

    var tempAddress by remember { mutableStateOf(address) }
    var tempLandmark by remember { mutableStateOf(landmark) }

    if (isEditingAddress) {
        AlertDialog(
            onDismissRequest = { isEditingAddress = false },
            title = { Text("Edit Address & Landmark") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = tempAddress,
                        onValueChange = { tempAddress = it },
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempLandmark,
                        onValueChange = { tempLandmark = it },
                        label = { Text("Landmark & Directions") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        address = tempAddress
                        landmark = tempLandmark
                        isEditingAddress = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditingAddress = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Step Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .shadow(1.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("step2_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column {
                            Text(
                                text = "Report Incident",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Step 2 of 2: Verify Geotag",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color(0xFF85F8C4))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Step 2 / 2",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF003D28)
                        )
                    }
                }

                // Stepper Line (100% full)
                LinearProgressIndicator(
                    progress = { 1.0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = CivicPrimary,
                    trackColor = Color(0xFFE2E7FF)
                )
            }
        }

        // Map Section (Interactive Geospatial Viewport)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            InteractiveGeotagMap(
                isSatelliteMode = isSatelliteMode,
                modifier = Modifier.fillMaxSize()
            )

            // Top Floating GPS Precision Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
                    .shadow(4.dp, RoundedCornerShape(9999.dp))
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
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
                        text = "GPS Accuracy: ± 2m (High precision)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            // Bottom Floating Hint Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .shadow(4.dp, RoundedCornerShape(9999.dp))
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.85f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Drag map or pin to fine-tune placement",
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }

            // Floating Controls on Right Side
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Recenter
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            Toast.makeText(context, "Location recentered to current GPS fix", Toast.LENGTH_SHORT).show()
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

                // Satellite / Vector toggle
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

                // Zoom in
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { zoomLevel += 0.2f },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom In",
                        tint = Color(0xFF334155),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Zoom out
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { zoomLevel = (zoomLevel - 0.2f).coerceAtLeast(0.6f) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zoom Out",
                        tint = Color(0xFF334155),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Location Confirmation Details Module (Bottom half)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .shadow(6.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Address Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "DETECTED ADDRESS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.05.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = address,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = landmark,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                TextButton(
                    onClick = {
                        tempAddress = address
                        tempLandmark = landmark
                        isEditingAddress = true
                    },
                    modifier = Modifier.testTag("edit_address_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = CivicSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Edit",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicSecondary
                        )
                    }
                }
            }

            // Ward & SLA Info Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF2F3FF))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDBE1FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = CivicPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.defaultJurisdiction,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CivicPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "24-hr initial response SLA applies",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }

            // Submit Complaint Action Button
            Button(
                onClick = {
                    if (isSubmitting) return@Button
                    isSubmitting = true
                    coroutineScope.launch {
                        delay(1200) // Simulated geo-hashing & dispatch
                        val created = repository.submitNewComplaint(
                            category = category,
                            title = title,
                            description = description,
                            address = address,
                            landmark = landmark
                        )
                        isSubmitting = false
                        onSubmitSuccess(created.id)
                    }
                },
                enabled = !isSubmitting,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CivicPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_complaint_button")
            ) {
                if (isSubmitting) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Dispatching to Ward 4 Queue...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Submit Complaint",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
