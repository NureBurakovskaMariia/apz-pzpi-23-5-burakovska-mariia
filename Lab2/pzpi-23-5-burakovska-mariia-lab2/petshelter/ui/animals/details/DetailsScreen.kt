package ua.nure.petshelter.ui.animals.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.petshelter.R
import ua.nure.petshelter.repository.dto.AnimalDto
import ua.nure.petshelter.ui.compose.PScreen
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                Details.Event.OnBack -> navController.navigateUp()
            }
        }
    }

    DetailsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun DetailsScreenContent(
    state: Details.State,
    onAction: (Details.Action) -> Unit
) {
    PScreen(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.color.active)
            }
            return@PScreen
        }

        val animal = state.animal
        if (animal == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data not found", color = AppTheme.color.grey)
            }
            return@PScreen
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppTheme.dimension.normal)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .clickable { onAction(Details.Action.OnBackClick) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimension.large))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = animal.name,
                    style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp)
                )
                Text(
                    text = animal.status.uppercase(),
                    style = AppTheme.typography.small.copy(
                        color = AppTheme.color.active,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(AppTheme.color.active.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimension.normal))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
            ) {
                InfoPill(label = "Species", value = animal.species, modifier = Modifier.weight(1f))
                InfoPill(label = "Gender", value = getGenderText(animal.gender), modifier = Modifier.weight(1f))
                InfoPill(label = "Breed", value = animal.breed ?: "Unknown", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(AppTheme.dimension.large))

            Text(
                text = "About Me",
                style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(AppTheme.dimension.small))

            Text(
                text = animal.description ?: "This fluffy friend doesn't have a description yet, but is definitely waiting for you!",
                style = AppTheme.typography.regular.copy(color = AppTheme.color.grey),
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(AppTheme.dimension.large))
        }
    }
}

@Composable
private fun InfoPill(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .background(AppTheme.color.active.copy(alpha = 0.1f))
            .padding(AppTheme.dimension.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = AppTheme.typography.small.copy(color = AppTheme.color.grey))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = AppTheme.typography.regular.copy(color = AppTheme.color.active, fontWeight = FontWeight.Bold)
        )
    }
}

private fun getGenderText(gender: String?): String {
    return when (gender?.uppercase()) {
        "MALE" -> "Boy"
        "FEMALE" -> "Girl"
        else -> "Unknown"
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    AppTheme {
        DetailsScreenContent(
            state = Details.State(
                isLoading = false,
                animal = AnimalDto(
                    id = 1,
                    name = "Rex",
                    species = "Dog",
                    breed = "Labrador",
                    gender = "MALE",
                    birthDate = "2023-01-01",
                    description = "Rex is a very playful and kind dog. He loves chasing balls and taking long walks outdoors. Looking for an active family!",
                    status = "available"
                )
            ),
            onAction = {}
        )
    }
}