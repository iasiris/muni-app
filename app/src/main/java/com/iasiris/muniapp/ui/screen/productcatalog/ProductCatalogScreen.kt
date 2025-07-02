package com.iasiris.muniapp.ui.screen.productcatalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.components.CardWithImageInTheLeft
import com.iasiris.muniapp.utils.components.CustomSearchBar
import com.iasiris.muniapp.utils.components.PillCard
import com.iasiris.muniapp.utils.components.PillCardWithDropDownMenu
import com.iasiris.muniapp.utils.components.SubheadText
import com.iasiris.muniapp.utils.paddingMedium

@Composable
fun ProductCatalogScreen(
    navController: NavController,
    prodCatViewModel: ProductCatalogViewModel = hiltViewModel()
) {
    val prodCatUiState by prodCatViewModel.prodCatUiState.collectAsStateWithLifecycle()

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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddingMedium)
        ) {
            items(prodCatUiState.products) { product ->
                CardWithImageInTheLeft(
                    product = product,
                    navigateToProductDetail = { productId ->
                        navController.navigate("product_detail/$productId")
                    }
                )
            }
        }
    }
}

