# Beta v0.6

Hey everyone,

On the one year anniversary of Partly Sane Skies' initial release, we are releasing what is the most technically complex, and biggest update in Partly Sane Skies' history. We've completely rewritten the way the mod interacts with the outside world, as well as itself. We're also debuting our new API, [Partly Sane Cloud](https://github.com/PartlySaneStudios/partly-sane-cloud) to hopefully solve some of the connection issues we have had in the past. This new API should also help us speed up development time for new features. We hope you enjoy!
Please continue to report bugs in the Discord server, and on behalf of the entire Partly Sane Studios team, thank you to everyone who contributed, supported or even simply used Partly Sane Skies this past year. It has truly been surreal.

## New
- Dungeon Waypoints (@Su386yt)
    - Visibly indicates which terminals, devices and levers still inactive, which ones are in progress, and which are completed
      ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/f2eedf3e-0be5-40a6-a9c7-6e7023b8e430)
- Wrong Tool for Crop Warning (@Su386yt)
    - Whenever you are using the wrong tool for a crop, you will now be warned with a bell and of course, and optional air raid siren
- OwO Language Transformer (@DerGruenkohl)
  -Transforms all of your chat messages to into OwO messages. I don't really know why we're still updating feature but this is now update number 3(?) relating to Owo.
- Prettify Skytils Mimic Killed Message (@Su386yt)
    - Replaces any Skytils mimic killed message with ``Mimic Killed!`` or another customizable option
- Added Superboom, Spirit Leaps, and Decoys to the item refill (@Su386yt)
- Added Health Alert, which now will warn you if you are low, in addition to members of your party (@Su386yt)

## Changes
- Moveable (Treecapitator) cooldown GUI (@Su386yt)
    - Using the OneConfig edit hud button, you can now move the Treecapitator cooldown
- Combined the API settings with Debug settings into a new Dev panel of the config menu (@j10a1n15)
- Added Option to only show the Discord RPC while playing SkyBlock (@j10a1n15)
- Option to disable certain rarities in the RNG Drop Banner (@j10a1n15)
- Public Data is now routed through Partly Sane Cloud, so players in India, China and North Korea, among other locations where GitHub is blocked can now use the mod more fully (@Su386yt)
    - There is still the option to get data from GitHub's servers directly
- Chat Alert system notifications now show the message content instead of just a warning (@Su386yt)

## Bug Fixes
- Fixed comically large RNG banner (@j10a1n15 and @Su386yt)
- Fixed banners not immediately updating when banner size was changed (@j10a1n15 and @Su386yt)
- Fixed certain dungeon start events not triggering when Color Non Messages is enabled (@Su386yt)
- Fix rare crash when the message is null (@j10a1n15)
- Fixed crash when using Fancy Warp Menu (@Su386yt)
- Fixed random 500 errors by transitioning to Partly Sane Cloud and away from Polyfrost Ursa Major (@Su386yt)
- Fixed Party Manager and Skill Upgrade Recommender not connection to server (@Su386yt)
- Fixed Party Manager not loading players without their API turned on (@Su386yt)

## Technical Changes
- Created PSS Event Manager
- Converted the following classes to Kotlin: ``OneConfigScreen``, ``ThemeManager``, ``Drop``, ``DropBannerRenderer``, ``SkyblockDataManager``, ``SkyblockPlayer``, ``SkyblockItem``, and ``SkyblockSkill``
- Refactored cooldown related classes (@Su386yt)
- Abstracted requests class (@Su386yt)
- Transitioned away from NEU API, and Polyfrost Ursa Major API in favor of Partly Sane Cloud (@Su386yt)

## New Contributors
* @DerGruenkohl made their first contribution in #350

**Full Changelog**: https://github.com/PartlySaneStudios/partly-sane-skies/compare/beta-v0.5.9...beta-v0.6