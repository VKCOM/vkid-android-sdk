package com.vk.id.sample.xml.onetapsheet

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.vk.id.onetap.xml.OneTapBottomSheet
import com.vk.id.sample.xml.R
import com.vk.id.sample.xml.uikit.common.onVKIDAuthFail
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess

public class OnetapSheetStylingXmlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vkid_activity_onetap_bottom_sheet)
        val vkidOneTapBottomSheet = findViewById<OneTapBottomSheet>(R.id.vkid_bottom_sheet)
        vkidOneTapBottomSheet.setCallbacks(
            onAuth = { oAuth, accessToken -> onVKIDAuthSuccess(this, accessToken) }, // TODO: Handle oAuth
            onFail = { oAuth, fail -> onVKIDAuthFail(this, fail) } // TODO: Handle oAuth
        )
        val button = findViewById<Button>(R.id.show_sheet_button)
        button.setOnClickListener {
            vkidOneTapBottomSheet.show()
        }
    }
}
