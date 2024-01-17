# Features

Features as of Beta v0.5.3 Prerelease 1

## General/Miscellaneous Features

General and Miscellaneous features.

### Mod Checker

When enabled, Mod Checker automatically will check your mods list for outdated, suspicious, or unknown mods. These mods may not be on the list of verified mods. If you believe a mod is being falsely flagged, please report it in the PSS discord (``/pssdiscord``)
![image](/images/mod_checker.png)

### Privacy

This toggle blocks other mods wanting to send data to their servers. Currently supported mods are Essentials & Dungeon Guide. If you know more mods that send that kind of data, feel free to report that in our discord server.

### Custom Main Menu

*For more information, [see the Custom Main Menu page](general/custom_main_menu.md)*

Upon start up of Minecraft, a nice, SkyBlock themed main menu will display.
There are various configurable options from SkyBlock-themed backgrounds,
along with an option to select a random background.

![Custom Main Menu](/images/custom_main_menu.png)

### Fun Fact on Main Menu
*For more information, [see the Custom Main Menu page](general/custom_main_menu.md)*
Displays a daily fun fact on the PSS custom main menu. 

### Skill Upgrade Recommendation
*For more information, [see the Skill Upgrade Recommendation page](general/skill_upgrade_recommendation.md)*

A helpful command to help recommend what skill you should upgrade less. Using ``/skillup <username>`` or ``/su <username>`` will provide a list of recommended skills to upgrade.

![Skill Upgrade Recommendation](/images/skill_upgrade.png)

### RNG Drop Banner and Sound
*For more information, [see the RNG Drop Banner](general/rng_drop_banner.md)*

When you get a rare drop, a Pumpkin Dicer or Melon Slicer like pop-up banner will appear, along with a sound, celebrating your drop.

![RNG Drop Banner](/images/rng_drop_banner.png)

## Custom Themes
*For more information, [see the Custom Themes page](general/theme.md)*

This mod supports a variety of different themes, including dark themes, very dark themes, colourful themes, and controversially, light themes. You can also choose your own accent colour, and create your own themes in the OneConfig menu under the theme section.

### Custom Sounds for SkyBlock
*For more information, [see the Custom Sounds page](general/custom_sounds.md)*

Instead of the normal noteblocks, there is now the option to use computer generated, discord sounds or even live instruments to play sounds such as SkyBlock music and sound effects.

### Location Banner
*For more information, [see the Location Banner page](general/location_banner.md)*

When switching to a new location region on SkyBlock, an MMO RPG style banner will appear, informing you that you have switched to a new region.

![Location Banner](/images/location_banner.png)

### Crêpes
*For more information, [see the Crêpes page](general/crêpes.md)*

Have you ever wanted to make crêpes, and don't have access to internet, but you do have access to SkyBlock? Well we've got you covered. Simply by doing /crêpes, you too can make crêpes from some random recipe we found on the internet.

# Chat Features

### Chat Alerts
*For more information, [see the Chat Alerts page](chat/chat_alerts.md)*

Using ``/ca``, you can add and remove specific phrases that will be highlighted when someone says them. Example: If you add the word "``flag``" (using ``/ca add flag``) to Chat Alerts, it will highlight the word and play a notification when someone says it.

![Chat Alerts](/images/chat_alerts.png)

### System Tray Notifications
Ever missed an important message or Dwarven Mines event? With the option to enable System Notifications, you will never miss them if you use the ChatAlert or Dwarven Events Feature
- Current support:
  - Chat Alerts
  - Mining Events

![System Tray](../../images/system_tray.png)

### Chat Color

Private messages and messages that are sent in the Party, Guild, Guild Officer, or SkyBlock Co-op channels will now have the color of the channel they are sent in.

![Chat Colors](/images/chat_color.png)

### OwO Wanguage

This feature basically changes all chat messages to use the owo language. Please don't use this; it literally breaks the chat completely. If you do, be warned.

![OwO Wanguage](/images/owowangauge.png)

### Incorrect Pet for Minion Alert

If you do not the right pet selected for leveling up minions, you will be warned so that you never lose any pet EXP because you still have your level 100 dungeon pet activated. An optional World War II (1939-1945) air raid siren can be activated.

### Pet for Minion Information Display

When opening the minion, your current pet selected will appear, along with the pet that you have set as favourite.

![Pet for Minion Information Display](/images/pet_minion_display.png)

### Non Chat Color

Messages sent by nons (non-ranked players) can be configured to have the same white as the rest of the chat.
This option is off by default but can be turned on in the PSS Config menu.

### Added a Wordeditor for ingame chat

Replace any word in chat to any other word, such as rp to reparty, Flagmaster to FlagHater or juju to nonbow

![Word Editor 1](/images/word_editor_1.png)


![Word Editor 2](/images/word_editor_2.png)


![Word Editor 3](/images/word_editor_3.png)


![Word Editor 4](/images/word_editor_4.png)


## Dungeon Features

### Party Manager

Manage your party and join dungeons with a helpful party manager, with features such as viewing stats,
kicking, party transfer buttons, etc. Included in the Party Manager GUI.
You can open it by typing the command ``/pm`` or by using the keybinding.
Default: ``M``

![Party Manager](/images/party_manager.png)

### Pearl Refill
Do you use pearls for your Dungeon runs and want to refill them backup to 16? With the Pearlrefill command you can automatically refill it back up to 16 from any other Stacksize, there is also a Keybind (Default P) and an option to enable auto refill at Dungeon start (use at your own risk)

### Watcher Ready, Message, Warning, Siren and Sound

