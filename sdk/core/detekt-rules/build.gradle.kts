plugins {
    `kotlin-dsl`
}
dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.detekt.api)
    implementation(libs.detekt.cli)
    implementation(libs.detekt.metrics)
}