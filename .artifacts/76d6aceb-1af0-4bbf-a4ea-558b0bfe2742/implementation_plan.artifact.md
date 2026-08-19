# Implementation Plan - Fix "Android BaseExtension not found" Error

The project is failing to sync because it uses Android Gradle Plugin (AGP) 9.0.1, which hides the legacy `BaseExtension` class by default. The Hilt Gradle plugin (version 2.57) still relies on this class, leading to the `IllegalStateException: Android BaseExtension not found`.

## User Review Required

> [!IMPORTANT]
> The fix involves setting `android.newDsl=false` in `gradle.properties`. This is a temporary workaround recommended by Google for projects using AGP 9.0 that still depend on plugins not yet fully migrated to the new internal DSL. This flag will be removed in AGP 10.0.

## Proposed Changes

### Build Configuration

#### [MODIFY] [gradle.properties](file:///C:/Users/Clayton/AndroidStudioProjects/RepSync/gradle.properties)
- Add `android.newDsl=false` to restore compatibility for the Hilt plugin.

#### [MODIFY] [libs.versions.toml](file:///C:/Users/Clayton/AndroidStudioProjects/RepSync/gradle/libs.versions.toml)
- Update Kotlin to `2.4.10` and KSP to `2.4.10-1.0.20` (estimated stable versions for AGP 9.x compatibility).
- Update Hilt to `2.60.1`.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Clayton/AndroidStudioProjects/RepSync/app/build.gradle.kts)
- Fix the library reference for Hilt Navigation Compose from `libs.hilt.nav.compose` to `libs.hilt.navigation.compose` to match `libs.versions.toml`.

## Verification Plan

### Automated Tests
- Run `./gradlew prepareKotlinBuildStats` or any sync-related task to verify the `BaseExtension` error is gone.
- Perform a full project sync in Android Studio.

### Manual Verification
- Verify that the Hilt components are correctly generated (if build succeeds).
- Check that the `android { ... }` block in `app/build.gradle.kts` is correctly recognized.
