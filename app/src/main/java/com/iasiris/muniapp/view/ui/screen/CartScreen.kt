package com.iasiris.muniapp.view.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iasiris.muniapp.R
import com.iasiris.muniapp.view.ui.components.BackButtonWithTitle
import com.iasiris.muniapp.view.ui.components.CardWithImageInTheLeftWithButtons
import com.iasiris.muniapp.view.ui.components.EmptyCartScreen
import com.iasiris.muniapp.view.ui.components.PrimaryButton
import com.iasiris.muniapp.view.ui.components.RowWithBodyTextAndAmount
import com.iasiris.muniapp.view.ui.components.RowWithSubheadTextAndAmount
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.view.ui.navigation.Routes.ORDER_HISTORY
import com.iasiris.muniapp.view.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()
    LaunchedEffect (Unit){
        cartViewModel.init()
    }

    if (cartUiState.products.isEmpty()) {
        EmptyCartScreen(navController)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            BackButtonWithTitle(
                title = stringResource(id = R.string.cart_title),
                onBackButtonClick = { navController.popBackStack() }

            )

            LazyColumn( //TODO fix scrollable content que se corta con el bloque de abajo
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingMedium)
                    .weight(1f)
            ) {
                itemsIndexed(cartUiState.products) { index, product ->
                    CardWithImageInTheLeftWithButtons(
                        product = product,
                        onAdd = { cartViewModel.onAddProduct(product) },
                        onRemove = { cartViewModel.onRemoveProduct(product) },
                        onDelete = { cartViewModel.onDeleteProduct(product) }
                    )
                    if (index < cartUiState.products.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = paddingLarge,
                                    vertical = paddingExtraSmall
                                ),
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(paddingSmall))

            Column(
                modifier = Modifier.padding(horizontal = paddingMedium)
            ) {
                RowWithBodyTextAndAmount(
                    text = stringResource(id = R.string.cart_subtotal),
                    totalAmount = cartUiState.subTotal
                )

                RowWithBodyTextAndAmount(
                    text = stringResource(id = R.string.delivery_fee),
                    totalAmount = cartUiState.deliveryFee
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                RowWithSubheadTextAndAmount(
                    text = stringResource(id = R.string.cart_total),
                    totalAmount = cartUiState.totalAmount
                )

                Spacer(modifier = Modifier.height(paddingMedium))

                PrimaryButton( //TODO agregar proceso de guardado del pedido
                    label = stringResource(
                        id = R.string.checkout
                    ),
                    onClick = { navController.navigate(ORDER_HISTORY) }//TODO navegar a order history screen cuando el pedido este guardado
                )

                Spacer(modifier = Modifier.height(paddingMedium))
            }
        }
    }
}

