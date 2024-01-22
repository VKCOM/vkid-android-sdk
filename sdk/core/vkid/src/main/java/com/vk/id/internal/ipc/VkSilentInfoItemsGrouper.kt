/**
 * Copyright (c) 2020 - present, LLC “V Kontakte”
 *
 * 1. Permission is hereby granted to any person obtaining a copy of this Software to
 * use the Software without charge.
 *
 * 2. Restrictions
 * You may not modify, merge, publish, distribute, sublicense, and/or sell copies,
 * create derivative works based upon the Software or any part thereof.
 *
 * 3. Termination
 * This License is effective until terminated. LLC “V Kontakte” may terminate this
 * License at any time without any negative consequences to our rights.
 * You may terminate this License at any time by deleting the Software and all copies
 * thereof. Upon termination of this license for any reason, you shall continue to be
 * bound by the provisions of Section 2 above.
 * Termination will be without prejudice to any rights LLC “V Kontakte” may have as
 * a result of this agreement.
 *
 * 4. Disclaimer of warranty and liability
 * THE SOFTWARE IS MADE AVAILABLE ON THE “AS IS” BASIS. LLC “V KONTAKTE” DISCLAIMS
 * ALL WARRANTIES THAT THE SOFTWARE MAY BE SUITABLE OR UNSUITABLE FOR ANY SPECIFIC
 * PURPOSES OF USE. LLC “V KONTAKTE” CAN NOT GUARANTEE AND DOES NOT PROMISE ANY
 * SPECIFIC RESULTS OF USE OF THE SOFTWARE.
 * UNDER NO CIRCUMSTANCES LLC “V KONTAKTE” BEAR LIABILITY TO THE LICENSEE OR ANY
 * THIRD PARTIES FOR ANY DAMAGE IN CONNECTION WITH USE OF THE SOFTWARE.
*/
package com.vk.id.internal.ipc

import com.vk.silentauth.SilentAuthInfo
import com.vk.silentauth.SilentAuthInfoWithProviderWeight
import com.vk.silentauth.SilentTokenProviderInfo

internal object VkSilentInfoItemsGrouper {

    fun List<SilentAuthInfoWithProviderWeight>.groupByWeightAndUserHash(): List<SilentAuthInfoWithProviderWeight> {
        return this.groupByUserHash()
            .sortedWith(SilentAuthInfoPriorityComparator())
            .reversed()
            .distinctBy { it.info.getDistinctId() }
    }

    private fun List<SilentAuthInfoWithProviderWeight>.groupByUserHash(): List<SilentAuthInfoWithProviderWeight> {
        val mutableMap = this.groupBy { it.info.userHash }.toMutableMap()
        return mutableListOf<SilentAuthInfoWithProviderWeight>().apply {
            val infoItemsWithoutUserHash = mutableMap.remove(SilentAuthInfo.EMPTY_USER_HASH)
            infoItemsWithoutUserHash?.let { addAll(it) }

            mutableMap.entries.forEach { (_, infoItems) ->
                val maxProviderWeight = infoItems.maxByOrNull { it.providerWeight }?.providerWeight
                val providerInfoItems = infoItems.map { SilentTokenProviderInfo.fromSilentAuthInfo(it.info) }
                val prioritySilentInfo = infoItems.filter { it.providerWeight == maxProviderWeight }.maxByOrNull { it.info.weight }
                prioritySilentInfo?.copy(info = prioritySilentInfo.info.copy(providerInfoItems = providerInfoItems))
                    ?.let { add(it) }
            }
        }
    }

    /**
     * Compare SilentAuthInfo by display priority
     * if the priority of the first information is greater than the priority of the second information,
     * then the first information will be greater than the second information
     */
    private class SilentAuthInfoPriorityComparator : Comparator<SilentAuthInfoWithProviderWeight> {
        @Suppress("ReturnCount") // expected
        override fun compare(first: SilentAuthInfoWithProviderWeight, second: SilentAuthInfoWithProviderWeight): Int {
            val compareProviderWeightResult = first.providerWeight.compareTo(second.providerWeight)
            if (compareProviderWeightResult != 0) {
                return compareProviderWeightResult
            }
            val compareWeightResult = first.info.weight.compareTo(second.info.weight)
            if (compareWeightResult != 0) {
                return compareWeightResult
            }

            // If the weight is identical, then we give priority to authInfo with a valid userHash.
            // It will allow extend access token for providers after extending silent token
            // {@see VkExtendTokenManager.setHashesForProvider}.
            val firstHashAvailable = first.info.userHash != SilentAuthInfo.EMPTY_USER_HASH
            val secondHashAvailable = second.info.userHash != SilentAuthInfo.EMPTY_USER_HASH
            return firstHashAvailable.compareTo(secondHashAvailable)
        }
    }
}
