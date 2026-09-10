package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.IssueCategory
import com.example.data.repository.CivicRepository
import com.example.ui.components.CivicBottomNavigation
import com.example.ui.components.CivicHeader
import com.example.ui.screens.ComplaintsScreen
import com.example.ui.screens.GovAdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MapViewScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReportStep1Screen
import com.example.ui.screens.ReportStep2Screen
import com.example.ui.screens.TicketDetailsScreen
import com.example.ui.theme.CivicPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CivicPulseTheme {
                CivicPulseApp()
            }
        }
    }
}

@Composable
fun CivicPulseApp() {
    val navController = rememberNavController()
    val repository = remember { CivicRepository.instance }
    val isAdminMode by repository.isAdminMode.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val isRootTab = currentRoute in listOf("home", "map", "complaints", "profile")

    // State passed between Step 1 and Step 2 of report
    var pendingCategory by remember { mutableStateOf(IssueCategory.ROADS_AND_POTHOLES) }
    var pendingTitle by remember { mutableStateOf("") }
    var pendingDescription by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Show custom header on root tabs or admin screen
            if (isRootTab || currentRoute == "gov_admin") {
                CivicHeader(
                    wardName = "Ward 4",
                    isAdminMode = isAdminMode,
                    onAdminToggle = {
                        repository.toggleAdminMode()
                        if (!isAdminMode) {
                            navController.navigate("gov_admin")
                        } else {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                    },
                    onProfileClick = {
                        navController.navigate("profile") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isRootTab) {
                CivicBottomNavigation(
                    currentTab = currentRoute,
                    onTabSelected = { selectedTab ->
                        navController.navigate(selectedTab) {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("home") {
                    HomeScreen(
                        repository = repository,
                        onStartReportClick = {
                            navController.navigate("report_step1")
                        },
                        onSeeMapClick = {
                            navController.navigate("map") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onTicketClick = { ticketId ->
                            navController.navigate("ticket_details/$ticketId")
                        }
                    )
                }

                composable("map") {
                    MapViewScreen(
                        repository = repository,
                        onTicketClick = { ticketId ->
                            navController.navigate("ticket_details/$ticketId")
                        }
                    )
                }

                composable("complaints") {
                    ComplaintsScreen(
                        repository = repository,
                        onComplaintClick = { ticketId ->
                            navController.navigate("ticket_details/$ticketId")
                        }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        repository = repository,
                        onAdminConsoleClick = {
                            navController.navigate("gov_admin")
                        }
                    )
                }

                composable("report_step1") {
                    ReportStep1Screen(
                        onBackClick = { navController.popBackStack() },
                        onProceedToStep2 = { cat, title, desc ->
                            pendingCategory = cat
                            pendingTitle = title
                            pendingDescription = desc
                            navController.navigate("report_step2")
                        }
                    )
                }

                composable("report_step2") {
                    ReportStep2Screen(
                        category = pendingCategory,
                        title = pendingTitle,
                        description = pendingDescription,
                        repository = repository,
                        onBackClick = { navController.popBackStack() },
                        onSubmitSuccess = { createdId ->
                            navController.navigate("ticket_details/$createdId") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                    )
                }

                composable(
                    route = "ticket_details/{ticketId}",
                    arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
                    TicketDetailsScreen(
                        complaintId = ticketId,
                        repository = repository,
                        onBackClick = { navController.popBackStack() },
                        onViewOnMapClick = { id ->
                            navController.navigate("map") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onBackToHomeClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                    )
                }

                composable("gov_admin") {
                    GovAdminScreen(
                        repository = repository,
                        onBackToCitizenMode = {
                            repository.setAdminMode(false)
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = false }
                            }
                        },
                        onOpenTicket = { ticketId ->
                            navController.navigate("ticket_details/$ticketId")
                        }
                    )
                }
            }
        }
    }
}
