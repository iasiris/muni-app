package com.iasiris.muniapp.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.iasiris.muniapp.ui.theme.MuniAppTheme

@Composable
fun SubheadText(
    text: String,
    fontWeight: FontWeight,
    color: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun BodyText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier
    )
}

@Composable
fun CaptionText(
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        fontWeight = fontWeight,
        color = color,
        modifier = modifier
    )
}

@Composable
fun ButtonText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    //14sp, Font Weight: Medium.
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        textAlign = textAlign,
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
fun OverlainText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ProductDetailPreview() {
    MuniAppTheme {
        Column {
            SubheadText(text = "Mountain", FontWeight.Bold)
            BodyText(text = "Mountain")
            CaptionText(text = "Mountain")
            ButtonText(text = "Mountain", color = Color.Black)
            OverlainText(text = "Mountain")

        }
    }
}