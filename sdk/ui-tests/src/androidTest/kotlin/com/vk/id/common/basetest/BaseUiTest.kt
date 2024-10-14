package com.vk.id.common.basetest

import com.kaspersky.components.alluresupport.withForcedAllureSupport
import com.kaspersky.components.composesupport.config.addComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase

public open class BaseUiTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder
        .withForcedAllureSupport(shouldRecordVideo = false)
        .addComposeSupport()

)
