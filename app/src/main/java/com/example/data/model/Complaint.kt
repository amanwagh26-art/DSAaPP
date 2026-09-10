package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddRoad
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

enum class IssueCategory(
    val title: String,
    val shortName: String,
    val icon: ImageVector,
    val defaultJurisdiction: String = "Ward 4 Public Works & Road Maintenance Wing"
) {
    ROADS_AND_POTHOLES(
        title = "Roads & Potholes",
        shortName = "Roads",
        icon = Icons.Default.AddRoad,
        defaultJurisdiction = "Ward 4 Public Works & Road Maintenance Wing"
    ),
    STREETLIGHTS_POWER(
        title = "Streetlights & Power",
        shortName = "Electrical",
        icon = Icons.Default.Lightbulb,
        defaultJurisdiction = "Ward 4 Electrical Grid & Illumination Cell"
    ),
    GARBAGE_SANITATION(
        title = "Garbage & Sanitation",
        shortName = "Sanitation",
        icon = Icons.Default.DeleteSweep,
        defaultJurisdiction = "Ward 4 Solid Waste & Sanitation Division"
    ),
    WATER_LEAKAGE(
        title = "Water & Leakage",
        shortName = "Water Board",
        icon = Icons.Default.WaterDrop,
        defaultJurisdiction = "Ward 4 Water Supply & Drainage Board"
    ),
    PARKS_AND_SIDEWALKS(
        title = "Public Parks, Sidewalks & Fallen Trees",
        shortName = "Horticulture",
        icon = Icons.Default.Park,
        defaultJurisdiction = "Ward 4 Horticulture & Public Spaces Directorate"
    )
}

enum class TicketStatus(val label: String) {
    PENDING("Pending Triage"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    REJECTED("Rejected")
}

enum class PriorityTier(val code: String, val label: String) {
    P1_HIGH_HAZARD("P1", "P1 - High Hazard"),
    P2_MODERATE_RISK("P2", "P2 - Moderate Risk"),
    P3_STANDARD_QUEUE("P3", "P3 - Standard Queue"),
    P4_ROUTINE_CYCLE("P4", "P4 - Routine Cycle")
}

data class InternalNote(
    val author: String,
    val timestamp: String,
    val content: String
)

data class AuditStage(
    val stageNumber: Int,
    val title: String,
    val timestamp: String,
    val subtitle: String,
    val officerNote: String? = null,
    val isCompleted: Boolean,
    val isActive: Boolean,
    val statusTag: String? = null,
    val latLng: String? = null
)

data class Complaint(
    val id: String,
    val category: IssueCategory,
    val title: String,
    val description: String,
    val status: TicketStatus,
    val priority: PriorityTier = PriorityTier.P2_MODERATE_RISK,
    val locationAddress: String,
    val landmark: String,
    val ward: String = "Ward 4, Sector 12",
    val latitude: Double = 18.5204,
    val longitude: Double = 73.8567,
    val submittedAt: String,
    val formattedDate: String = "Nov 15, 2024",
    val complainantName: String = "Maya Sen",
    val complainantVoterId: String = "#W4-VTR-99824",
    val complainantPhone: String = "+91 98230-XXXXX",
    val complainantEmail: String = "maya.sen@citizen.gov.in",
    val assignedUnit: String = "Ward 4 - Rapid Road Infrastructure Crew #3",
    val leadEngineer: String = "Eng. R. Deshmukh (Radio: Ch-04)",
    val slaRemainingHours: Int = 13,
    val slaRemainingMinutes: Int = 15,
    val slaPercentage: Int = 55,
    val currentStep: Int = 3,
    val totalSteps: Int = 4,
    val affectedNeighborsCount: Int = 14,
    val isUserAffected: Boolean = false,
    val proofRating: Float? = null,
    val closureId: String? = null,
    val aiConfidence: Float = 94.8f,
    val aiSeverityDescription: String = "Computer vision confidence 94.8%. High risk of two-wheeler skidding.",
    val imageType: String = "pothole", // "pothole", "streetlight", "garbage", "water"
    val internalNotes: List<InternalNote> = emptyList(),
    val auditStages: List<AuditStage> = emptyList()
)
