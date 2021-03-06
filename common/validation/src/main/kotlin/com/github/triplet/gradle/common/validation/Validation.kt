package com.github.triplet.gradle.common.validation

import com.android.Version
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.logging.Logger
import org.gradle.util.GradleVersion
import org.gradle.util.VersionNumber

private val MIN_GRADLE_VERSION = GradleVersion.version("6.2")
private val MIN_AGP_VERSION = VersionNumber.parse("3.6.0-rc03")

/**
 * Validates required dependencies. If GPP can't run in the current context, an error will be
 * thrown.
 */
fun validateRuntime() {
    val agpVersion = VersionNumber.parse(Version.ANDROID_GRADLE_PLUGIN_VERSION)
    val validator = RuntimeValidator(
            GradleVersion.current(), MIN_GRADLE_VERSION, agpVersion, MIN_AGP_VERSION)

    validator.validate()
}

/** @return true if the variant is *not* debuggable and can therefore be published. */
fun validateDebuggability(variant: ApplicationVariant, logger: Logger): Boolean {
    val isValid = !variant.buildType.isDebuggable

    if (!isValid) {
        val typeName = variant.buildType.name
        if (typeName.equals("release", true)) {
            logger.error("GPP cannot configure variant '${variant.name}' because it is debuggable")
        } else {
            logger.info("Skipping debuggable build with type '$typeName'")
        }
    }

    return isValid
}
