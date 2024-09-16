import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.content.OneTapBottomSheetAuthStatus
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthFailed
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthSuccess
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentMain
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle

@Preview
@Composable
private fun OneTapBottomSheetFailedPreview() {
    SheetContentAuthFailed(
        "<Название сервиса>",
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        repeatClicked = {}
    )
}

@Preview
@Composable
private fun OneTapBottomSheetSuccessPreview() {
    SheetContentAuthSuccess(
        "Другое название сервиса",
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
    )
}

@Preview
@Composable
private fun OneTapBottomSheetTransparentDarkPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "Another service",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetDarkPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Dark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetTransparentLightPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.TransparentLight(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetLightPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthMailPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.MAIL),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthOKPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.OK),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthMailAndOKPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthOKAndMailPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetsignInAnotherAccountButtonEnabledPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = false,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFastAuthEnabledPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioOrderPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.Order,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioApplicationPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.Application,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioEnterToAccountPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterToAccount,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioOrderInServicePreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.OrderInService,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioRegistrationForTheEventPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "Кто Я такой, чтобы называть сервис",
        scenario = OneTapScenario.RegistrationForTheEvent,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}
