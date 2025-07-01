package com.iasiris.muniapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iasiris.muniapp.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.ui.navigation.Routes.PROFILE
import com.iasiris.muniapp.ui.screen.productcatalog.ProductCatalog

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
        /*composable(LOGIN) {
            Login(
                navigateToHome = { navController.navigate(Routes.PRODUCT_CATALOG) },
                navigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterBottomSheet(
                navigateToHome = { navController.navigate(Routes.PRODUCT_CATALOG) },
                onDismiss = { navController.popBackStack() }
            )
        }*/
        composable(PRODUCT_CATALOG) {
            ProductCatalog(
                navigateToProductDetail = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }
        /*composable(PRODUCT_DETAIL) {
            ProductDetail(
                navController,
                navigateToCart = { navController.navigate(CART) }
            )
        }
        composable(CART) {
            Cart(navController)
        }
        composable(PROFILE) {
            Profile(navController)
        }*/
    }
}

/*navigateToCatalog = { //EJEMPLO PARA BORRAR STACK DE NAVEGACION
    navigationController.navigate(ProductCatalog) {
        popUpTo(ProductCatalog) { inclusive = true}
    }
}*/