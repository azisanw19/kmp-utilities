# 1.0.0-alpha03

## Feinn Context 1.0.0-alpha03

### 🌟 New Features

- **Platform Context**: Enhanced context functionality to handle various utilities that require context 🔧.

## Feinn Context 1.0.0-alpha03

### 🌟 New Features

- **Launch Open Uri**: Launch functionality to handle web open uri and local storage uri 🛠️.️

# 1.0.0-alpha02

## Feinn Date Time 1.0.0-alpha02

### 🌟 New Features

- **Improved Date Parsing**: Enhanced parsing functions to handle a broader range of date formats with better error handling 🛠️.
- **Custom Date Format Support**: Added support for custom date formats to `FeinnDateTime.getFormattedDateTime()` for more flexible date-time formatting 🔧.
- **Locale Support for Regions**: `FeinnLocale` now supports Regions information, allowing formatting and parsing across multiple regions 🌍🕰️.
- **Time Zone Conversion**: Introduced a method to convert `FeinnDateTime` to different regions ⏰🌎.

### 🐞 Bug Fixes

- **Date Formatting Inconsistencies**: Fixed an issue where certain date formats were not rendering consistently on iOS 🍏 and Android 📱.
- **Crash Fix for Parsing Invalid Dates**: Fixed a crash that occurred when parsing invalid date strings. Now, a `FeinnDateTimeThrowable` is thrown with a clear error message 🛑.

### ⚠️ Known Issues

- Some complex time zone conversions may still exhibit slight inconsistencies between platforms 🔧.
- The `FeinnDate` and `FeinnDateTime` instances may behave differently when used in non-UTC time zones ⏳.
- Additional testing needed for edge cases with parsing time zone-specific date strings 🧪.

# 1.0.0-alpha01

## Feinn Date Time 1.0.0-alpha01

### 🌟 New Features

- **FeinnDate**: A class representing date with platform-specific implementations for different environments (Android 📱 and iOS 🍏).
- **FeinnLocale**: A locale utility class that allows you to retrieve and format dates based on the system's locale 🌍.
- **Date Formatting and Parsing**: Functions for formatting `FeinnDate` to strings and parsing strings into `FeinnDate` instances 🗓️.
- **Multi-platform Support**: Designed to work across multiple platforms, including Android 📱 and iOS 🍏.

### ⚠️ Known Issues

- Some date formatting options may not work consistently across all platforms 🛠️.
- Locale-specific features are still being tested and may require additional refinements 🔧.
- Limited unit tests for edge cases in date parsing and formatting 🧪.