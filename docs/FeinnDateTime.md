![badge][badge-android]
![badge][badge-ios]
[![badge][badge-feinn-date-time]](https://central.sonatype.com/artifact/id.feinn.azisanw19/feinn-date-time)


# Date Time Utilities

First add the dependency to your project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("id.feinn.azisanw19:feinn-date-time:$kmp_utils_version")
}
```

Make sure to replace $kmp_utils_version with the appropriate version of the library.

## Usage

After adding the dependency, you can start using the Feinn Date Time utilities in your Kotlin
Multiplatform project.

### Getting the Current Date:

You can easily get the current date using the `FeinnDate` class. Here's an example:

```kotlin
// Get the current date and time
val currentDate = FeinnDate.now()

// Print the current date and time
println("Current Date and Time: ${currentDate.toString()}")
```

### Formatting Dates:

You can format a `FeinnDate` instance into a string with a specific format and locale:

```kotlin
val currentDate = FeinnDate.now()

// Format the date as "yyyy-MM-dd"
val formattedDate = currentDate.getFormattedDate(
    format = FeinnDateTimeFormatter.ISO_LOCAL_DATE,
    locale = FeinnLocale.getDefault()
)

println("Formatted Date: $formattedDate")
```

### Parsing a Date from a String:

You can also parse a date string into a `FeinnDate` instance:

```kotlin
// Parse a date string with the format "yyyy-MM-dd"
val dateString = "2024-11-23"
val parsedDate = FeinnDate.parse(
    dateString, 
    format = FeinnDateTimeFormatter.ISO_LOCAL_DATE, 
    locale = FeinnLocale.getDefault()
)

println("Parsed Date: ${parsedDate.toString()}")
```

### Working with Different Locales Date:

You can work with different locales by using the `FeinnLocale` class. Here's how to get the default
locale of the system and format a date accordingly:

```kotlin
val feinnLocale = FeinnLocale.getDefault()

// Format the current date using the default locale
val formattedDate = currentDate.getFormattedDate(format = "dd MMMM yyyy", locale = feinnLocale)

println("Formatted Date with Default Locale: $formattedDate")
```

### Getting Milliseconds from `FeinnDate`:

You can get the milliseconds from a `FeinnDate` instance:

```kotlin
val currentDate = FeinnDate.now()

// Get the current time in milliseconds
val millis = currentDate.millisSeconds

println("Current Milliseconds: $millis")
```

### Getting the Current Date and Time:

You can easily get the current date and time using the `FeinnDateTime` class. Here's an example:

```kotlin
// Get the current date and time
val currentDateTime = FeinnDateTime.now()

// Print the current date and time
println("Current Date and Time: ${currentDateTime.toString()}")
```

### Formatting Dates and Times:

You can format a `FeinnDateTime` instance into a string with a specific format and locale:

```kotlin
val currentDateTime = FeinnDateTime.now()

// Format the date and time as "yyyy-MM-dd HH:mm:ss"
val formattedDateTime = currentDateTime.getFormattedDateTime(
    format = "yyyy-MM-dd HH:mm:ss",
    locale = FeinnLocale.getDefault()
)

println("Formatted Date and Time: $formattedDateTime")
```

### Parsing a Date and Time from a String:

You can also parse a date and time string into a `FeinnDateTime` instance:

```kotlin
// Parse a date and time string with the format "yyyy-MM-dd HH:mm:ss"
val dateTimeString = "2024-11-23 14:30:00"
val parsedDateTime = FeinnDateTime.parse(
    dateTimeString, 
    format = "yyyy-MM-dd HH:mm:ss", 
    locale = FeinnLocale.getDefault()
)

println("Parsed Date and Time: ${parsedDateTime.toString()}")
```

### Converting to `FeinnDate`:

You can convert a `FeinnDateTime` instance to a `FeinnDate` instance:

```kotlin
val currentDateTime = FeinnDateTime.now()

// Convert to FeinnDate (start of the day)
val feinnDate = currentDateTime.toFeinnDate()

println("FeinnDate from DateTime: ${feinnDate.toString()}")
```

### Getting Milliseconds from `FeinnDateTime`:

You can get the milliseconds from a `FeinnDateTime` instance:

```kotlin
val currentDateTime = FeinnDateTime.now()

// Get the current time in milliseconds
val millis = currentDateTime.millisSeconds

println("Current Milliseconds: $millis")
```

### Working with Different Locales:

You can work with different locales by using the `FeinnLocale` class. Here's how to get the default
locale of the system and format a date and time accordingly:

```kotlin
val feinnLocale = FeinnLocale.getDefault()

// Format the current date and time using the default locale
val formattedDateTime = currentDateTime.getFormattedDateTime(
    format = "dd MMMM yyyy HH:mm:ss",
    locale = feinnLocale
)

println("Formatted Date and Time with Default Locale: $formattedDateTime")
```


[badge-feinn-date-time]: https://img.shields.io/maven-central/v/id.feinn.azisanw19/feinn-date-time.svg?style=flat
[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat