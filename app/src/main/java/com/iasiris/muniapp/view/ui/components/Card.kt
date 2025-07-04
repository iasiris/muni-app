package com.iasiris.muniapp.view.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.outlined.NoDrinks
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.iasiris.muniapp.R
import com.iasiris.muniapp.data.model.CartItem
import com.iasiris.muniapp.data.model.Order
import com.iasiris.muniapp.data.model.Product
import com.iasiris.muniapp.utils.paddingExtraSmall
import com.iasiris.muniapp.utils.paddingMedium
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.utils.sizeMedium
import com.iasiris.muniapp.view.ui.theme.MuniAppTheme
import com.iasiris.muniapp.view.ui.theme.Shapes
import com.iasiris.muniapp.view.viewmodel.PriceOrder

@Composable
fun PillCard(
    text: String, isSelected: Boolean = false, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(paddingSmall)
            .clickable { onClick() },
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(paddingSmall),
            modifier = Modifier.padding(horizontal = paddingMedium, vertical = paddingSmall)
        ) {
            BodyText(
                text = text,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PillCardWithDropDownMenu(
    selectedOrder: PriceOrder, onOrderSelected: (PriceOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isSelected = selectedOrder != PriceOrder.FEATURED
    val options = listOf(
        PriceOrder.FEATURED to stringResource(id = R.string.order_featured),
        PriceOrder.ASCENDING to stringResource(id = R.string.price_low_to_high),
        PriceOrder.DESCENDING to stringResource(id = R.string.price_high_to_low)
    )

    Card(
        modifier = Modifier
            .padding(paddingSmall)
            .clickable { expanded = true },
        shape = Shapes.medium,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = paddingMedium, vertical = paddingSmall)
        ) {
            BodyText(
                text = stringResource(id = R.string.order_by),
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(paddingExtraSmall))

            Icon(
                imageVector = Icons.Default.Reorder,
                contentDescription = stringResource(id = R.string.dropwdown_menu),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(sizeMedium)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { (option, label) ->
                val selected = option == selectedOrder
                DropdownMenuItem(
                    text = {
                        BodyText(
                            text = label, color = if (selected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = if (selected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        expanded = false
                        onOrderSelected(option)
                    },
                    modifier = if (selected) Modifier.background(MaterialTheme.colorScheme.primary)
                    else Modifier.background(Color.White)
                )
            }
        }
    }
}


@Composable
fun RowWithAddCartAndQuantity(
    quantity: Int, onAdd: () -> Unit = {}, onRemove: () -> Unit = {}, navigateTo: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        QuantityButtons(
            quantity, onAdd = onAdd, onRemove = onRemove, modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(paddingSmall))
        PrimaryButton(
            label = stringResource(id = R.string.add_to_cart),
            onClick = navigateTo,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )
    }
}

@Composable
fun RowWithPriceAndHasDrink(
    price: Double = 0.0, hasDrink: Boolean, fontWeight: FontWeight
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        SubheadText(
            text = "$$price", fontWeight = fontWeight
        )
        Icon(
            imageVector = if (hasDrink) Icons.Filled.LocalBar else Icons.Outlined.NoDrinks,
            contentDescription = stringResource(id = R.string.drink),
            tint = if (hasDrink) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun RowWithQuantityAndAmount(
    quantity: Int, totalAmount: Double
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        SubheadText(
            text = if (quantity == 1) stringResource(
                R.string.one_product, quantity
            ) else stringResource(R.string.products, quantity), fontWeight = FontWeight.Bold
        )
        SubheadText(
            text = "$$totalAmount", fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowWithSubheadTextAndAmount(
    text: String, totalAmount: Double
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        SubheadText(
            text = text, fontWeight = FontWeight.Bold
        )
        SubheadText(
            text = "$$totalAmount", fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowWithBodyTextAndAmount(
    text: String, totalAmount: Double
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BodyText(
            text = text, fontWeight = FontWeight.Normal
        )
        BodyText(
            text = "$$totalAmount", fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun RowWithNameAndDeleteIcon(
    text: String, onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        SubheadText(text = text, fontWeight = FontWeight.Bold)

        IconButton(
            onClick = onDelete
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",//stringResource(id = R.string.delete_icon),
                tint = Color.Gray,
            )
        }
    }
}

@Composable
fun RowWithPriceAndButtons(
    price: Double = 0.0, quantity: Int, onAdd: () -> Unit = {}, onRemove: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingSmall)
    ) {
        SubheadText(
            text = "$$price",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start
        )

        QuantityButtons(
            quantity = quantity,
            onAdd = onAdd,
            onRemove = onRemove,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun CardWithImageInTheLeft(
    product: Product, navigateToProductDetail: (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(paddingExtraSmall)
            .height(120.dp)
            .fillMaxWidth()
            .clickable {
                navigateToProductDetail(product.id)
            }) {
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
                    text = product.description, color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Composable
fun CardWithImageInTheLeftWithButtons(
    product: Product, onAdd: () -> Unit, onRemove: () -> Unit, onDelete: () -> Unit
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
                    text = product.description, color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Composable
fun CardWithDateAndTotal(
    order: Order
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        modifier = Modifier
            .padding(paddingExtraSmall)
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            BodyText(
                text = stringResource(id = R.string.order_id, order.orderId),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(paddingExtraSmall))

            BodyText(
                text = stringResource(id = R.string.order_total_price, order.totalPrice),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(paddingExtraSmall))

            CaptionText(
                text = stringResource(id = R.string.order_date, order.orderDate),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardsPreview() {
    MuniAppTheme {
        Column(
            modifier = Modifier.padding(paddingMedium)
        ) {
            PillCard(
                text = "Mountain", isSelected = false, onClick = {})
            PillCard(
                text = "Mountain", isSelected = true, onClick = {})
            Spacer(modifier = Modifier.height(paddingMedium))
            RowWithPriceAndHasDrink(
                price = 10.0, hasDrink = true, fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(paddingMedium))
            RowWithAddCartAndQuantity(
                quantity = 1,
            )
            RowWithQuantityAndAmount(
                quantity = 1, totalAmount = 10.0
            )
            RowWithQuantityAndAmount(
                quantity = 2, totalAmount = 10.0
            )
            RowWithSubheadTextAndAmount(
                text = "Total", totalAmount = 10.0
            )
            RowWithBodyTextAndAmount(
                text = "Subtotal", totalAmount = 10.0
            )
            RowWithNameAndDeleteIcon(
                text = "Product Name", onDelete = { })
            RowWithPriceAndButtons(price = 10.0, quantity = 1, onAdd = {}, onRemove = {})
            CardWithImageInTheLeft(
                product = Product(
                    id = "1",
                    name = "Product Name",
                    description = "Product Description",
                    imageUrl = "",
                    price = 10.0,
                    hasDrink = true,
                    category = "Category",
                ), navigateToProductDetail = {})
            CardWithImageInTheLeftWithButtons(
                product = Product(
                    id = "1",
                    name = "Product Name",
                    description = "Product Description",
                    imageUrl = "",
                    price = 10.0,
                    hasDrink = true,
                    category = "Category",
                    quantity = 1
                ), onAdd = {}, onRemove = {}, onDelete = {})
            CardWithDateAndTotal(
                order = Order(
                    orderId = "1", productsId = listOf(
                        CartItem(
                            name = "Product Name",
                            description = "Product Description",
                            imageUrl = "",
                            price = 10,
                            hasDrink = true,
                            quantity = 1
                        )
                    ),
                    totalPrice = 10,
                    orderDate = "05/06/2025"
                )
            )
        }
    }
}