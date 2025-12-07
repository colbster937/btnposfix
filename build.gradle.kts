import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.language.jvm.tasks.ProcessResources



val MOD_ID = "btnposfix"
val MOD_NAME = "ButtonPositionFix"
val MOD_DESCRIPTION = "Repositions the main menu buttons to be consistent."
val MOD_VERSION = "1.0.0"
val MOD_MAIN = "dev.colbster937.$MOD_ID.${MOD_NAME}Mod"
val MOD_SIDE = "client"
val MOD_GITHUB = "https://github.com/colbster937/$MOD_ID"
val MOD_LICENSE = "AGPL-3.0-or-later"
val MOD_AUTHORS = listOf("Colbster937")
val MOD_CONTRIBUTORS = emptyList<String>()

val MC_VERSION = "b1.7.3"
val MIN_MC_VERSION = ""
val MAX_MC_VERSION = "1.12.2"
val LOADER_VERSION = "0.18.1"
val OSL_VERSION = "0.16.3"

val CONTACT_DISCORD = "https://dsc.webmc.fun"
val CONTACT_EMAIL = "colbster937@colbster937.dev"



plugins {
  id("java")
  id("fabric-loom") version "1.13-SNAPSHOT"
  id("ploceus") version "1.13-SNAPSHOT"
}

val MOD_AUTHORS_ARRAY = if (MOD_AUTHORS.isNotEmpty()) {
  MOD_AUTHORS.joinToString(
    separator = "\", \"",
    prefix = "\"",
    postfix = "\""
  )
} else {
  ""
}

val MOD_CONTRIBUTORS_ARRAY = if (MOD_CONTRIBUTORS.isNotEmpty()) {
  MOD_CONTRIBUTORS.joinToString(
    separator = "\", \"",
    prefix = "\"",
    postfix = "\""
  )
} else {
  ""
}

val MC_DEPEND = when {
  MIN_MC_VERSION.isNotBlank() && MAX_MC_VERSION.isNotBlank() ->
    ">=${MIN_MC_VERSION} <=${MAX_MC_VERSION}"
  MIN_MC_VERSION.isNotBlank() ->
    ">=${MIN_MC_VERSION}"
  MAX_MC_VERSION.isNotBlank() ->
    "<=${MAX_MC_VERSION}"
  else ->
    "*"
}

@Suppress("DEPRECATION")
loom {
  if (MOD_SIDE == "client") clientOnlyMinecraftJar()
  else if (MOD_SIDE == "server") serverOnlyMinecraftJar()
}

@Suppress("DEPRECATION")
ploceus {
  if (MOD_SIDE == "client") clientOnlyMappings()
  else if (MOD_SIDE == "server") serverOnlyMappings()
}

dependencies {
  minecraft("com.mojang:minecraft:$MC_VERSION")
  mappings(ploceus.featherMappings("23"))
  modImplementation("net.fabricmc:fabric-loader:$LOADER_VERSION")

  ploceus.dependOsl(OSL_VERSION, MOD_SIDE)
}

sourceSets {
	named("main") {
		java.srcDir("./src/main/java")
		resources.srcDir("./src/main/resources")
	}
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
  options.release.set(8)
}

tasks.withType<ProcessResources>() {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  outputs.upToDateWhen { false }
  doFirst {
    filesMatching(listOf("fabric.mod.json", "mixins.json")) {
			expand(mapOf(
				"mod_id" to MOD_ID,
        "mod_name" to MOD_NAME,
				"mod_description" to MOD_DESCRIPTION,
				"mod_version" to MOD_VERSION,
        "mod_main" to MOD_MAIN,
        "mod_side" to MOD_SIDE,
        "mod_github" to MOD_GITHUB,
        "mod_license" to MOD_LICENSE,
        "mod_authors" to MOD_AUTHORS_ARRAY,
        "mod_contributors" to MOD_CONTRIBUTORS_ARRAY,
        "mc_depend" to MC_DEPEND,
        "loader_version" to LOADER_VERSION,
        "osl_version" to OSL_VERSION,
        "contact_discord" to CONTACT_DISCORD,
        "contact_email" to CONTACT_EMAIL
			))
		}
  }

  inputs.files(tasks.named<JavaCompile>("compileJava").map { it.outputs.files })
}

tasks.remapJar {
  archiveFileName.set("$MOD_NAME-$MOD_VERSION.jar")
}