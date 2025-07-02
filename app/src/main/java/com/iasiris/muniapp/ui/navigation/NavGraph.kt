package com.iasiris.muniapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iasiris.muniapp.ui.navigation.Routes.CART
import com.iasiris.muniapp.ui.navigation.Routes.LOGIN
import com.iasiris.muniapp.ui.navigation.Routes.ORDER_HISTORY
import com.iasiris.muniapp.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.ui.navigation.Routes.PRODUCT_DETAIL
import com.iasiris.muniapp.ui.navigation.Routes.PROFILE
import com.iasiris.muniapp.ui.navigation.Routes.REGISTER
import com.iasiris.muniapp.ui.screen.cart.CartScreen
import com.iasiris.muniapp.ui.screen.login.LoginScreen
import com.iasiris.muniapp.ui.screen.orderhistory.OrderHistoryScreen
import com.iasiris.muniapp.ui.screen.productcatalog.ProductCatalogScreen
import com.iasiris.muniapp.ui.screen.productdetail.ProductDetailScreen
import com.iasiris.muniapp.ui.screen.profile.ProfileScreen
import com.iasiris.muniapp.ui.screen.register.RegisterScreen

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
    // val cartViewModel: CartViewModel = hiltViewModel()
    //val prodCatViewModel: ProductCatalogViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = LOGIN
    ) { //TODO agregar if para chequear si usuario esta loggeado, si esta loggeado llevar directamente a home
        composable(LOGIN) {
            LoginScreen(navController)
        }
        composable(REGISTER) {
            RegisterScreen(navController)
        }
        composable(PRODUCT_CATALOG) {
            ProductCatalogScreen(navController)
        }
        composable(PRODUCT_DETAIL) {
            ProductDetailScreen(navController)
        }
        composable(PROFILE) {
            ProfileScreen(navController)
        }
        composable(CART) {
            CartScreen(navController)
        }
        composable(ORDER_HISTORY) {
            OrderHistoryScreen(navController)
        }
    }
}

/*navigateToCatalog = { //EJEMPLO PARA BORRAR STACK DE NAVEGACION
    navigationController.navigate(ProductCatalog) {
        popUpTo(ProductCatalog) { inclusive = true}
    }
}*/