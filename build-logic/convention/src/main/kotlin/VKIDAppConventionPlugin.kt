import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

private fun Project.shouldInjectManifestPlaceholders() = gradle
    .startParameter
    .taskNames
    .map { it.lowercase() }
    .any { it.contains("assemble") || it.contains("test") || it.contains("lint") }
