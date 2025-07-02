package com.iasiris.muniapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iasiris.muniapp.ui.navigation.Routes.CART
import com.iasiris.muniapp.ui.navigation.Routes.LOGIN
import com.iasiris.muniapp.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.ui.navigation.Routes.PROFILE
import com.iasiris.muniapp.ui.navigation.Routes.REGISTER
import com.iasiris.muniapp.ui.screen.cart.CartScreen
import com.iasiris.muniapp.ui.screen.login.LoginScreen
import com.iasiris.muniapp.ui.screen.productcatalog.ProductCatalogScreen
import com.iasiris.muniapp.ui.screen.profile.ProfileScreen
import com.iasiris.muniapp.ui.screen.register.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCT_CATALOG = "product_catalog"
    const val PRODUCT_DETAIL = "product_detail/{id}"
    const val CART = "cart"
    const val PROFILE = "profile"
}

@Composable
fun NavGraph(
    navController: NavHostController
) {
    // val cartViewModel: CartViewModel = hiltViewModel()
    //val prodCatViewModel: ProductCatalogViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = PRODUCT_CATALOG
    ) { //TODO agregar if para chequear si usuario esta loggeado, si esta loggeado llevar directamente a home
        composable(LOGIN) {
            LoginScreen(
                navigateToHome = { navController.navigate(PRODUCT_CATALOG) },
                navigateToRegister = { navController.navigate(REGISTER) }
            )
        }
        composable(REGISTER) {
            RegisterScreen(
                navigateToHome = { navController.navigate(PRODUCT_CATALOG) }
            )
        }
        composable(PRODUCT_CATALOG) {
            ProductCatalogScreen(
                navigateToProductDetail = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }
        composable(PROFILE) {
            ProfileScreen(navController)
        }
        composable(CART) {
            CartScreen(navController)
        }
        /*composable(PRODUCT_DETAIL) {
            ProductDetail(
                navController,
                navigateToCart = { navController.navigate(CART) }
            )
        }*/
    }
}

/*navigateToCatalog = { //EJEMPLO PARA BORRAR STACK DE NAVEGACION
    navigationController.navigate(ProductCatalog) {
        popUpTo(ProductCatalog) { inclusive = true}
    }
}*/