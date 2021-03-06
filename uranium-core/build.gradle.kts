import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform") version "1.3.72"
    id("org.jetbrains.dokka") version "0.10.1"
    `maven-publish`
}

kotlin {
    jvm()
    js {
        targets {
            browser()
        }
    }
    wasm32()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.7")
            }
        }

        val coroutinesMain by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(coroutinesMain)
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
            }
        }

        val jsMain by getting {
            dependsOn(coroutinesMain)
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.7")
            }
        }

	    all {
		    languageSettings.enableLanguageFeature("InlineClasses")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
	    }
    }
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputDirectory = "$buildDir/dokka"

        multiplatform {
            val common by creating { }
            val jvm by creating { }
            val js by creating { }
            val wasm32 by creating { }
        }
    }
}

object Bintray
{
    const val repo = "uranium"
    const val name = "uranium-core"
    const val description = "Core package for uranium library"
    const val vcsUrl = "https://github.com/karol-202/uranium"
}

publishing {
    repositories {
        val user = System.getenv("BINTRAY_USER")
        val key = System.getenv("BINTRAY_KEY")

        maven("https://api.bintray.com/maven/$user/${Bintray.repo}/${Bintray.name}/;publish=1") {
            name = "Bintray"
            credentials {
                username = user
                password = key
            }
        }
    }

    publications.withType<MavenPublication>().all {
        pom {
            name.set(Bintray.name)
            description.set(Bintray.description)
            url.set(Bintray.vcsUrl)
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("karol202")
                    name.set("Karol Jurski")
                    email.set("karoljurski1@gmail.com")
                }
            }
            scm {
                url.set(Bintray.vcsUrl)
            }
        }
    }
}
