# BuildInfo Plugin

A plugin to generate a build info json file that can be used for
version introspection in code, to generate unique identifiers for
docker images, or anything that requires knowing which version
of the code is being deployed.

### How do I use it?

Add the plugin:

```kotlin
plugins {
    // ...other plugins...
    id("com.glitchybyte.gradle.plugin.buildinfoplugin") version "1.0.0"
}
```

Configure the plugin:

```kotlin
tasks {
    saveBuildInfo {
        // filename = "build-info.json" // Default value.
        // codeBitXor = 0xff00ff00      // Default value.
        destinations = setOf(
            "src/main/resources/com/glitchybyte/example"
        )
    }
}
```

**filename**: Output filename created on each destination.

**codeBitXor**: 32 bit number to xor the code as it's being
generated. This can make the code "look" different from your other
projects. It has no bearing on the actual generation or availability
of codes.

**destinations**: Output destination directories to save the
build-info file.

### What will be generated?

Build will trigger a file `build-info.json` to be generated in the
given destinations. Contents will be like this:

```json
    {
      "group": "com.glitchybyte.example", // Project group.
      "name": "ExampleApp",               // Project name.
      "version": "1.0.0",                 // Project version.
      "datetime": "20201128025915",       // Build date and time stamp.
      "code": "mxp6gj3"                   // Unique build code.
    }
```

`code` will be a short and easily usable build identifier (e.g.,
build version, image tag, etc.). It will be unique for the
particular project once per second until around the year 2150.
No actual words will be formed.

In the example given, the file will be generated in the
`src/main/resources/com/glitchybyte/example` directory,
so it can be easily read in a basic application. You can add as
many destination directories as you want.
