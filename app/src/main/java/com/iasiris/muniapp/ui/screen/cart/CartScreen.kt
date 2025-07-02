package com.iasiris.muniapp.ui.screen.cart

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.iasiris.muniapp.R
import com.iasiris.muniapp.data.model.Product
import com.iasiris.muniapp.utils.components.BackButtonWithTitle
import com.iasiris.muniapp.utils.components.CaptionText
import com.iasiris.muniapp.utils.components.EmptyCartScreen
import com.iasiris.muniapp.utils.components.PrimaryButton
import com.iasiris.muniapp.utils.components.RowWithBodyTextAndAmount
import com.iasiris.muniapp.utils.components.RowWithNameAndDeleteIcon
import com.iasiris.muniapp.utils.components.RowWithPriceAndButtons
import com.iasiris.muniapp.utils.components.RowWithSubheadTextAndAmount
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (cartUiState.products.isEmpty()) {
            EmptyCartScreen(it, navController)
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                BackButtonWithTitle(
                    title = stringResource(id = R.string.cart_title),
                    onBackButtonClick = { navController.popBackStack() }

                )

                LazyColumn( //TODO fix scrollable content
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = paddingMedium)
                        .weight(1f)
                ) {
                    itemsIndexed(cartUiState.products) { index, product ->
                        CardWithImageInTheLeft(
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

                    PrimaryButton( //TODO agregar proceso de checkout
                        label = stringResource(
                            id = R.string.checkout
                        ),
                        onClick = {}//TODO navigateToCheckout
                    )

                    Spacer(modifier = Modifier.height(paddingMedium))
                }


            }
        }
    }
}

@Composable
fun CardWithImageInTheLeft(
    product: Product,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        modifier = Modifier
            .padding(paddingExtraSmall)
            .height(120.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //TODO agregar imagen por default mientras cargan las imagenes reales
            AsyncImage(
                model = product.imageUrl,
                contentDescription = stringResource(id = R.string.product_image),
                onError = {
                    Log.i("AsyncImage", "Error loading image ${it.result.throwable.message}")
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(paddingSmall))

            Column(
                modifier = Modifier.padding(end = paddingMedium)
            ) {
                RowWithNameAndDeleteIcon(
                    text = product.name,
                    onDelete = onDelete,
                )

                CaptionText(
                    text = product.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                RowWithPriceAndButtons(
                    price = product.price,
                    quantity = product.quantity,
                    onAdd = onAdd,
                    onRemove = onRemove
                )
            }
        }
    }
}