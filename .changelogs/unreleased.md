* Backport Cherished Worlds to Forge 1.7.10, retaining the original package and mod ID.
* Add clickable favorites for worlds, saved servers, and discovered LAN servers.
* Sort favorites first while preserving world last-played order and server manual order within each group.
* Protect favorite worlds and saved servers from deletion.
* Add a favorite toggle to world creation and Ctrl+Shift+F for the selected list entry.
* Preserve favorites when editing a saved server's name or address.
* Support Et Futurum Requiem world screenshots and search without requiring EFR.
* Save upstream-compatible favorites with atomic replacement, backups, and corrupt-file protection.
* Use the GTNH buildscript, UniMixins, Java 8-compatible output, and upstream artwork and translations.
* Replace the Advanced World Selection integration with configurable mouse-following tooltips on the standard world screen.
* Show world seed, generator, day/time, and saved player position and dimension using the normal Minecraft tooltip font and screen-edge wrapping.
* Hold Ctrl for saved/current mod differences, with configurable activation, information fields, hover delay, width, and mod-list limits in the mod config screen.
* Read existing world metadata without modifying saves and retain native EFR screenshots and search.
* Compact the default tooltip to a shared world type/time/day line followed by X/Z/Y coordinates, with no Forge version or modifier-key hint.
* Show world tooltips immediately by default with a configurable 0 ms hover delay.
