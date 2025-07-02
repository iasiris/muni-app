package com.iasiris.muniapp.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingSmall

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val navBackStackEntry = navController?.currentBackStackEntryAsState()?.value
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        Triple(Icons.Default.Home, Routes.PRODUCT_CATALOG, "Product Catalog"),
        Triple(Icons.Default.ShoppingCart, Routes.CART, "Cart"),
        Triple(Icons.Default.Person, Routes.PROFILE, "Profile")
    )

    val selectedTab =
        items.indexOfFirst { it.second == currentRoute }.let { if (it == -1) 0 else it }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = paddingSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddingLarge),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                BottomNavItem(
                    icon = item.first,
                    isSelected = selectedTab == index,
                    onClick = {
                        if (navController != null && currentRoute != item.second) {
                            navController.navigate(item.second) {
                                launchSingleTop = true
                                restoreState = false
                                popUpTo(Routes.PRODUCT_CATALOG) { saveState = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(paddingSmall)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

        )
    }
}