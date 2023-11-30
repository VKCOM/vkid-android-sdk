/**
 * Copyright (c) 2020 - present, LLC “V Kontakte”

 * Permission is hereby granted to any person obtaining a copy of this Software to use the Software without charge.
 * Restrictions
 * You may not modify, merge, publish, distribute, sublicense, and/or sell copies, create derivative works based upon the Software or any part thereof.
 * Termination
 * This License is effective until terminated. LLC “V Kontakte” may terminate this License at any time without any without any negative consequences to our rights.
 * You may terminate this License at any time by deleting the Software and all copies thereof. Upon termination of this license for any reason,
 * you shall continue to be bound by the provisions of Section 2 above.
 * Termination will be without prejudice to any rights LLC “V Kontakte” may have as a result of this agreement.
 * Disclaimer of warranty and liability
 * THE SOFTWARE IS MADE AVAILABLE ON THE “AS IS” BASIS. LLC “V KONTAKTE” DISCLAIMS ALL WARRANTIES THAT THE SOFTWARE MAY BE SUITABLE OR UNSUITABLE
 * FOR ANY SPECIFIC PURPOSES OF USE. LLC “V KONTAKTE” CAN NOT GUARANTEE AND DOES NOT PROMISE ANY SPECIFIC RESULTS OF USE OF THE SOFTWARE.
 * UNDER NO SIRCUMSTANCES LLC “V KONTAKTE” BEAR LIABILITY TO THE LICENSEE OR ANY THIRD PARTIES FOR ANY DAMAGE IN CONNECTION WITH USE OF THE SOFTWARE.
 */

package com.vk.silentauth;

import android.os.Bundle;
import com.vk.silentauth.SilentAuthInfo;
import java.util.List;

interface ISilentAuthInfoProvider {
    List<SilentAuthInfo> getSilentAuthInfos(
        int appId,
        String packageName,
        String digestHash,
        String uuid,
        String sdkApiVersion,
        String clientDeviceId,
        String clientExternalDeviceId
    );

    /**
    * Send data for extend partial access token in other apps
    *
    * @params bundle wrapper with SilentAuthExchangeData inside (@see [com.vk.silentauth.SilentAuthExchangeData])
    */
    void updateTokenByExtendedHash(
        in Bundle bundle
    );
}
