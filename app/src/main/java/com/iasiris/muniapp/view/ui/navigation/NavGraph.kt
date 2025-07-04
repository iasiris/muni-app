package com.iasiris.muniapp.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iasiris.muniapp.view.ui.navigation.Routes.CART
import com.iasiris.muniapp.view.ui.navigation.Routes.LOGIN
import com.iasiris.muniapp.view.ui.navigation.Routes.ORDER_HISTORY
import com.iasiris.muniapp.view.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.view.ui.navigation.Routes.PRODUCT_DETAIL
import com.iasiris.muniapp.view.ui.navigation.Routes.PROFILE
import com.iasiris.muniapp.view.ui.navigation.Routes.REGISTER
import com.iasiris.muniapp.view.ui.screen.CartScreen
import com.iasiris.muniapp.view.ui.screen.LoginScreen
import com.iasiris.muniapp.view.ui.screen.OrderHistoryScreen
import com.iasiris.muniapp.view.ui.screen.ProductCatalogScreen
import com.iasiris.muniapp.view.ui.screen.ProductDetailScreen
import com.iasiris.muniapp.view.ui.screen.ProfileScreen
import com.iasiris.muniapp.view.ui.screen.RegisterScreen
import com.iasiris.muniapp.view.viewmodel.CartViewModel
import com.iasiris.muniapp.view.viewmodel.LoginViewModel
import com.iasiris.muniapp.view.viewmodel.OrderHistoryViewModel
import com.iasiris.muniapp.view.viewmodel.ProductCatalogViewModel
import com.iasiris.muniapp.view.viewmodel.ProductDetailViewModel
import com.iasiris.muniapp.view.viewmodel.ProfileViewModel
import com.iasiris.muniapp.view.viewmodel.RegisterViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCT_CATALOG = "product_catalog"
    const val PRODUCT_DETAIL = "product_detail/{id}"
    const val CART = "cart"
    const val PROFILE = "profile"
    const val ORDER_HISTORY = "order_history"
}

@Composable
fun NavGraph(
    navController: NavHostController
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val orderViewModel: OrderHistoryViewModel = hiltViewModel()
    val prodCatViewModel: ProductCatalogViewModel = hiltViewModel()
    val productDetailViewModel: ProductDetailViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = LOGIN
    ) { //TODO agregar if para chequear si usuario esta loggeado, si esta loggeado llevar directamente a home
        composable(LOGIN) {
            LoginScreen(navController, loginViewModel)
        }
        composable(REGISTER) {
            RegisterScreen(navController, registerViewModel)
        }
        composable(PRODUCT_CATALOG) {
            ProductCatalogScreen(navController, prodCatViewModel)
        }
        composable(PRODUCT_DETAIL) {
            ProductDetailScreen(navController, productDetailViewModel)
        }
        composable(PROFILE) {
            ProfileScreen(navController, profileViewModel)
        }
        composable(CART) {
            CartScreen(navController, cartViewModel)
        }
        composable(ORDER_HISTORY) {
            OrderHistoryScreen(navController, orderViewModel)
        }
    }
}

/*navigateToCatalog = { //EJEMPLO PARA BORRAR STACK DE NAVEGACION
    navigationController.navigate(ProductCatalog) {
        popUpTo(ProductCatalog) { inclusive = true}
    }
}*/