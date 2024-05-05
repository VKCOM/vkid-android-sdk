import com.vk.id.health.metrics.buildspeed.buildSpeed
import com.vk.id.health.metrics.storage.firestore

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.binary.compatibility.validator) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.detekt) apply false
    id("vkid.android.dependency.lock") apply true
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.baselineprofile) apply false
    id("vkid.android.project-substitution") apply true
    id("vkid.health.metrics") apply true
}


//healthMetrics {
//    firestore(rootProject.file("build-logic/metrics/service-credentials.json"))
//    buildSpeed {
//        measuredTaskPath = ":help"
//    }
//    buildSpeed {
//        measuredTaskPath = ":logger:dependencies"
//    }
//}