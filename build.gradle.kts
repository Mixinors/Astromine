plugins {
	id("com.matthewprenger.cursegradle") version "1.4.0"
	id("org.cadixdev.licenser") version "0.6.1"
	id("fabric-loom") version "1.0-SNAPSHOT"
	id("java")
	id("maven-publish")
}

var minecraftVersion = project.properties["minecraft"]
group = "com.github.mixinors"
version = "2.0.8" + project.properties["minecraft"]

license {
	header("LICENSE")
	ignoreFailures.set(true)
}

loom {
	accessWidenerPath.set(file("src/main/resources/astromine.accesswidener"))
	
	runs {
		create("Data") {
			client()
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${file("src/generated/resources")}")
			
			ideConfigGenerated(true)
			runDir("build/datagen")
			source(sourceSets.main.get())
		}
	}
}

repositories {
	maven("https://maven.vini2003.dev/releases")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://maven.architectury.dev")
	maven("https://jitpack.io")
	maven("https://ladysnake.jfrog.io/artifactory/mods")
	
	maven("https://maven.blamejared.com") {
		content {
			includeGroup("vazkii.patchouli")
		}
	}
}

dependencies {
	fun modApiInclude(str: String) {
		modApi(str)
		include(str)
	}
	
	
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings("net.fabricmc:yarn:${minecraftVersion}+build.11:v2")
	
	// Fabric
	modImplementation("net.fabricmc:fabric-loader:${project.properties["fabricLoader"]}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabricApi"]}")
	
	// Cardinal-Components
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${project.properties["cardinalComponents"]}")
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-block:${project.properties["cardinalComponents"]}")
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-chunk:${project.properties["cardinalComponents"]}")
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${project.properties["cardinalComponents"]}")
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-item:${project.properties["cardinalComponents"]}")
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-world:${project.properties["cardinalComponents"]}")
	modApiInclude("dev.onyxstudios.cardinal-components-api:cardinal-components-level:${project.properties["cardinalComponents"]}")
	
	// Satin
	modApiInclude("io.github.ladysnake:satin:${project.properties["satin"]}")
	
	modApiInclude("com.terraformersmc.terraform-api:terraform-shapes-api-v1:${project.properties["shapes"]}") // Shapes
	modApiInclude("vazkii.patchouli:Patchouli:${project.properties["patchouli"]}") // Patchouli
	modApi("com.terraformersmc:modmenu:${project.properties["modMenu"]}") // ModMenu
	modApiInclude("teamreborn:energy:${project.properties["trEnergy"]}") // Tech Reborn Energy
	modApiInclude("me.shedaniel.cloth:cloth-config-fabric:${project.properties["clothConfig"]}") // Cloth Config
	modApiInclude("com.shnupbups:Piglib:${project.properties["pigLib"]}") // PigLib
	modApi("me.shedaniel:RoughlyEnoughItems-fabric:${project.properties["rei"]}") // RoughlyEnoughItems
	modImplementation("dev.architectury:architectury-fabric:${project.properties["architectury"]}") // Arch API
	
	// Hammer
	modApiInclude("dev.vini2003:hammer-core:${project.properties["hammer"]}")
	modApiInclude("dev.vini2003:hammer-gui:${project.properties["hammer"]}")
	modApiInclude("dev.vini2003:hammer-gui-energy:${project.properties["hammer"]}")
	modApiInclude("dev.vini2003:hammer-gravity:${project.properties["hammer"]}")
	
	modApiInclude("com.shnupbups:CauldronLib:${project.properties["cauldronLib"]}") // Cauldron
}

tasks.processResources {
	inputs.property("version", project.version)
	
	filesMatching("fabric.mod.json") {
		expand("version" to project.version)
	}
}

tasks.withType<JavaCompile> {
	options.release.set(17)
}

java {
	withSourcesJar()
	
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
	main {
		resources {
			srcDir("src/generated/resources")
		}
	}
}
