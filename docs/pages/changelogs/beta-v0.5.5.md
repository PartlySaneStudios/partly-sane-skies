# Beta v0.5.5

## New
- Custom Discord RPC (@Su386yt)
    - Added a customisable Discord RPC (The ``Playing:`` status). Default message when enabled is ``Playing Hypixel SkyBlock``.

![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/9a7cf944-c4f5-443b-b6a1-b75d50899aa0)

![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/c563afb7-3607-4bd7-acfb-30f455987dfc)

- Sanity Check (@RayDeeUx)
  - HAEH! Checks for one's sanity using their SkyBlock networth and join date (sourced from SkyCrypt, which is currently down, so this command is next to worthless right now).

## Changes
- Updated OneConfig (@j10a1n15)
- Garden Shop Trade Cost renamed to Visitor Trade Cost (@Su386yt)
- Farming toggles were moved to be more readable (@Su386yt)
- Keybinds moved from Minecraft config menu to OneConfig Menu (@Su386yt)

## Bug Fixes
- Fixed the ``/clearpssdata`` not actually reloading all data (@Su386yt)
- Fixed SkyMart Value not displaying anything (@Su386yt)

## Technical Changes
- me.partlysanestudios.partlysaneskies.api package was moved to me.partlysanestudios.partlysaneskies.data.api (@Su386yt)
- ``RequestsManager``, ``Request``, ``PublicDataManager``, ``RequestRunnable`` and ``PartlySaneSkies`` (main class) classes were all reprogrammed in Kotlin (@Su386yt)
- Requests system now runs on an independent thread (@Su386yt)
- Added names to a lot of threads (@Su386yt)

## Documentation changes
- Updated features list! (@FlagHater)
- Updated README! (@FlagHater)

**Full Changelog and Official Download Link**: https://github.com/PartlySaneStudios/partly-sane-skies/releases/tag/beta-v0.5.5
