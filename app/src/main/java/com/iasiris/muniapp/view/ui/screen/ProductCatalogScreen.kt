package com.iasiris.muniapp.view.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.view.ui.components.CartWithImageOnTheTop
import com.iasiris.muniapp.view.ui.components.CustomSearchBar
import com.iasiris.muniapp.view.ui.components.PillCard
import com.iasiris.muniapp.view.ui.components.PillCardWithDropDownMenu
import com.iasiris.muniapp.view.ui.components.PrimaryButton
import com.iasiris.muniapp.view.ui.components.SimpleCircularProgressIndicator
import com.iasiris.muniapp.view.ui.components.SubheadText
import com.iasiris.muniapp.view.viewmodel.CartViewModel
import com.iasiris.muniapp.view.viewmodel.ProductCatalogViewModel

@Composable
fun ProductCatalogScreen(
    prodCatViewModel: ProductCatalogViewModel,
    cartViewModel: CartViewModel
) {
    val prodCatUiState by prodCatViewModel.prodCatUiState.collectAsStateWithLifecycle()
    val state = prodCatUiState.screenState

    LaunchedEffect(Unit) {
        prodCatViewModel.loadProducts()
    }
    when (state) {
        is ScreenState.Loading -> {
            SimpleCircularProgressIndicator()
        }

        is ScreenState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = paddingMedium, start = paddingMedium),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    SubheadText(
                        text = stringResource(id = R.string.muni),
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold
                    )
                }

                CustomSearchBar(
                    searchText = prodCatUiState.searchText,
                    onSearchTextChange = { prodCatViewModel.onSearchTextChange(it) }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = paddingMedium)
                ) {
                    LazyRow {
                        item {
                            PillCardWithDropDownMenu(
                                selectedOrder = prodCatUiState.selectedOrder,
                                onOrderSelected = { option ->
                                    prodCatViewModel.onOrderSelected(option)
                                }
                            )
                        }
                        items(prodCatUiState.categories) { category ->
                            val isSelected = category == prodCatUiState.selectedCategory
                            PillCard(
                                text = category,
                                isSelected = isSelected,
                                onClick = { prodCatViewModel.onCategorySelected(category) }
                            )
                        }
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = paddingMedium),
                    contentPadding = PaddingValues(bottom = paddingLarge, top = paddingSmall),
                    horizontalArrangement = Arrangement.spacedBy(paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(paddingSmall),
                ) {
                    items(prodCatUiState.products) { product ->
                        CartWithImageOnTheTop(
                            product = product,
                            onClick = {
                                cartViewModel.addToCart(product)
                            }
                        )
                    }
                }
            }
        }

        is ScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubheadText(
                        text = state.message,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    PrimaryButton(
                        onClick = { prodCatViewModel.loadProducts(true) },
                        label = "${stringResource(id = R.string.retry)}",
                    )
                }
            }
        }
    }
}


