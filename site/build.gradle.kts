tasks {
	val copyStatic by creating(Copy::class) {
		from("static")
		into("build/site")
	}

	val copyDocs by creating(Copy::class) {
		dependsOn(":uranium-core:dokka")
		from("../uranium-core/build/dokka/uranium-core")
		into("build/site/docs")
	}

	val assembleSite by creating {
		dependsOn(copyStatic)
		dependsOn(copyDocs)
	}
}
