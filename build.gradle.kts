import dev.triumphteam.helper.MinecraftVersion
import dev.triumphteam.helper.compileOnly
import dev.triumphteam.helper.paper

plugins {
    java
    id("me.mattstudios.triumph") version "0.2.7"
}

group = "dev.triumphteam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    paper()
}

dependencies {
    compileOnly(paper(MinecraftVersion.V1_18_1))
}

// Using my gradle plugin to generate the plugin.yml file for me.
// I know you said no libs, and *technically* it's not a lib, but if you say it's not allowed I can move it to the resources.
bukkit {
    name = "BadApplePlugin"
    apiVersion = "1.18"

    commands {
        command("badapple") {
            description = "Basic commands for the Bad Apple video."
            permission = "video.badapple"
        }
    }
}
