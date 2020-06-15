tasks {
	val clean by creating(Delete::class) {
		setDelete("build/site/")
	}

	val copyDocs by creating(Copy::class) {
		dependsOn(":uranium-core:dokka")
		from("../uranium-core/build/dokka/")
		into("build/site/docs/")
	}

	val copyStatic by creating(Copy::class) {
		from("static/")
		into("build/site/")
	}

	val assembleSite by creating {
		dependsOn(clean)
		dependsOn(copyDocs)
		dependsOn(copyStatic)
	}
}
