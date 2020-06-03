import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.61"
	`maven-publish`
}

dependencies {
	api(project(":uranium-core"))

	implementation(kotlin("stdlib-jdk8"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.3.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

tasks.register<Jar>("sourcesJar") {
	archiveClassifier.set("sources")
	from(sourceSets.main.get().allSource)
}

publishing {
	publications {
		create<MavenPublication>("swing") {
			from(components["java"])
			artifact(tasks["sourcesJar"])
		}
	}
}
