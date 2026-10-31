# Cherished Worlds Legacy

Favorite worlds and servers in Minecraft 1.7.10.

<img src="src/main/resources/assets/cherishedworlds/logo.png" width="200" alt="Cherished Worlds">

[GitHub releases](https://github.com/AngelicIndustries/CherishedWorldsLegacy/releases) · [Discord](https://discord.gg/xAWCqGrguG)

## Features

* Click the star beside a world, saved server, or LAN server to favorite it.
* Favorites appear first. Worlds keep their last-played ordering within each group; saved servers keep their manual ordering.
* Favorite worlds and saved servers are protected from deletion until you unfavorite them.
* Mark a world as a favorite while creating it with the star beside its name.
* Use **Ctrl+Shift+F** to toggle the selected world or server.
* Works with Et Futurum Requiem's world screenshots and search, including filtered lists.
* Keeps favorites when you change a saved server's name or address.

Favorites are stored in `cherishedworlds-favorites.dat` in the Minecraft instance folder, using upstream's uncompressed NBT format. Writes use a temporary file and keep the previous version as `.bak`. If the file cannot be read, it is preserved and changes are blocked until it is repaired or moved aside.

This backport retains the original `cherishedworlds` mod ID and `com.illusivesoulworks.cherishedworlds` package. Modern Minecraft's world-upgrade backup prompt is not applicable to 1.7.10.

## Dependencies

Install on the client with Forge 10.13.4.1614 and [UniMixins](https://github.com/LegacyModdingMC/UniMixins). Servers do not need the mod.

[Et Futurum Requiem](https://github.com/GTNewHorizons/Et-Futurum-Requiem) is optional. Compatibility is tested against 2.6.65-GTNH with world screenshots enabled and disabled.

## Building

```sh
./gradlew build
```

Java 8-compatible output using the GTNH buildscript. Before the first tag, use `VERSION=1.0.0-dev ./gradlew build`. Unit tests run on Java 8 as part of the build.

The development runtime includes the shared Angelic Industries test environment. These development mods are not bundled or published as required dependencies.

Validation includes 12 Java 8 unit tests and client smoke tests with EFR absent, screenshots disabled, and screenshots enabled. The client checks cover filtering, selection, deletion protection, world creation, saved-server edits and join targets, LAN ordering, and GUI scaling. Dedicated-server startup is also checked.

## Credits

* [Illusive Soulworks](https://github.com/illusivesoulworks/cherishedworlds) for Cherished Worlds, its artwork and translations.
* Angelic Industries for the 1.7.10 backport.
* The [GTNH buildscript](https://github.com/GTNewHorizons/ExampleMod1.7.10), Forge and MCP teams.

## License

[LGPL-3.0-or-later](LICENSE), with the original copyright notices retained. Full license texts: [GPL](COPYING) and [LGPL](COPYING.LESSER).
