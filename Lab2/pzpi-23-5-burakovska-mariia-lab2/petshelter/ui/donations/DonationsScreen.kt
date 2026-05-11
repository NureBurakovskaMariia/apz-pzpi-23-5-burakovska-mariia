package ua.nure.petshelter.ui.donations

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.petshelter.ui.compose.PSButton
import ua.nure.petshelter.ui.compose.PSDonationDialog
import ua.nure.petshelter.ui.compose.PScreen
import ua.nure.petshelter.ui.compose.PSTitle
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun DonationsScreen(
    viewModel: DonationsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is Donations.Event.ShowSnackbar -> {
                    println(event.message)
                }
                Donations.Event.OnDonationSuccess -> {

                }
            }
        }
    }

    DonationsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun DonationsScreenContent(
    state: Donations.State,
    onAction: (Donations.Action) -> Unit
) {
    PScreen {
        PSTitle(title = "Make a Donation")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.dimension.normal)
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimension.normal))

            Text(
                text = "Select Donation Type",
                style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(AppTheme.dimension.small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Donations.Type.entries.forEach { type ->
                    TypeChip(
                        text = type.title,
                        isSelected = state.selectedType == type,
                        onClick = { onAction(Donations.Action.OnTypeSelected(type)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.dimension.large))

            Text(
                text = "Amount / Quantity",
                style = AppTheme.typography.regular.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.amount,
                onValueChange = { onAction(Donations.Action.OnAmountChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 100.5", color = AppTheme.color.grey) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(AppTheme.dimension.large))

            Text(
                text = "Note (Optional)",
                style = AppTheme.typography.regular.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.note,
                onValueChange = { onAction(Donations.Action.OnNoteChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Add any details here...", color = AppTheme.color.grey) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                maxLines = 5,
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(AppTheme.dimension.large))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppTheme.color.active)
                }
            } else {
                PSButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Submit Donation"
                ) {
                    onAction(Donations.Action.OnSubmitClick)
                }
            }
            Spacer(modifier = Modifier.height(AppTheme.dimension.large))
        }
    }
    if (state.showSuccessDialog) {
        PSDonationDialog(
            onDismiss = { onAction(Donations.Action.OnDismissDialog) }
        )
    }
}

@Composable
private fun TypeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) AppTheme.color.active else AppTheme.color.active.copy(alpha = 0.1f)
    val textColor = if (isSelected) AppTheme.color.background else AppTheme.color.active

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = AppTheme.dimension.small),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppTheme.typography.regular.copy(color = textColor, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppTheme.color.active,
    unfocusedBorderColor = AppTheme.color.grey.copy(alpha = 0.5f),
    focusedTextColor = AppTheme.color.foreground,
    unfocusedTextColor = AppTheme.color.foreground,
    cursorColor = AppTheme.color.active
)

@Preview(showSystemUi = true)
@Composable
private fun DonationsScreenPreview() {
    AppTheme {
        DonationsScreenContent(
            state = Donations.State(),
            onAction = {}
        )
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DonationsScreenDarkPreview() {
    AppTheme {
        DonationsScreenContent(
            state = Donations.State(),
            onAction = {}
        )
    }
}