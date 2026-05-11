package ua.nure.petshelter.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ua.nure.petshelter.R
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun PSDonationDialog(
    modifier: Modifier = Modifier,
    title: String = "Thank You!",
    message: String = "Your donation helps our fluffy friends live better lives. We appreciate your support!",
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = AppTheme.color.cardBackground)
                .padding(AppTheme.dimension.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.donate_success),
                contentDescription = "Heart",
                tint = AppTheme.color.active,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(AppTheme.dimension.normal))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = AppTheme.typography.large.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(AppTheme.dimension.small))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = AppTheme.typography.regular.copy(
                    textAlign = TextAlign.Center,
                    color = AppTheme.color.grey
                )
            )

            Spacer(modifier = Modifier.height(AppTheme.dimension.large))

            PSButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Close"
            ) {
                onDismiss()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PSDonationDialogPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppTheme.color.background),
            contentAlignment = Alignment.Center
        ) {
            PSDonationDialog(
                onDismiss = {}
            )
        }
    }
}