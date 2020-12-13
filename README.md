# BuildInfo Plugin

A Gradle plugin to generate a build info json file that can be
used for version introspection in code, to generate unique
identifiers for Docker images, or anything that requires knowing
which version of the code is being deployed.

### How do I use it?
*>>> This is not working at the moment. Will update as soon as
I get GCP Artifact Repository working. <<<*

In `settings.gradle.kts`:
```kotlin
pluginManagement {
    plugins {
        id("com.google.cloud.artifactregistry.gradle-plugin") version "2.1.1"
    }
    repositories {
        maven(url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo"))
    }
}
```

In `build.gradle.kts`:
```kotlin
plugins {
    id("com.glitchybyte.gradle.plugin.buildinfoplugin") version "1.0.0"
}

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

```json5
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
so it can be easily read in an application. You can add as
many destination directories as you want.
