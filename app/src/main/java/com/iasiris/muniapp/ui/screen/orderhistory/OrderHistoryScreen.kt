package com.iasiris.muniapp.ui.screen.orderhistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iasiris.muniapp.R
import com.iasiris.muniapp.ui.navigation.Routes.PRODUCT_CATALOG
import com.iasiris.muniapp.utils.components.BackButtonWithTitle
import com.iasiris.muniapp.utils.components.CardWithDateAndTotal
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingLarge
import com.iasiris.muniapp.utils.paddingMedium

@Composable
fun OrderHistoryScreen(
    navController: NavController,
    orderHistoryViewModel: OrderHistoryViewModel = hiltViewModel()
) {
    val orderHistoryUiState by orderHistoryViewModel.orderHistoryUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        BackButtonWithTitle(
            title = stringResource(id = R.string.order_history_title),
            onBackButtonClick = { navController.navigate(PRODUCT_CATALOG) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddingMedium)
                .weight(1f)
        ) {
            itemsIndexed(orderHistoryUiState.orders) { index, order ->
                CardWithDateAndTotal(order)
                if (index < orderHistoryUiState.orders.lastIndex) {
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
    }
}