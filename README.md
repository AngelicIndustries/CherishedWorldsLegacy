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
* Hover over a world for a regular mouse-following tooltip with its seed, generator, day/time, and saved player position and dimension. Hold **Ctrl** to see saved/current mod differences.

Favorites are stored in `cherishedworlds-favorites.dat` in the Minecraft instance folder, using upstream's uncompressed NBT format. Writes use a temporary file and keep the previous version as `.bak`. If the file cannot be read, it is preserved and changes are blocked until it is repaired or moved aside.

This backport retains the original `cherishedworlds` mod ID and `com.illusivesoulworks.cherishedworlds` package. Modern Minecraft's world-upgrade backup prompt is not applicable to 1.7.10.

## Dependencies

Install on the client with Forge 10.13.4.1614 and [UniMixins](https://github.com/LegacyModdingMC/UniMixins). Servers do not need the mod.

[Et Futurum Requiem](https://github.com/GTNewHorizons/Et-Futurum-Requiem) is optional. Compatibility is tested against 2.6.65-GTNH with world screenshots enabled and disabled.

## Configuration

Use **Mods > Cherished Worlds Legacy > Config**, or edit `config/cherishedworlds.cfg`. Changes made in the config screen apply immediately.

World tooltips are enabled by default after a 250 ms hover. You can change the delay, require Shift or Ctrl, disable individual information fields, or disable the tooltip entirely. Text uses Minecraft's normal font size, wraps to the available space, and follows the pointer. Long mod lists are capped to fit the screen.

Mod details use Ctrl by default; you can show them on ordinary hover, use Shift, or hide them. Missing, changed, and added mods appear before optional unchanged entries. The comparison uses Forge's mod list from the last world save and the currently active mods. It includes client mods and is not a compatibility verdict.

Details are read from `level.dat`, with a labeled `level.dat_old` fallback. No save data is written. Metadata is cached while the screen is open and refreshed when it is reopened. Advanced World Selection is no longer included or integrated; these tooltips work on the standard world screen with or without EFR.

## Building

```sh
./gradlew build
```

Java 8-compatible output using the GTNH buildscript. Before the first tag, use `VERSION=1.0.0-dev ./gradlew build`. Unit tests run on Java 8 as part of the build.

The development runtime includes the shared Angelic Industries test environment. These development mods are not bundled or published as required dependencies.

Validation includes Java 8 unit tests and client smoke tests with EFR absent, screenshots disabled, and screenshots enabled. The client checks cover filtering, selection, deletion protection, world creation, saved-server edits and join targets, LAN ordering, and GUI scaling. Dedicated-server startup is also checked.

## Credits

* [Illusive Soulworks](https://github.com/illusivesoulworks/cherishedworlds) for Cherished Worlds, its artwork and translations.
* Angelic Industries for the 1.7.10 backport.
* The [GTNH buildscript](https://github.com/GTNewHorizons/ExampleMod1.7.10), Forge and MCP teams.

## License

[LGPL-3.0-or-later](LICENSE), with the original copyright notices retained. Full license texts: [GPL](COPYING) and [LGPL](COPYING.LESSER).
