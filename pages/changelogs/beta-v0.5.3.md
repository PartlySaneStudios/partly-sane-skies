# Beta v0.5.3

# New
- Treecapitator cooldown display (@Su386yt)
  - [video](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/69fdead9-a682-4fc9-9830-496443c162e5)
  - ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/601f2344-ed39-47c9-bd28-e6da2c09a639)
  - A visually pleasing treecapitator cooldown that takes inspiration from 1.9+ attack indicator to display the time remaining on the treecapitator cooldown (more cooldowns will probably be on the way I just need ideas)
  - Monkey Pet support
  - Rounded corners
  - No enchantment glint
  - 2.7/5 stars on yelp
- Dungeon Snitcher (@Su386yt)
  - ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/5e1fa3f2-699d-4812-b757-867bcd9f4da7)
  - Tired of monkes slacking in dungeon but too socially awkward to call them out? Well the PSS dungeon slacker can do it for you. With the included prefix, you can even use the excuse "sorry its automatic"
- Added option to hide up-to-date mods in the mod checker (@j10a1n15)
- Revamped debug mode (@Su386yt)
  - ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/6a602ffb-0a68-45db-acfa-6f8f82aad5e0)
  - Literally only nerds care about this, but it should be more clear now what Debug mode does
- Added **Pre-release** update channel (@Su386yt)
  - A bit of an experiment, but PSS will now be releasing pre-release builds. They will be accessible through Partly Sane Skies' pre-release channel, and through SkyClient's beta updater
  - Partly Sane Skies: Do ``/pssc`` and switch ``Update Channel`` to ``Pre-release``. You should also turn on ``Use Beta Versions`` in the mod checker
  - SkyClient: Do ``/scu`` and turn on ``Enable Skyclient Beta``
- Pet alert now displays the pet level and rarity (@Su386yt)
  - ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/1627b47d-1f66-4c08-bb91-35e42cf7d8f4)



# Changes
- Pet Alert will now work even when pets are hidden (@Su386yt)
- AutoGG strings will now restart when OneConfig mysteriously decides to delete them (@Su386yt)
- Mods that are in a different channel than yours (beta/normal) will be marked as out of date instead of unknown (@Su386yt)
- Fixed typos, but none of them were public (@j10a1n15)
- Healer Alert no longer blinds your entire screen (@j10a1n15)
- Moved the dungeons category above the mining category in the config (@Su386yt)

# Bug Fixes
- Composter was fixed again with Hypixel's latest update (it uses regex now) (@Su386yt)

# Technical Changes
- ``Prank`` class was moved from ``me.partlysanestudios.partlysaneskies`` to ``me.partlysanestudios.partlysaneskies.features.sound`` where it belongs (@Su386yt)
- Created pet data caching system (@Su386yt)
- ``BannerRenderer`` class was moved from ``me.partlysanestudios.partlysaneskies.gui`` to ``me.partlysanestudios.partlysaneskies.gui.hud`` where it belongs (@Su386yt)
- ``PSSItemRender`` now supports sizes in pixels instead of just scale (@Su386yt)
- ``Prank``, and ``RefreshKeybinds`` classes were switched to objects; ``LocationBannerDisplay`` functions were switched to all static (@Su386yt)
