![](https://img.shields.io/badge/-Astromine-A3254C?style=for-the-badge&logo=appveyor)
![](https://img.shields.io/badge/Minecraft-1.21.1-62B47A?style=for-the-badge)
![](https://img.shields.io/badge/NeoForge-21.1.x-F16436?style=for-the-badge)
![](https://img.shields.io/badge/Java-21-007396?style=for-the-badge)

Astromine is a space and technology mod for Minecraft. Build machines, move items, fluids, and energy, process resources, launch rockets, and head into orbit.

This branch targets **Minecraft 1.21.1** on **NeoForge 21.1.x**. Older branches contain the historical Fabric versions.

Please refer to the in-game manual for gameplay documentation.

<img src="https://i.imgur.com/qolQ8du.png" width="384" height="169"/>

![](https://img.shields.io/badge/-Features-A3254C?style=for-the-badge&logo=appveyor)

- **Machines!**

  Solid Fuel Generators, Fluid Generators, Electric Furnaces, Triturators, Alloy Smelters, Refineries, Electrolyzers, Fluid Mixers, Melters, Solidifiers, Wire Mills, Pressers, and more.

- **Logistics!**

  Item, fluid, energy, and utility pipes, with sided transfer controls and NeoForge-native item/fluid/energy capability support.

- **Storage!**

  Tanks, capacitors, buffers, creative variants, fluid filters, and persistent storage behavior.

- **Utilities!**

  Drills, pumps, block placers, block breakers, machine upgrade kits, and other industrial tools.

- **Space!**

  Rockets, orbital gameplay, bodies, stations, sky rendering, space slimes, and dimension travel.

- **Tests!**

  A GameTest suite covers machines, transfer, storage, logistics, and registration sanity.

**We've got a lot to offer; so check it out and let us know what you think!**

![](https://img.shields.io/badge/-Development-yellow?style=for-the-badge&logo=appveyor)

Active development happens on `development-1.21.1`.

CI builds the mod and runs the GameTest server.

![](https://img.shields.io/badge/-Releases-green?style=for-the-badge&logo=appveyor)

You can find pre-compiled **stable** binaries on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/astromine) and [Modrinth](https://modrinth.com/mod/astromine).

You can find pre-compiled **development** binaries in [GitHub Actions](https://github.com/Mixinors/Astromine/actions).

![](https://img.shields.io/badge/-Building-blue?style=for-the-badge&logo=appveyor)

Astromine requires **Java 21**. The Gradle wrapper is included, so you do not need a local Gradle install.

To build the mod:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

The resulting `.jar` files will appear in `build/libs`.

![](https://img.shields.io/badge/-Running-blueviolet?style=for-the-badge&logo=appveyor)

Client:

```bash
./gradlew runClient
```

Server:

```bash
./gradlew runServer
```

GameTests:

```bash
./gradlew runGameTestServer
```

Full local CI equivalent:

```bash
./gradlew build runGameTestServer --stacktrace --no-daemon
```

![](https://img.shields.io/badge/-Data%20Generation-orange?style=for-the-badge&logo=appveyor)

Astromine keeps generated assets and data in `src/generated/resources`.

To generate assets and data:

```bash
./gradlew runData
```

Commit generated resource changes when they are intentional.

![](https://img.shields.io/badge/-Publishing-lightgrey?style=for-the-badge&logo=appveyor)

The GitHub `Publish` workflow runs on `development-1.21.1` and publishes only after a successful build and GameTest run. Maven publishing requires explicit repository configuration.

Publishing uses:

- `MAVEN_URL`
- `MAVEN_USER`
- `MAVEN_PASS`, from GitHub Actions secrets

![](https://img.shields.io/badge/-Contributing-yellow?style=for-the-badge&logo=appveyor)

Please use the included `.editorconfig` when committing code.

Before opening a PR, run:

```bash
./gradlew build runGameTestServer
```

To apply license formatting:

```bash
./gradlew licenseFormat
```

If you're submitting a large pull request, especially for machines, logistics, fluids, energy, GUIs, worldgen, rockets, or body systems, please get in touch beforehand so we can make suggestions and make sure it will be accepted.
