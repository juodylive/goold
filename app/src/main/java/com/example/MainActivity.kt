package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.AppStrings
import com.example.ui.components.TopDetectorAppBar
import com.example.ui.screens.CalibrationScreen
import com.example.ui.screens.DetectScreen
import com.example.ui.screens.DevicesScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceDark
import com.example.ui.theme.MetalScanProTheme
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DetectorViewModel

sealed class AppNavDestination(val route: String, val title: String, val icon: ImageVector, val tag: String) {
    object Detect : AppNavDestination("detect", "Detect", Icons.Default.Sensors, "nav_tab_detect")
    object Calibration : AppNavDestination("calibration", "Calibrate", Icons.Default.Tune, "nav_tab_calibration")
    object Devices : AppNavDestination("devices", "Hardware", Icons.Default.PhoneAndroid, "nav_tab_devices")
    object History : AppNavDestination("history", "History", Icons.Default.History, "nav_tab_history")
    object Settings : AppNavDestination("settings", "Settings", Icons.Default.Settings, "nav_tab_settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetalScanProTheme {
                val detectorViewModel: DetectorViewModel = viewModel()
                MetalScanProApp(detectorViewModel)
            }
        }
    }
}

data class NavItem(val route: String, val getTitle: (Boolean) -> String, val icon: ImageVector, val tag: String)

@Composable
fun MetalScanProApp(
    viewModel: DetectorViewModel
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isAr = appLanguage == "ar"
    val layoutDirection = if (isAr) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: "detect"

        val activeSensor by viewModel.activeSensor.collectAsStateWithLifecycle()
        val connectionStatus by activeSensor.connectionStatus.collectAsStateWithLifecycle()
        val isDetecting by viewModel.isDetecting.collectAsStateWithLifecycle()

        val navItems = listOf(
            NavItem("detect", { AppStrings.navDetect(it) }, Icons.Default.Sensors, "nav_tab_detect"),
            NavItem("calibration", { AppStrings.navCalibrate(it) }, Icons.Default.Tune, "nav_tab_calibration"),
            NavItem("devices", { AppStrings.navHardware(it) }, Icons.Default.PhoneAndroid, "nav_tab_devices"),
            NavItem("history", { AppStrings.navHistory(it) }, Icons.Default.History, "nav_tab_history"),
            NavItem("settings", { AppStrings.navSettings(it) }, Icons.Default.Settings, "nav_tab_settings")
        )

        Scaffold(
            modifier = Modifier.fillMaxSize().background(DetectorDarkBg),
            topBar = {
                TopDetectorAppBar(
                    activeSensor = activeSensor,
                    connectionStatus = connectionStatus,
                    isDetecting = isDetecting,
                    isArabic = isAr,
                    onToggleLanguage = { viewModel.toggleAppLanguage() }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = DetectorSurfaceDark,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .border(1.dp, DetectorSurfaceBorder)
                        .testTag("main_navigation_bar")
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        val title = item.getTitle(isAr)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    color = if (isSelected) AmberRadar else TextSecondary
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AmberRadar,
                                selectedTextColor = AmberRadar,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = AmberRadar.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                }
            },
            containerColor = DetectorDarkBg
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(DetectorDarkBg)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "detect",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("detect") {
                        DetectScreen(
                            viewModel = viewModel,
                            onNavigateCalibration = {
                                navController.navigate("calibration")
                            },
                            onNavigateSettings = {
                                navController.navigate("settings")
                            }
                        )
                    }
                    composable("calibration") {
                        CalibrationScreen(viewModel = viewModel)
                    }
                    composable("devices") {
                        DevicesScreen(viewModel = viewModel)
                    }
                    composable("history") {
                        HistoryScreen(viewModel = viewModel)
                    }
                    composable("settings") {
                        SettingsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
