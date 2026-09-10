package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.IssueCategory
import com.example.ui.components.IncidentImageGraphic
import com.example.ui.theme.CivicPrimary
import com.example.ui.theme.CivicSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportStep1Screen(
    onBackClick: () -> Unit,
    onProceedToStep2: (category: IssueCategory, title: String, description: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(IssueCategory.ROADS_AND_POTHOLES) }
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("Deep Pothole & Damaged Asphalt") }
    var description by remember {
        mutableStateOf("Deep asphalt depression filled with muddy rainwater on the north-bound carriageway. Creates severe skidding danger for two-wheelers and scooters especially after dusk.")
    }

    var isRecordingAudio by remember { mutableStateOf(false) }
    var hasPhoto by remember { mutableStateOf(true) }

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
                                .testTag("step1_back_button")
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
                                text = "Step 1 of 2: Details & Evidence",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(Color(0xFFE2E7FF))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Step 1 / 2",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CivicPrimary
                        )
                    }
                }

                // Stepper Line
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = CivicPrimary,
                    trackColor = Color(0xFFE2E7FF)
                )
            }
        }

        // Form Fields Scrollable Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Category Dropdown
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Incident Category *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ExposedDropdownMenuBox(
                    expanded = isCategoryDropdownExpanded,
                    onExpandedChange = { isCategoryDropdownExpanded = !isCategoryDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory.title,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownExpanded) },
                        leadingIcon = {
                            Icon(
                                imageVector = selectedCategory.icon,
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
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .testTag("category_dropdown")
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryDropdownExpanded,
                        onDismissRequest = { isCategoryDropdownExpanded = false }
                    ) {
                        IssueCategory.entries.forEach { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCategory = category
                                        isCategoryDropdownExpanded = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    tint = if (category == selectedCategory) CivicPrimary else Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = category.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                                    color = if (category == selectedCategory) CivicPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Title Field
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Brief Title *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${title.length}/100",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { if (it.length <= 100) title = it },
                    placeholder = { Text("e.g., Deep Pothole & Damaged Asphalt") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CivicPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("title_input")
                )
            }

            // Description Field with Voice Mic Dictation
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detailed Description *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Voice Mic Dictation Trigger
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (isRecordingAudio) Color(0xFFFFDAD6) else Color(0xFFE2E7FF))
                            .clickable {
                                isRecordingAudio = !isRecordingAudio
                                if (isRecordingAudio) {
                                    Toast.makeText(context, "Listening... Speak your issue description.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isRecordingAudio) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Voice Dictate",
                            tint = if (isRecordingAudio) Color(0xFFBA1A1A) else CivicPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isRecordingAudio) "Listening..." else "Dictate",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRecordingAudio) Color(0xFFBA1A1A) else CivicPrimary
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Provide details: depth, location landmarks, severity, impact on traffic...") },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CivicPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("description_input")
                )

                // Quick tags chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Two-wheeler hazard", "Water logging", "Traffic slow", "Pillar 142").forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9999.dp))
                                .background(Color(0xFFF1F5F9))
                                .clickable {
                                    if (!description.contains(tag)) {
                                        description = "$description ($tag)"
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+ $tag",
                                fontSize = 11.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }

            // Evidence & Photo Upload Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Attach Photos & Evidence *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Current Attached Photo Preview
                    if (hasPhoto) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                        ) {
                            IncidentImageGraphic(
                                type = when (selectedCategory) {
                                    IssueCategory.ROADS_AND_POTHOLES -> "pothole"
                                    IssueCategory.STREETLIGHTS_POWER -> "streetlight"
                                    IssueCategory.GARBAGE_SANITATION -> "garbage"
                                    IssueCategory.WATER_LEAKAGE -> "water"
                                    IssueCategory.PARKS_AND_SIDEWALKS -> "parks"
                                },
                                modifier = Modifier.fillMaxSize()
                            )

                            // Geotag overlay pill
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color(0xFF0F172A).copy(alpha = 0.85f))
                                    .padding(horizontal = 4.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PinDrop,
                                        contentDescription = null,
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = "GPS Geotagged",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Delete button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .clickable { hasPhoto = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    // Add More Photos Button
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF94A3B8), RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .clickable {
                                hasPhoto = true
                                Toast.makeText(context, "Captured high-res incident evidence", Toast.LENGTH_SHORT).show()
                            }
                            .testTag("add_photo_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    tint = CivicPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "+ Add Photo",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CivicPrimary
                            )
                            Text(
                                text = "Max 3 (EXIF)",
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Cryptographic Telemetry Notice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0FDF4))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Device GPS coordinates, timestamp, and hardware EXIF telemetry are securely captured for automated municipal dispatch and verified SLA compliance.",
                        fontSize = 11.sp,
                        color = Color(0xFF166534),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Bottom Sticky Action Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .shadow(8.dp)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Please enter an issue title", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onProceedToStep2(selectedCategory, title, description)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CivicPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("proceed_to_step2_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Proceed to Verify Location (1/2)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
