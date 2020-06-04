import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig

plugins {
    kotlin("multiplatform") version "1.3.72"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
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

bintray {
    user = project.findProperty("bintray.user") as String?
    key = project.findProperty("bintray.key") as String?
    setPublications(*publishing.publications.names.toTypedArray())
    publish = true

    pkg(delegateClosureOf<PackageConfig> {
        repo = "uranium"
        name = "uranium"
        description = "Core package for uranium project"
        vcsUrl = "https://github.com/karol-202/uranium"
        githubRepo = "karol-202/uranium"
        setLicenses("MIT")
    })
}
