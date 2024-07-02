# Beta v0.3

Hey all!
We just released a new update to Partly Sane Skies with a lot of bug fixes, minor changes, and a few new features. Thank you to everyone who contributed to this update, and a massive thank you to @Wyvest and the @Polyfrost team for supplying us with the servers so that we can finally fix the infamous 500 error.

Lastly, last version was the last version that supported Vigilance, so you will now be required to use OneConfig in order to use this update.

## What's Changed
### New
- Pet for Minion Information Display
  When opening the minion, your current pet selected will appear, along with the pet that you have set as favourite.
  ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/f206cdb6-9ee0-4161-91b4-be257458f8da)

- Best Minion Profit Calculator
  Using real time data, the mod will now display which configurations will result in the most profitable minions.
  ![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/03a27491-7644-4d81-a471-e0d3b274922c)

- Block right clicks when holding mathematical hoes
  When you are holding a mathematical hoe, right clicks will be cancelled to prevent accidentally opening the recipes menu.

- Custom Themes
  You should now be able to use a variety of different themes, including new dark themes, very dark themes, colourful themes, and controversially, light themes. You can also choose your own accent colour, and create your own themes in the OneConfig menu under the theme section

![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/eb057d45-6ee4-41e2-a971-1b8d9c26b5be)
![image](https://github.com/PartlySaneStudios/partly-sane-skies/assets/83100266/eea5f88b-9dcc-4958-bd1d-024471952f59)


### Changes
- Dynamically Adjusting Composter Items
  The amount of items for composter profit will now adjust with the amount of items filled, and composter upgrades.
- Better number formatting
- Switched from Skycrypt to Hypixel API for all data
- Added colour to the Party Manager Menu
- Fixed all typos (I think)

### Bugs Fixes
- **__Fixed 500 error during Party Manager__**
    - Using the new system developed in part by the @NotEnoughUpdates team, and developed and hosted by @Wyvest, and the @Polyfrost team.
    - You should now be able to set your Time Between Request setting in OneConfig to between 0.5 and 1.5 seconds.

- Fixed being unable to delete the last item from the list in FarmNotifier
- Fixed the mod being completely unplayable in India and North Korea
- Fixed lack of colour in the "buy it now details" section of the custom AH menu
- Fixed Inflation and Item Markup Display Freaking Out When Lowest Bin Can't Be Found
- Fixed When buying from the bits shop, bits side bar disappears when it says +800
- Fixed the dungeon score printing twice
- Fixed When the string ends with a & (colour code symbol), it gives a IndexOutOfRange crashing game
- Fixed Watcher ready banner and probably worm warning lasting forever on screen
- Fixed If player has items that were not arrows in quiver, party manager would not load


## What's Changed
* Fixed bug with pet alert command by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/135
* Fixed the incorrect name for pet alert by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/136
* Update ChatAlertsCommand.java by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/137
* Changed from LGPL 3 to BSD 3 by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/139
* Created CREDITS by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/140
* Renamed credits to a .md file by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/141
* fixed developer name by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/142
* Update PartlySaneSkies.java by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/143
* Fix discord and help commands [hopefully] by @AfkUserMC in https://github.com/PartlySaneStudios/partly-sane-skies/pull/138
* Update README.md by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/144
* Changed config_menu image by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/145
* Remove unnecessary ' in "Commands" by @AfkUserMC in https://github.com/PartlySaneStudios/partly-sane-skies/pull/146
* Fix format for /chal command in ReadMe by @AfkUserMC in https://github.com/PartlySaneStudios/partly-sane-skies/pull/147
* New gradle toolkit by @koxx12-dev in https://github.com/PartlySaneStudios/partly-sane-skies/pull/148
* Fixed build action by @koxx12-dev in https://github.com/PartlySaneStudios/partly-sane-skies/pull/149
* Fixing things that probably aren't broken: Recreated by request by @AfkUserMC in https://github.com/PartlySaneStudios/partly-sane-skies/pull/151
* Fixed minor spelling mistake in Farmnotifier by @j10a1n15 in https://github.com/PartlySaneStudios/partly-sane-skies/pull/153
* A billion fixes by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/155
* Updated the commands section and added commands.md by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/157
* Added //fn & Colored Run Numbers by @j10a1n15 in https://github.com/PartlySaneStudios/partly-sane-skies/pull/156
* Integers with formatted numbers no longer say 1.00, instead say 1 by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/158
* Dev Environment Crash by @hannibal002 in https://github.com/PartlySaneStudios/partly-sane-skies/pull/162
* Merge pull request #5 from PartlySaneStudios/main by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/163
* Added ability to create dogfood versions without it yelling at you fo… by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/164
* Fixed the dogfood version tester by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/165
* Fixed dungeons score review triggering twice by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/166
* Fixed (Bug) When buying from the bits shop, bits side bar disappears … by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/167
* Fixed AH Market details bugs by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/168
* Pet alert changes and new feature by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/169
* Pet alert crash and dynamically adjusting composter items by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/170
* Changed from Skycrypt to Hypixel API for bazaar data, and made dynami… by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/171
* Added blocking mathematical hoes by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/172
* Created new skyblock data management by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/173
* Updated README and CREDITS by @j10a1n15 in https://github.com/PartlySaneStudios/partly-sane-skies/pull/174
* Visable > visible fix by @j10a1n15 in https://github.com/PartlySaneStudios/partly-sane-skies/pull/175
* Fiexd all the typos (I think) by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/176
* Added PolyfrostUrsaMajor system instead of API keys by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/178
* Custom themes by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/179
* Updated version, changed Ursa Major to Ursa Minor, and added license … by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/180
* Fixed if a file was corrupted, skill data would not load, thus Party Manager was unable to load by @Su386yt in https://github.com/PartlySaneStudios/partly-sane-skies/pull/181

## New Contributors
* @AfkUserMC made their first contribution in https://github.com/PartlySaneStudios/partly-sane-skies/pull/138
* @koxx12-dev made their first contribution in https://github.com/PartlySaneStudios/partly-sane-skies/pull/148
* @j10a1n15 made their first contribution in https://github.com/PartlySaneStudios/partly-sane-skies/pull/153
* @hannibal002 made their first contribution in https://github.com/PartlySaneStudios/partly-sane-skies/pull/162

Once again, a massive thank you to @Wyvest and the @Polyfrost team, along with everyone else.

**Full Changelog**: https://github.com/PartlySaneStudios/partly-sane-skies/compare/beta-v0.2.1...beta-v0.3