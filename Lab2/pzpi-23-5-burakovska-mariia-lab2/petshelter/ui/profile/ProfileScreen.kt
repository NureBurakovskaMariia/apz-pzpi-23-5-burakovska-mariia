package ua.nure.petshelter.ui.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ua.nure.petshelter.R
import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.repository.dto.TaskDto
import ua.nure.petshelter.ui.compose.PSInputField
import ua.nure.petshelter.ui.compose.PSTaskCard // Імпорт твоєї нової картки
import ua.nure.petshelter.ui.compose.PScreen
import ua.nure.petshelter.ui.compose.PSTitle
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Profile.Event.OnBack -> navController.navigateUp()
                is Profile.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }
    ProfileScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileScreenContent(
    state: Profile.State,
    onAction: (Profile.Action) -> Unit
) {
    val columnState = rememberScrollState()

    PScreen {
        PSTitle(title = stringResource(R.string.personalInformation))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimension.normal),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(130.dp)
                    .clip(shape = CircleShape)
                    .border(
                        width = 2.dp,
                        color = AppTheme.color.active,
                        shape = CircleShape
                    ),
                model = R.drawable.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(state = columnState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.personalInformation),
                    style = AppTheme.typography.small.copy(
                        color = AppTheme.color.active
                    ),
                    modifier = Modifier
                        .padding(
                            top = AppTheme.dimension.normal,
                            bottom = AppTheme.dimension.small
                        )
                )

                PSInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimension.small),
                    label = stringResource(R.string.name),
                    value = state.name,
                ) {}

                PSInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimension.small),
                    label = stringResource(R.string.email),
                    value = state.email,
                ) { }

                Spacer(modifier = Modifier.height(AppTheme.dimension.large))

                if (state.tasks.isNotEmpty()) {
                    Text(
                        text = "My Volunteer Tasks",
                        style = AppTheme.typography.large.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.color.foreground
                        ),
                        modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
                    )

                    state.tasks.forEach { task ->
                        PSTaskCard(
                            task = task,
                            onStatusChange = { isCompleted ->
                                onAction(Profile.Action.OnTaskStatusChange(task.id, isCompleted))
                            },
                            modifier = Modifier.padding(bottom = AppTheme.dimension.small)
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimension.large))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            ProfileScreenContent(state = Profile.State()) {}
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            ProfileScreenContent(state = Profile.State()) {}
        }
    }
}