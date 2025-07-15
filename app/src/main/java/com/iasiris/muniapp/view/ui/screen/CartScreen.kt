package com.iasiris.muniapp.view.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.view.ui.components.BackButtonWithTitle
import com.iasiris.muniapp.view.ui.components.BodyText
import com.iasiris.muniapp.view.ui.components.CaptionText
import com.iasiris.muniapp.view.ui.components.CardWithImageInTheLeftWithButtons
import com.iasiris.muniapp.view.ui.components.EmptyCartScreen
import com.iasiris.muniapp.view.ui.components.PrimaryButton
import com.iasiris.muniapp.view.ui.components.RowWithBodyTextAndAmount
import com.iasiris.muniapp.view.ui.components.RowWithSubheadTextAndAmount
import com.iasiris.muniapp.view.ui.components.SimpleCircularProgressIndicator
import com.iasiris.muniapp.view.viewmodel.CartViewModel
import com.iasiris.muniapp.view.viewmodel.OrderHistoryViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    orderHistoryViewModel: OrderHistoryViewModel
) {
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    val isOrderAdded = orderHistoryViewModel.orderHistoryUiState.collectAsStateWithLifecycle().value.isOrderAdded

    LaunchedEffect(Unit) {
        cartViewModel.init()
    }

    LaunchedEffect(isOrderAdded) {
        if (isOrderAdded) {
            showDialog = true
            cartViewModel.clearCart()
            orderHistoryViewModel.resetOrderAdded()
        }
    }

    if (cartUiState.cartItems.isEmpty() && !showDialog) {
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingMedium)
                    .weight(1f)
            ) {
                itemsIndexed(cartUiState.cartItems) { index, cartItem ->
                    CardWithImageInTheLeftWithButtons(
                        cartItem = cartItem,
                        onIncrease = { cartViewModel.onIncreaseCartItem(cartItem) },
                        onDecrease = { cartViewModel.onDecreaseCartItem(cartItem) },
                        onRemove = { cartViewModel.onRemoveCartItem(cartItem) }
                    )
                    if (index < cartUiState.cartItems.lastIndex) {
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

                PrimaryButton(
                    label = stringResource(id = R.string.checkout),
                    onClick = {
                        cartUiState.isOrderProcessing = true
                        orderHistoryViewModel.addOrder(cartUiState.cartItems)
                    }
                )

                Spacer(modifier = Modifier.height(paddingMedium))

                if (showDialog) {//TODO NO ESTA MOSTRANDO EL DIALOGO
                    cartUiState.isOrderProcessing = false
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            PrimaryButton(
                                label = stringResource(id = R.string.confirm),
                                onClick = {
                                    showDialog = false
                                    cartViewModel.clearCart()
                                }
                            )
                        },
                        title = { BodyText(stringResource(id = R.string.order_success_title)) },
                        text = { CaptionText(stringResource(id = R.string.order_success_message)) },
                    )
                }
            }
        }
    }

    if (cartUiState.isOrderProcessing) {
        SimpleCircularProgressIndicator()
    }
}




