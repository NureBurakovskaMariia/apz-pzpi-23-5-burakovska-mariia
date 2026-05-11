package ua.nure.petshelter.ui.auth.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.petshelter.R
import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.ui.compose.PSButton
import ua.nure.petshelter.ui.compose.PSInputField
import ua.nure.petshelter.ui.compose.PScreen
import ua.nure.petshelter.ui.compose.PSTitle
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Login.Event.OnBack -> navController.navigateUp()
                is Login.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }
    LoginScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun LoginScreenContent(
    state: Login.State,
    onAction: (Login.Action) -> Unit
) {
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context = context) }
    val coroutineScope = rememberCoroutineScope()
    PScreen(
        modifier = Modifier.padding(horizontal = AppTheme.dimension.normal),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PSTitle(title = stringResource(R.string.loginTitle))

        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.welcomeBack),
            style = AppTheme.typography.large
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.loginSubtitle),
            style = AppTheme.typography.small.copy(
                color = AppTheme.color.grey
            )
        )
        Column {
            PSInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.email),
                value = state.email,
                errorText = if (state.loginError != null) "" else null
            ) {
                onAction(Login.Action.OnEmailChange(email = it))
            }
            PSInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.password),
                value = state.password,
                isPassword = true,
                errorText = state.loginError?.let { stringResource(id = it) },
            ) {
                onAction(Login.Action.OnPasswordChange(password = it))
            }
        }

        PSButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal),
            text = stringResource(R.string.login),
        ) {
            onAction(Login.Action.OnLogIn)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = AppTheme.dimension.small, bottom = AppTheme.dimension.large)
                .clickable {
                    onAction(Login.Action.OnNavigate(Screen.Auth.Registration))
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(end = AppTheme.dimension.small),
                text = stringResource(R.string.dontHaveAnAcc),
                style = AppTheme.typography.regular.copy(
                    color = AppTheme.color.grey
                )
            )
            Text(
                text = stringResource(R.string.registrationTitle),
                style = AppTheme.typography.regular.copy(
                    color = AppTheme.color.active
                ),
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            LoginScreenContent(
                state = Login.State(
                )
            ) { }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            LoginScreenContent(
                state = Login.State(
                )
            ) { }
        }
    }
}