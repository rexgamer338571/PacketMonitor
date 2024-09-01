import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("com.gradleup.shadow") version "8.3.0"
}

repositories {
    mavenLocal()
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

group = "dev.ng5m"
version = "1.0.0"
description = "a"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("libs/PacketMonitor-${project.version}.jar"))
    }
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "dev.ng5m.PluginMain"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

