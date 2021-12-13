import dev.triumphteam.helper.*

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

bukkit {
    name = "BadApplePlugin"
    apiVersion = "1.18"

    commands {
        command("badapple") {
            description = "Plays the Bad Apple video"
            permission = "video.badapple"
        }
    }
}