When the watcher is done spawning mobs, a message will appear on your screen, along with an optional sound,
party message and World War II (1939-1945) air raid siren in case the previous two don't get your attention.

### Permanent Dungeon Party Selector

Automatically parties everyone in a permanent dungeon party. Using ``/pp`` or ``/permparty``, you have the option to create, party, delete, and add and remove members from your permanent party. You can even add different parties such as an f6Party or a jujuNonCarry party.

### Healer Alert

Sends a warning banner whenever a dungeon teammate is low on health. Togglable to change alert at either 50% health or 25% health. 

### Dungeon Player Rater

At the end of the dungeon, the mod will calculate what percentage of the dungeon was cleared by each player,
and how much they contributed, showing you how useful each player was.
In a perfectly balanced 5-player party, each player should get 20%.

### Dungeon Snitcher

Calls out players who may be slacking in dungeons, using the Dungeon Player Rater. Uses a configurable automatated message. 

![image](/images/dungeon_snitcher.png)

### Auto GG at End of Dungeon

Automatically sends a "GG" in the chat at the end of a dungeon run. 

### Required Secrets Notifier

Your teammates are throwing because they don't know you don't need 100% of secrets on lower floors? This feature sends a warning to yourself with a banner and sound (which can be an air raid siren) and a party message for your teammates.

![Required Secrets Notifier](/images/required_secrets.png)

## Farming

### End of Farm Notifier

Create a region where you will be notified when you reach it, using the same commands as world edit. See ``/farmnotif`` under the Commands section for more information.

![End of Farm Notifier](/images/end_of_farm.png)

### Block Right Clicks with Mathematical Hoes

When you are holding a mathematical hoe, right clicks will be cancelled to prevent accidentally opening the recipes menu.

### Top Crops to Compost

Shows you information about which crops are the best to use for the composter at the current moment in time.

![Top Crops to Compost](/images/crop_compost.png)

### Best Skymart Profit

Shows which items are the most profitable for the copper cost. (Which ones give the most coins per copper?)

![Skymart Profit](/images/skymart_profit.png)

### Visitors Logbook Stats
![Visitor's Logbook Stats](/images/visitor_logbook.png)

### Treecap Cooldown

Dispalys a cooldown bar inspired by the 1.9+ attack indicator. Has Monkey pet support.
![(Cooldown showcase)](/images/treecap_cooldown.mp4) 

## Mining Features

### Worm Warning

When you are mining in the Crystal Hollows and a worm or Scatha spawns, you get a message on screen, along with a sound, warning you that a worm has spawned.

### Mining Events Reminder

The mining events reminder has a selective list of all dwarven mines & crystal hollows events. These events can be enabled separately. You can be reminded with either a banner or a banner and sound. You can choose the banner's color and display time yourself. You can also get reminded 20 seconds before the event starts.
![Mining Events Reminder](/images/mining_events.png)

### Pickaxe Ability Reminder

Not always aware when the pickaxe cooldown is over? Not anymore! With the new pickaxe cooldown reminder, this won't happen anymore. Need a banner? We have that! Want color in it? It's already available! Sound? Of course. It also includes PTSD, the best feature of it all—the Air Raid Siren! You can also block right-clicks on your private island, so you'll never accidentally pickoboculus your island again.

![Pickaxe Ability Reminder](/images/pickaxe_ability.png)



## Economy

### No Cookie Warning

Never lose your coins to the void again! When the mod detects you do not have a cookie active, it will warn you to buy a new one. Optionally, it can warn you only if you have a lot of coins in your purse.

### Coins to Cookies Converter 

Using ``/cookies2coins``, you can convert a given amount of coins to an equivalent value in a real life currency of your choosing. 
![image](/images/cookies_to_coins.png)


### Enhanced Auction Menu and BIN Sniper

A brand-new auction house menu that gives you more information on prices, instant inflation, and mark up.
Using that information, the menu highlights BIN items that are significantly below their value (Default: 13% below).

![Custom Main Menu](/images/custom_ah.png)

### Best Minion Profit Calculator

Using real time data, the mod will now display which configurations will result in the most profitable minions.

![Best Minion Calculator](/images/best_minion_calculator.png)

### Visitor Trade Cost

Shows you information about the visitor trades.

![Custom Main Menu](/images/garden_trade_cost.png)


### Best Bit Shop Profit

A lot of people use the bits accumulated from booster cookies to convert to items to sell on the auction house. Instead of having to guess which items are the best and for what price, it will now recommend to you the items that sell for the most coins per bit.

![Bit Shop Profit](/images/bit_shop_profit.png)


## Shortcuts

### Open Wiki Keybinding

Using the keybinding, it will automatically look up the wiki article for the item you are hovering over.
(``NONE`` key by default)

### Pets Menu Keybinding

A keybinding shortcut to open the pet menu. Customisable in the vanilla options' menu. (``NONE`` key by default)

### Crafting Table Menu Keybinding

A keybinding shortcut to open the crafting table menu.
Customisable in the vanilla options' menu.
(``NONE`` key by default)

### Wardrobe Menu Keybinding

A keybinding shortcut to open the wardrobe menu. Customisable in the vanilla options' menu. (``NONE`` key by default)

### Storage Menu Keybinding

A keybinding shortcut to open the storage menu. Customisable in the vanilla options' menu. (``NONE`` key by default)

### Hoe Right Click

A Keybinding shortcut to allow Hoe Right Click for one time or for some minutes, depending on your setting. Customiseable in the vanilla options' menu. (``NONE`` by default)

### Party All Friends

A command to party all of your active friends. Using ``/fp``, it will party every online member on your friends' list.
