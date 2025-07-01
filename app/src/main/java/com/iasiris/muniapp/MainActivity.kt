package com.iasiris.muniapp

import android.os.Bundle
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
import androidx.navigation.compose.rememberNavController
import com.iasiris.muniapp.ui.navigation.BottomNavBar
import com.iasiris.muniapp.ui.navigation.NavGraph
import com.iasiris.muniapp.ui.theme.MuniAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuniAppTheme {
                MuniApp()
            }
        }
    }
}

@Composable
fun MuniApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController
            )

        }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavGraph(navController = navController)
        }
    }
}