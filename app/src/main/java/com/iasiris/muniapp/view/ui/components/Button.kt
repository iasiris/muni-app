package com.iasiris.muniapp.view.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iasiris.muniapp.R
import com.iasiris.muniapp.utils.paddingSmall
import com.iasiris.muniapp.utils.sizeMedium
import com.iasiris.muniapp.view.ui.theme.MuniAppTheme
import kotlinx.coroutines.delay

@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        ButtonText(text = label)
    }
}

@Composable
fun SecondaryButton(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        ButtonText(
            text = label,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun BackButtonWithTitle(
    title: String,
    onBackButtonClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingSmall)
    ) {
        IconButton(
            onClick = { onBackButtonClick() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(id = R.string.back_button),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        SubheadText(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AddToButton(
    onClick: () -> Unit
) {
    var clicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (clicked) 1.2f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scale"
    )
    Button(
        onClick = {
            clicked = true
            onClick()
        },
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .size(40.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale)
            .then(
                if (clicked) Modifier else Modifier
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_to_cart_icon),
            tint = Color.White,
            modifier = Modifier.size(sizeMedium)
        )
    }
    if (clicked) {
        LaunchedEffect(Unit) {
            delay(150)
            clicked = false
        }
    }
}

@Composable
fun QuantityButtons(//TODO change names of variables onAdd, onRemove
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(60))
            .background(MaterialTheme.colorScheme.outlineVariant)
    ) {
        IconButton(
            onClick = onDecrease,
            enabled = quantity > 1,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(id = R.string.decrease_icon),
                tint = if (quantity > 1) MaterialTheme.colorScheme.onSurface else Color.Gray,
                modifier = Modifier.size(sizeMedium)
            )
        }

        ButtonText(
            text = quantity.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = onIncrease,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.increase_icon),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(sizeMedium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtons() {
    MuniAppTheme {
        Column {
            PrimaryButton(
                label = "Enviar",
                onClick = {}
            )

            SecondaryButton(
                label = "Enviar",
                onClick = {}
            )

            BackButtonWithTitle(
                title = "Titulo",
                onBackButtonClick = {}
            )
            AddToButton(onClick = {})

            QuantityButtons(
                quantity = 1,
                onIncrease = {},
                onDecrease = {},
                modifier = Modifier
            )
        }
    }
}