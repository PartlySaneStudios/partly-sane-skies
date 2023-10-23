# Beta v0.4.1

## New
- Join Hypixel Button on main menu (@j10a1n15)
- Visitor's Logbook Stats (@RayDeeUx)
  ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/b26b9d39-a77e-4357-bcac-54ab33e665a2)
- End of Farm Notifier Refined (@j10a1n15)

## Changes
- Concise dogfood message (@RayDeeUx )

## Bug Fixes
- Fixed performance issue with banner messages (@j10a1n15)

## Legacy Version Update (@Su386yt)

We have updated the legacy (non OneConfig) version from v0.2.1 to the latest version. This should fix a ton of crashes and bugs, as well as add a ton of new features. However, we do highly recommend using the beta version. We will not be updating the legacy version as often. Quick run down of change logs:

```diff
v0.3
+ Pet for Minion Information Display
+ Best Minion Profit Calculator
+ Block right clicks when holding mathematical hoes
+ Custom Themes
= Dynamically Adjusting Composter Items
= Better number formatting
= Switched from Skycrypt to Hypixel API for all data
= Added colour to the Party Manager Menu
= Fixed all typos (I think)
= Fixed 500 error during Party Manager
= Fixed being unable to delete the last item from the list in FarmNotifier
= Fixed the mod being completely unplayable in India and North Korea
= Fixed lack of colour in the "buy it now details" section of the custom AH menu
= Fixed Inflation and Item Markup Display Freaking Out When Lowest Bin Can't Be Found
= Fixed When buying from the bits shop, bits side bar disappears when it says +800
= Fixed the dungeon score printing twice
= Fixed When the string ends with a & (colour code symbol), it gives a IndexOutOfRange crashing game
= Fixed Watcher ready banner and probably worm warning lasting forever on screen
= Fixed If player has items that were not arrows in quiver, party manager would not load

v0.3.1
+ Added ``/allowhoerightclick`` to enable hoes right clicking for a short period of time
+ Added the PSS logo to the OneConfig menu
= /pss will now open the config menu as well as printing to help command (request from narct)
= /pss, and /pssh will now accurately display the complete list of commands automatically
= Added option to manage the End of Farm Notifier chime cooldown
= Remade the command system so it is easier and simpler to add more commands
= Fixed more typos
= Fixed NullPointerException and crash when item had incorrect item data
= Fixed the composter that Hypixel broke in the latest update
= Fixed the bit shop not showing anything when the request speed was set too low
= Fixed issue with perm party manager where the data could become corrupted, preventing game from starting up

v0.3.2
+ End of Farm Region Revamp
+ Added a Wordeditor for ingame chat
+ Added /crepes
+ Added /pssversion
= Discord link more consistent
= Fixed the path to the keybind config
= Added support for OneColor in GUI themes
= Added ability to only allow hoe rightclicks once
= Added chroma support to custom main menu
= Fixed /skillup having two aliases
= Fixed cooldowns to banner times
= Fixed bug where chat alerts and chat colours weren't compatible
= Fix dungeon joining buttons not working with the skyblock dungeons update

v0.3.3
= Fixed major performance issue

v0.4
+ Mining Events Reminder
+ OwO Wanguage
+ Added Pickaxe ability Reminder
+ Added required secrets notifier
+ Custom Auction House Menu Revamp
= Converted all systems to the new banner renderer
= Converted most code to use "§", and changed color coordination
= Fixed incorrect capitalisation of SkyBlock
= Fixed Auction House crashing
= Fixed banners not having a shadow or fading
= Fixed crashes when joining certain islands

v0.4.1
+ Join Hypixel Button on main menu 
+ Visitor's Logbook Stats
+ End of Farm Notifier Refined
= Concise dogfood message
= Fixed performance issue with banner messages 
```

**Full Changelog**: https://github.com/PartlySaneStudios/partly-sane-skies/compare/beta-v0.4...beta-v0.4.1