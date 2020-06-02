plugins {
    kotlin("multiplatform") version "1.3.72"
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
