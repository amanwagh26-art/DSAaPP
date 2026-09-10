package com.example.data.repository

import com.example.data.model.AuditStage
import com.example.data.model.Complaint
import com.example.data.model.InternalNote
import com.example.data.model.IssueCategory
import com.example.data.model.PriorityTier
import com.example.data.model.TicketStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CivicRepository {

    private val _complaints = MutableStateFlow<List<Complaint>>(initialComplaints())
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    fun toggleAdminMode() {
        _isAdminMode.update { !it }
    }

    fun setAdminMode(enabled: Boolean) {
        _isAdminMode.value = enabled
    }

    fun getComplaintById(id: String): Complaint? {
        return _complaints.value.find { it.id == id }
    }

    fun toggleAffectedNeighbor(complaintId: String) {
        _complaints.update { list ->
            list.map { item ->
                if (item.id == complaintId) {
                    val newIsAffected = !item.isUserAffected
                    val newCount = if (newIsAffected) item.affectedNeighborsCount + 1 else item.affectedNeighborsCount - 1
                    item.copy(
                        isUserAffected = newIsAffected,
                        affectedNeighborsCount = newCount
                    )
                } else item
            }
        }
    }

    fun submitNewComplaint(
        category: IssueCategory,
        title: String,
        description: String,
        address: String,
        landmark: String,
        latitude: Double = 18.5204,
        longitude: Double = 73.8567
    ): Complaint {
        val nextNumber = 89421 + (_complaints.value.size)
        val newId = "#GOV-$nextNumber"
        val newComplaint = Complaint(
            id = newId,
            category = category,
            title = title,
            description = description,
            status = TicketStatus.PENDING,
            priority = PriorityTier.P2_MODERATE_RISK,
            locationAddress = address,
            landmark = landmark,
            ward = "Ward 4, Sector 12",
            latitude = latitude,
            longitude = longitude,
            submittedAt = "Just now",
            formattedDate = "Today",
            complainantName = "Maya Sen",
            complainantVoterId = "#W4-VTR-99824",
            complainantPhone = "+91 98230-XXXXX",
            assignedUnit = category.defaultJurisdiction,
            leadEngineer = "Duty Officer (Standby)",
            slaRemainingHours = 24,
            slaRemainingMinutes = 0,
            slaPercentage = 100,
            currentStep = 1,
            totalSteps = 4,
            affectedNeighborsCount = 1,
            isUserAffected = true,
            imageType = when (category) {
                IssueCategory.ROADS_AND_POTHOLES -> "pothole"
                IssueCategory.STREETLIGHTS_POWER -> "streetlight"
                IssueCategory.GARBAGE_SANITATION -> "garbage"
                IssueCategory.WATER_LEAKAGE -> "water"
                IssueCategory.PARKS_AND_SIDEWALKS -> "parks"
            },
            auditStages = listOf(
                AuditStage(
                    stageNumber = 1,
                    title = "Stage 1: Submitted",
                    timestamp = "Just now",
                    subtitle = "Geotagged photo verified",
                    isCompleted = true,
                    isActive = false,
                    statusTag = "Verified",
                    latLng = "Lat %.4f, Lng %.4f".format(latitude, longitude)
                ),
                AuditStage(
                    stageNumber = 2,
                    title = "Stage 2: Under Review",
                    timestamp = "Pending triage queue",
                    subtitle = "Automated geographic routing to ${category.shortName} team",
                    isCompleted = false,
                    isActive = true,
                    statusTag = "In Queue"
                ),
                AuditStage(
                    stageNumber = 3,
                    title = "Stage 3: In Progress",
                    timestamp = "Scheduled",
                    subtitle = "Field team assignment & dispatch",
                    isCompleted = false,
                    isActive = false
                ),
                AuditStage(
                    stageNumber = 4,
                    title = "Stage 4: Resolved",
                    timestamp = "Pending closure",
                    subtitle = "Before & after photo verification and municipal sign-off",
                    isCompleted = false,
                    isActive = false
                )
            ),
            internalNotes = listOf(
                InternalNote(
                    author = "Automated Geo-Triage",
                    timestamp = "Just now",
                    content = "Ticket routed to ${category.defaultJurisdiction} based on coordinates."
                )
            )
        )

        _complaints.update { listOf(newComplaint) + it }
        return newComplaint
    }

    fun updateTicket(
        ticketId: String,
        newStatus: TicketStatus,
        newPriority: PriorityTier,
        assignedCrew: String,
        newNote: String?
    ) {
        _complaints.update { list ->
            list.map { ticket ->
                if (ticket.id == ticketId) {
                    val updatedNotes = if (!newNote.isNullOrBlank()) {
                        ticket.internalNotes + InternalNote(
                            author = "Officer R. Sharma (Chief Engineer)",
                            timestamp = "Just now",
                            content = newNote
                        )
                    } else ticket.internalNotes

                    val step = when (newStatus) {
                        TicketStatus.PENDING -> 1
                        TicketStatus.IN_PROGRESS -> 3
                        TicketStatus.RESOLVED -> 4
                        TicketStatus.REJECTED -> 1
                    }

                    ticket.copy(
                        status = newStatus,
                        priority = newPriority,
                        assignedUnit = assignedCrew,
                        currentStep = step,
                        internalNotes = updatedNotes
                    )
                } else ticket
            }
        }
    }

    companion object {
        val instance = CivicRepository()

        private fun initialComplaints(): List<Complaint> = listOf(
            Complaint(
                id = "#GOV-89421",
                category = IssueCategory.ROADS_AND_POTHOLES,
                title = "Deep Pothole & Damaged Asphalt",
                description = "Deep depression filled with muddy rainwater creating hazardous transit conditions for two-wheelers and buses. Water pooling prevents drivers from seeing depth (~28cm). Located directly in the north-bound central transit corridor.",
                status = TicketStatus.IN_PROGRESS,
                priority = PriorityTier.P1_HIGH_HAZARD,
                locationAddress = "MG Road, Pillar 142, Sector 4",
                landmark = "Near Metro Pillar 142 • Landmark: City Mart Junction",
                ward = "Ward 4, Sector 12",
                latitude = 18.5204,
                longitude = 73.8567,
                submittedAt = "Submitted Today, 10:45 AM",
                formattedDate = "Nov 15, 2024",
                complainantName = "Maya Sen",
                complainantVoterId = "#W4-VTR-99824",
                complainantPhone = "+91 98230-XXXXX",
                complainantEmail = "maya.sen@citizen.gov.in",
                assignedUnit = "Ward 4 - Rapid Road Infrastructure Crew #3",
                leadEngineer = "Eng. R. Deshmukh (Radio: Ch-04 • Mobile Unit 89)",
                slaRemainingHours = 13,
                slaRemainingMinutes = 15,
                slaPercentage = 55,
                currentStep = 3,
                totalSteps = 4,
                affectedNeighborsCount = 14,
                isUserAffected = false,
                aiConfidence = 94.8f,
                aiSeverityDescription = "Computer vision confidence 94.8%. High risk of two-wheeler skidding.",
                imageType = "pothole",
                auditStages = listOf(
                    AuditStage(
                        stageNumber = 1,
                        title = "Stage 1: Submitted",
                        timestamp = "Nov 15, 10:45 AM",
                        subtitle = "Geotagged photo verified",
                        isCompleted = true,
                        isActive = false,
                        statusTag = "Verified",
                        latLng = "Lat 18.5204, Lng 73.8567"
                    ),
                    AuditStage(
                        stageNumber = 2,
                        title = "Stage 2: Under Review",
                        timestamp = "Nov 15, 11:00 AM",
                        subtitle = "Officer R. Sharma reviewing dispatch priority",
                        officerNote = "Pothole severity classified as Priority 2 (Standard Flow Hazard)",
                        isCompleted = true,
                        isActive = false,
                        statusTag = "In Queue"
                    ),
                    AuditStage(
                        stageNumber = 3,
                        title = "Stage 3: In Progress",
                        timestamp = "Nov 15, 01:30 PM",
                        subtitle = "Scheduled road patch crew assignment & hot-mix batching",
                        isCompleted = false,
                        isActive = true,
                        statusTag = "Active Crew"
                    ),
                    AuditStage(
                        stageNumber = 4,
                        title = "Stage 4: Resolved",
                        timestamp = "Pending Final Sign-off",
                        subtitle = "Before & after photo verification and municipal sign-off",
                        isCompleted = false,
                        isActive = false
                    )
                ),
                internalNotes = listOf(
                    InternalNote(
                        author = "Dispatcher Sharma (Ops Control)",
                        timestamp = "11:15 AM",
                        content = "Field crew #3 notified. Rapid batch asphalt truck scheduled for 2:00 PM patch sealing."
                    )
                )
            ),
            Complaint(
                id = "#GOV-89210",
                category = IssueCategory.STREETLIGHTS_POWER,
                title = "Faulty Streetlight near Gate 3",
                description = "Malfunctioning sodium vapor lamp flickering repeatedly near residential perimeter Gate 3 on Sector 4 School Road. Creates a blind spot for pedestrians during evening commute.",
                status = TicketStatus.PENDING,
                priority = PriorityTier.P2_MODERATE_RISK,
                locationAddress = "5th Cross, Sector 4 School Road",
                landmark = "Opposite Gate 3 Playground",
                ward = "Ward 4, Sector 4",
                latitude = 18.5245,
                longitude = 73.8592,
                submittedAt = "Submitted Yesterday, 06:15 PM",
                formattedDate = "Nov 14, 2024",
                complainantName = "Arjun Patel",
                complainantVoterId = "#W4-VTR-48192",
                complainantPhone = "+91 97321-XXXXX",
                assignedUnit = "Awaiting Ward Supervisor verification",
                leadEngineer = "Unassigned",
                slaRemainingHours = 8,
                slaRemainingMinutes = 45,
                slaPercentage = 35,
                currentStep = 1,
                totalSteps = 4,
                affectedNeighborsCount = 8,
                imageType = "streetlight",
                auditStages = listOf(
                    AuditStage(
                        stageNumber = 1,
                        title = "Stage 1: Submitted",
                        timestamp = "Nov 14, 06:15 PM",
                        subtitle = "Citizen photo telemetry verified",
                        isCompleted = true,
                        isActive = false,
                        statusTag = "Verified"
                    ),
                    AuditStage(
                        stageNumber = 2,
                        title = "Stage 2: Under Review",
                        timestamp = "Awaiting Inspection",
                        subtitle = "Pending supervisor queue allocation",
                        isCompleted = false,
                        isActive = true,
                        statusTag = "Pending Triage"
                    ),
                    AuditStage(
                        stageNumber = 3,
                        title = "Stage 3: In Progress",
                        timestamp = "Scheduled",
                        subtitle = "Electric maintenance boom-lift unit dispatch",
                        isCompleted = false,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 4,
                        title = "Stage 4: Resolved",
                        timestamp = "Pending",
                        subtitle = "Luminance sensor validation",
                        isCompleted = false,
                        isActive = false
                    )
                ),
                internalNotes = listOf(
                    InternalNote(
                        author = "Desk Assistant (Ward 4)",
                        timestamp = "09:00 AM",
                        content = "Merged with duplicate ticket #GOV-89190 reported by school security."
                    )
                )
            ),
            Complaint(
                id = "#GOV-88734",
                category = IssueCategory.GARBAGE_SANITATION,
                title = "Overflowing Community Dustbin",
                description = "Bulk domestic waste and plastic packaging piling up around community park collection bins causing odor and stray animal gathering.",
                status = TicketStatus.RESOLVED,
                priority = PriorityTier.P3_STANDARD_QUEUE,
                locationAddress = "Sector 9 Community Park Gate",
                landmark = "Park North Gate Exit",
                ward = "Ward 4, Sector 9",
                latitude = 18.5175,
                longitude = 73.8520,
                submittedAt = "Resolved Nov 13, 03:30 PM",
                formattedDate = "Nov 13, 2024",
                complainantName = "Maya Sen",
                complainantVoterId = "#W4-VTR-99824",
                complainantPhone = "+91 98230-XXXXX",
                assignedUnit = "Ward Crew 4 (Sanitation)",
                leadEngineer = "Supervisor P. Nair",
                slaRemainingHours = 0,
                slaRemainingMinutes = 0,
                slaPercentage = 0,
                currentStep = 4,
                totalSteps = 4,
                affectedNeighborsCount = 22,
                proofRating = 5.0f,
                closureId = "CLS-4019",
                imageType = "garbage",
                auditStages = listOf(
                    AuditStage(
                        stageNumber = 1,
                        title = "Stage 1: Submitted",
                        timestamp = "Nov 13, 09:10 AM",
                        subtitle = "Reported by resident",
                        isCompleted = true,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 2,
                        title = "Stage 2: Under Review",
                        timestamp = "Nov 13, 10:00 AM",
                        subtitle = "Assigned to Morning Waste Route",
                        isCompleted = true,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 3,
                        title = "Stage 3: In Progress",
                        timestamp = "Nov 13, 01:15 PM",
                        subtitle = "Compactor truck dispatched",
                        isCompleted = true,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 4,
                        title = "Stage 4: Resolved",
                        timestamp = "Nov 13, 03:30 PM",
                        subtitle = "Bins sanitized, emptied, and verified clean",
                        isCompleted = true,
                        isActive = false,
                        statusTag = "Clean & Verified"
                    )
                )
            ),
            Complaint(
                id = "#GOV-87912",
                category = IssueCategory.WATER_LEAKAGE,
                title = "Water Main Valve Leakage",
                description = "High-pressure valve pinhole leak causing potable water to pool across pedestrian sidewalk.",
                status = TicketStatus.RESOLVED,
                priority = PriorityTier.P1_HIGH_HAZARD,
                locationAddress = "Main Boulevard, Pipeline Junction 3",
                landmark = "Near Water Board Substation 4",
                ward = "Ward 4, Sector 3",
                latitude = 18.5280,
                longitude = 73.8610,
                submittedAt = "Resolved Nov 10",
                formattedDate = "Nov 10, 2024",
                complainantName = "Rahul K.",
                complainantVoterId = "#W4-VTR-11029",
                complainantPhone = "+91 99882-XXXXX",
                assignedUnit = "Rapid Valve Maintenance Team B",
                leadEngineer = "Eng. V. Joshi",
                slaRemainingHours = 0,
                slaRemainingMinutes = 0,
                slaPercentage = 0,
                currentStep = 4,
                totalSteps = 4,
                affectedNeighborsCount = 45,
                closureId = "CLS-3891",
                imageType = "water",
                auditStages = listOf(
                    AuditStage(
                        stageNumber = 1,
                        title = "Stage 1: Submitted",
                        timestamp = "Nov 10, 07:30 AM",
                        subtitle = "Sensor and resident alert",
                        isCompleted = true,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 2,
                        title = "Stage 2: Under Review",
                        timestamp = "Nov 10, 08:00 AM",
                        subtitle = "Pressure regulation triage",
                        isCompleted = true,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 3,
                        title = "Stage 3: In Progress",
                        timestamp = "Nov 10, 11:00 AM",
                        subtitle = "Gasket replacement & pressure test",
                        isCompleted = true,
                        isActive = false
                    ),
                    AuditStage(
                        stageNumber = 4,
                        title = "Stage 4: Resolved",
                        timestamp = "Nov 10, 02:45 PM",
                        subtitle = "Closure certificate signed",
                        isCompleted = true,
                        isActive = false,
                        statusTag = "Closed"
                    )
                )
            ),
            Complaint(
                id = "#GOV-89419",
                category = IssueCategory.STREETLIGHTS_POWER,
                title = "Flickering Streetlight Cluster",
                description = "Series of 4 LED streetlamps repeatedly tripping circuit. Low illumination along pedestrian walkway.",
                status = TicketStatus.IN_PROGRESS,
                priority = PriorityTier.P2_MODERATE_RISK,
                locationAddress = "8th Cross, Ring Road",
                landmark = "Near Ring Road Flyover",
                ward = "Ward 4, Sector 8",
                latitude = 18.5260,
                longitude = 73.8540,
                submittedAt = "1h ago",
                formattedDate = "Today",
                complainantName = "Arjun Patel",
                complainantVoterId = "#W4-VTR-48192",
                assignedUnit = "Electric Crew #2 (En Route)",
                leadEngineer = "Eng. S. Bannerjee",
                slaRemainingHours = 19,
                slaRemainingMinutes = 30,
                slaPercentage = 80,
                currentStep = 3,
                totalSteps = 4,
                affectedNeighborsCount = 12,
                imageType = "streetlight"
            ),
            Complaint(
                id = "#GOV-89408",
                category = IssueCategory.WATER_LEAKAGE,
                title = "Sewage Overflow near Drain B",
                description = "Underground mainline backup leaking contaminated stormwater onto public crosswalk. Urgent vacuum truck required.",
                status = TicketStatus.PENDING,
                priority = PriorityTier.P1_HIGH_HAZARD,
                locationAddress = "Market Sq., Sector 3",
                landmark = "Behind Central Vegetable Market",
                ward = "Ward 4, Sector 3",
                latitude = 18.5210,
                longitude = 73.8510,
                submittedAt = "2h ago",
                formattedDate = "Today",
                complainantName = "Priya Nair",
                complainantVoterId = "#W4-VTR-77291",
                assignedUnit = "Hydro & Drainage Emergency Unit",
                leadEngineer = "Unassigned",
                slaRemainingHours = 4,
                slaRemainingMinutes = 15,
                slaPercentage = 20,
                currentStep = 1,
                totalSteps = 4,
                affectedNeighborsCount = 38,
                imageType = "water"
            ),
            Complaint(
                id = "#GOV-89390",
                category = IssueCategory.GARBAGE_SANITATION,
                title = "Illegal Waste Dump on Sidewalk",
                description = "Construction rubble and bulk discarded packaging left near community park fence.",
                status = TicketStatus.PENDING,
                priority = PriorityTier.P4_ROUTINE_CYCLE,
                locationAddress = "Greenwood Ave",
                landmark = "Corner of 12th Lane",
                ward = "Ward 4, Sector 12",
                latitude = 18.5190,
                longitude = 73.8640,
                submittedAt = "3h ago",
                formattedDate = "Today",
                complainantName = "Rahul K.",
                complainantVoterId = "#W4-VTR-11029",
                assignedUnit = "Bulk Waste Clearance Squad #2",
                leadEngineer = "Unassigned",
                slaRemainingHours = 21,
                slaRemainingMinutes = 0,
                slaPercentage = 90,
                currentStep = 1,
                totalSteps = 4,
                affectedNeighborsCount = 6,
                imageType = "garbage"
            )
        )
    }
}
