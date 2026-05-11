package ua.nure.petshelter.ui.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
fun RegistrationScreen(
    viewModel: RegisterViewModel,
    navController: NavController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Register.Event.OnBack -> navController.navigateUp()
                is Register.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }
    RegistrationScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegistrationScreenContent(
    state: Register.State,
    onAction: (Register.Action) -> Unit
) {
    PScreen(
        modifier = Modifier.padding(horizontal = AppTheme.dimension.normal),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PSTitle(title = stringResource(R.string.registrationTitle))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.joinUs),
            style = AppTheme.typography.large
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.registrationMessage),
            style = AppTheme.typography.small.copy(
                color = AppTheme.color.grey
            )
        )

        Column {
            PSInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.name),
                value = state.name,
                errorText = state.nameError?.let { stringResource(id = it) }
            ) {
                onAction(Register.Action.OnNameChange(name = it))
            }
            PSInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.email),
                value = state.email,
                errorText = state.emailError?.let { stringResource(id = it) }
            ) {
                onAction(Register.Action.OnEmailChange(email = it))
            }
            PSInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.password),
                value = state.password,
                isPassword = true,
                errorText = state.passwordError?.let { stringResource(id = it) }
            ) {
                onAction(Register.Action.OnPasswordChange(password = it))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimension.small)
                    .clickable{ onAction(Register.Action.OnPrivacyPolicyAgreementChange(isAgreed = !state.isPrivacyPolicyAgreed)) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.isPrivacyPolicyAgreed,
                    onCheckedChange = { isChecked ->
                        onAction(Register.Action.OnPrivacyPolicyAgreementChange(isAgreed = isChecked))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppTheme.color.active,
                        uncheckedColor = AppTheme.color.active
                    ),
                    modifier = Modifier.padding(end = AppTheme.dimension.extraSmall)
                )
                Text(
                    text = stringResource(R.string.privacyPolicyAgreement),
                    style = AppTheme.typography.small.copy(
                        textAlign = TextAlign.Start,
                        color = AppTheme.color.grey
                    )
                )
            }
        }

        PSButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal),
            text = stringResource(R.string.signUp),
            enabled = state.isPrivacyPolicyAgreed
        ) {
            onAction(Register.Action.OnRegister)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = AppTheme.dimension.small, bottom = AppTheme.dimension.large)
                .clickable {
                    onAction(Register.Action.OnNavigate(Screen.Auth.SignIn))
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(end = AppTheme.dimension.small),
                text = stringResource(R.string.haveAnAcc),
                style = AppTheme.typography.regular.copy(
                    color = AppTheme.color.grey
                )
            )
            Text(
                text = stringResource(R.string.login),
                style = AppTheme.typography.regular.copy(
                    color = AppTheme.color.active
                ),
            )
        }
    }
}

@Preview
@Composable
private fun RegistrationScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            RegistrationScreenContent(
                state = Register.State(isPrivacyPolicyAgreed = true)
            ) { }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegistrationScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            RegistrationScreenContent(
                state = Register.State(isPrivacyPolicyAgreed = true)
            ) { }
        }
    }
}