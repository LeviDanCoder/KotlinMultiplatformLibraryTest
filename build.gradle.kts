plugins {
    kotlin("multiplatform") version "1.7.10"
    id("maven-publish")

}

configurations {
    listOf(metadataApiElements, metadataApiElements).forEach {

        // Method #2
        it.get().outgoing.artifact(tasks.sourcesJar)
    }
}

group = "me.dan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val ktorVersion = "2.0.3"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }

    afterEvaluate {
        publishing {
            publications {
                register<MavenPublication>("release") {
                    artifact(tasks.sourcesJar.get())
                    groupId = "github.com.LeviDanCoder"
                    artifactId = "multiplatformtest"
                    version="1.0"
                }
            }
        }
    }

}

