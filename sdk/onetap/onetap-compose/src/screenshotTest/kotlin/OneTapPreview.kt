import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.onetap.compose.onetap.OneTap

@Preview
@Composable
private fun OneTapPreview() {
    OneTap(onAuth = { _, _ -> })
}