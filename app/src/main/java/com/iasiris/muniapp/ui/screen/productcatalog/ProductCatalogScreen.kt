package com.iasiris.muniapp.ui.screen.productcatalog

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.iasiris.muniapp.R
import com.iasiris.muniapp.data.model.Product
import com.iasiris.muniapp.utils.components.BodyText
import com.iasiris.muniapp.utils.components.CaptionText
import com.iasiris.muniapp.utils.components.CustomSearchBar
import com.iasiris.muniapp.utils.components.PillCard
import com.iasiris.muniapp.utils.components.PillCardWithDropDownMenu
import com.iasiris.muniapp.utils.components.RowWithPriceAndHasDrink
import com.iasiris.muniapp.utils.components.SubheadText
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall

@Composable
fun ProductCatalogScreen(
    navigateToProductDetail: (String) -> Unit,
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
                    navigateToProductDetail = navigateToProductDetail
                )
            }
        }
    }
}

@Composable
fun CardWithImageInTheLeft( //TODO no esta cargando la imagen!!!
    product: Product,
    navigateToProductDetail: (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(paddingExtraSmall)
            .height(120.dp)
            .fillMaxWidth()
            .clickable {
                navigateToProductDetail(product.id)
            }
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
                BodyText(text = product.name)

                Spacer(modifier = Modifier.height(paddingExtraSmall))

                CaptionText(
                    text = product.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                RowWithPriceAndHasDrink(
                    price = product.price,
                    hasDrink = product.hasDrink,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}