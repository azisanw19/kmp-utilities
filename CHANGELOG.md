# 1.1.0

## Feinn Cryptographic
- Add AESCCM for encryption and decryption

## Feinn Screenshot
- Add screenshot component for Android and iOS BETA

# 1.0.0

## Feinn Byte Buffer
- Add documentation for FeinnByteBuffer
- Add function allocate

## Feinn Date Time
- Delete deprecated annotations

# 1.0.0-alpha10

## Feinn Byte Buffer
- Utilities to handle byte buffer like java.nio.ByteBuffer

# 1.0.0-alpha09

## Maintenance

- Upgrade dependencies

## Feinn Notification

- Update inline documentation

## Feinn Date Time

- FeinnDateTime library is deprecated and no longer maintained. Migrate to kotlinx-datetime for continued support.

# 1.0.0-alpha08

## Feinn Notification

### Fix Bugs

- Notification channel id and notification content id not match

# 1.0.0-alpha07
- Fix deployments failed

# 1.0.0-alpha06

## Feinn Permission

### New Feature

- Permission notification

## Feinn Notification

### New Feature

- Current notification supported on iOS and Android

# 1.0.0-alpha05

## Feinn Permission

### Bug Fixes

- iOS shouldShowRationale false when notDetermined, true when restricted or denied

## Feinn Date Time

### Bug Fixes

- iOS default locale to currentLocale

# 1.0.0-alpha04

## Feinn Permission

### New Features

- `rememberFeinnPermissionState`: A new `@Composable` function that remembers and provides a `FeinnPermissionState` instance for a given permission. It takes a permission type of `FeinnPermissionType` and a callback `(onPermissionResult)` that is invoked when the permission result is obtained, providing a `Boolean` indicating whether the permission was granted or denied.

## Feinn Launcher

### Bug Fixes

- Context Handling: Fixed an issue where the previous implementation was incorrectly relying on context, which has now been removed for improved functionality.

# 1.0.0-alpha03

## Feinn Platform Context

### New Features

- Platform Context: Enhanced context functionality to handle various utilities that require context.

## Feinn Launcher

### New Features

- Launch Open Uri: Launch functionality to handle web open uri and local storage uri.

# 1.0.0-alpha02

## Feinn Date Time

### New Features

- Improved Date Parsing: Enhanced parsing functions to handle a broader range of date formats with better error handling.
- Custom Date Format Support: Added support for custom date formats to `FeinnDateTime.getFormattedDateTime()` for more flexible date-time formatting.
- Locale Support for Regions: `FeinnLocale` now supports Regions information, allowing formatting and parsing across multiple regions.
- Time Zone Conversion: Introduced a method to convert `FeinnDateTime` to different regions.

### Bug Fixes

- Date Formatting Inconsistencies: Fixed an issue where certain date formats were not rendering consistently on iOS and Android.
- Crash Fix for Parsing Invalid Dates: Fixed a crash that occurred when parsing invalid date strings. Now, a `FeinnDateTimeThrowable` is thrown with a clear error message.

# 1.0.0-alpha01

## Feinn Date Time

### New Features

- FeinnDate: A class representing date with platform-specific implementations for different environments (Android and iOS).
- FeinnLocale: A locale utility class that allows you to retrieve and format dates based on the system's locale.
- Date Formatting and Parsing: Functions for formatting `FeinnDate` to strings and parsing strings into `FeinnDate` instances️.
- Multi-platform Support: Designed to work across multiple platforms, including Android and iOS.