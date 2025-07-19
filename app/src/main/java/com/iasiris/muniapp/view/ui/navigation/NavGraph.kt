package com.iasiris.muniapp.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.view.ui.navigation.Routes.CART
import com.iasiris.muniapp.view.ui.navigation.Routes.LOGIN
import com.iasiris.muniapp.view.ui.navigation.Routes.ORDER_HISTORY
import com.iasiris.muniapp.view.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.view.ui.navigation.Routes.PROFILE
import com.iasiris.muniapp.view.ui.navigation.Routes.REGISTER
import com.iasiris.muniapp.view.ui.screen.CartScreen
import com.iasiris.muniapp.view.ui.screen.LoginScreen
import com.iasiris.muniapp.view.ui.screen.OrderHistoryScreen
import com.iasiris.muniapp.view.ui.screen.ProductCatalogScreen
import com.iasiris.muniapp.view.ui.screen.ProfileScreen
import com.iasiris.muniapp.view.ui.screen.RegisterScreen
import com.iasiris.muniapp.view.viewmodel.CartViewModel
import com.iasiris.muniapp.view.viewmodel.LoginViewModel
import com.iasiris.muniapp.view.viewmodel.OrderHistoryViewModel
import com.iasiris.muniapp.view.viewmodel.ProductCatalogViewModel
import com.iasiris.muniapp.view.viewmodel.ProfileViewModel
import com.iasiris.muniapp.view.viewmodel.RegisterViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCT_CATALOG = "product_catalog"
    const val CART = "cart"
    const val PROFILE = "profile"
    const val ORDER_HISTORY = "order_history"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    userPreferences: UserPreferences
) {
    val userIdFlow = userPreferences.userIdFlow.collectAsState(initial = null)
    val isLoggedIn = userIdFlow.value != null

    val cartViewModel: CartViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val orderHistoryViewModel: OrderHistoryViewModel = hiltViewModel()
    val prodCatViewModel: ProductCatalogViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) PRODUCT_CATALOG else LOGIN
    ) {
        composable(LOGIN) {
            val uiState = loginViewModel.loginUiState.collectAsState()
            LoginScreen(navController, loginViewModel)
            if (uiState.value.shouldNavigateToCatalog) {
                navController.navigate(PRODUCT_CATALOG) {
                    popUpTo(0) { inclusive = true }
                }
                loginViewModel.resetNavigationFlag()
            }
        }

        composable(REGISTER) {
            val uiState = registerViewModel.registerUiState.collectAsState()
            RegisterScreen(navController, registerViewModel)
            if (uiState.value.shouldNavigateToCatalog) {
                navController.navigate(PRODUCT_CATALOG) {
                    popUpTo(0) { inclusive = true }
                }
                registerViewModel.clearFlag { it.copy(shouldNavigateToCatalog = false) }
            }
        }

        composable(PRODUCT_CATALOG) {
            ProductCatalogScreen(prodCatViewModel, cartViewModel)
        }

        composable(PROFILE) {
            val uiState = profileViewModel.profileUiState.collectAsState()
            ProfileScreen(navController, profileViewModel)
            if (uiState.value.shouldNavigateToLogin) {
                navController.navigate(LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
                profileViewModel.clearFlag { it.copy(shouldNavigateToLogin = false) }
            }
        }

        composable(CART) {
            CartScreen(navController, cartViewModel, orderHistoryViewModel)
        }

        composable(ORDER_HISTORY) {
            OrderHistoryScreen(navController, orderHistoryViewModel)
        }
    }
}