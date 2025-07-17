package com.iasiris.muniapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.view.ui.components.BottomNavBar
import com.iasiris.muniapp.view.ui.navigation.NavGraph
import com.iasiris.muniapp.view.ui.navigation.Routes
import com.iasiris.muniapp.view.ui.theme.MuniAppTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var userPreferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuniAppTheme {
                MuniApp(userPreferences)
            }
        }
    }
}

@Composable
fun MuniApp(userPreferences: UserPreferences) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER) {
                BottomNavBar(navController = navController)
            }

        }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavGraph(navController = navController, userPreferences = userPreferences)
        }
    }
}