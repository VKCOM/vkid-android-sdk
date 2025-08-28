package com.vk.id.common.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.vk.id.common.InternalVKIDApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@InternalVKIDApi
public fun internalVKIDRememberModalBottomSheetState(
//    skipPartiallyExpanded: Boolean = true,
//    confirmValueChange: (SheetValue) -> Boolean,
//    skipHiddenState: Boolean,
//    density: Density,
): SheetState {
    val materialSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    return materialSheetState
//    return rememberSaveable(
//        true,
//        { },
//        saver = SheetState.Saver(
//            skipPartiallyExpanded = skipPartiallyExpanded,
//            confirmValueChange = confirmValueChange,
//            density = density,
//        )
//    ) {
//        SheetState(
//            skipPartiallyExpanded,
//            density,
//            SheetValue.Hidden,
//            confirmValueChange,
//            skipHiddenState
//        )
//    }
}
