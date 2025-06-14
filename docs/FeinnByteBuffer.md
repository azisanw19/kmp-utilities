![badge][badge-android]
![badge][badge-ios]
[![badge][badge-feinn-byte-buffer]](https://central.sonatype.com/artifact/id.feinn.azisanw19/feinn-byte-buffer)

# Buffer Utilities

A Kotlin Multiplatform library providing buffer utilities for efficient byte manipulation across platforms.

First add the dependency to your project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("id.feinn.azisanw19:feinn-byte-buffer:$kmp_utils_version")
}
```

Make sure to replace $kmp_utils_version with the appropriate version of the library.

## Usage

After adding the dependency, you can start using the Feinn Buffer utilities in your Kotlin Multiplatform project.

### Creating Buffers

You can create buffers in several ways:

```kotlin
// Create buffer from byte array
val buffer1 = FeinnByteBuffer.wrap(byteArrayOf(1, 2, 3, 4))

// Allocate new buffer with capacity
val buffer2 = FeinnByteBuffer.allocate(1024)
```

### Reading Data

```kotlin
// Get single byte
val byte = buffer.get()

// Get primitive types
val short = buffer.short
val int = buffer.int
val long = buffer.long
val float = buffer.float
val double = buffer.double
val char = buffer.char

// Bulk read
val byteArray = ByteArray(10)
buffer.get(byteArray) // Read 10 bytes into array
```

### Writing Data

```kotlin
// Put single byte
buffer.put(0x0A)

// Put primitive types
buffer.putShort(1000)
buffer.putInt(123456)
buffer.putLong(123456789L)
buffer.putFloat(3.14f)
buffer.putDouble(3.1415926535)
buffer.putChar('A')

// Bulk write
buffer.put(byteArrayOf(1, 2, 3, 4))
```

### Buffer Control

```kotlin
// Rewind buffer (prepare for re-reading)
buffer.rewind()

// Reset to marked position
buffer.reset()
```

### Byte Order

```kotlin
// Set byte order
buffer.order(FeinnByteOrder.LITTLE_ENDIAN)

// Get current byte order
val order = buffer.order()
```

### Direct Buffer Access

```kotlin
// Access backing array
if (buffer.hasArray()) {
    val array = buffer.array()
}
```

### Exception Handling

Common exceptions to handle:

- `FeinnBufferOverflowException`: When trying to write beyond buffer capacity
- `FeinnBufferUnderflowException`: When trying to read beyond buffer limit
- `FeinnReadOnlyBufferException`: When trying to modify a read-only buffer

[badge-feinn-byte-buffer]: https://img.shields.io/maven-central/v/id.feinn.azisanw19/feinn-byte-buffer.svg?style=flat
[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat