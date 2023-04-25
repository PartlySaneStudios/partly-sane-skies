# Commands

<!-- /pss command -->
<details>
  <summary> <code> /pss </code> </summary>

## ``/pss`` 

### Aliases
These commands also work instead of ``/pss``:

``/helpss``

``/psshelp``

### Description
A helpful command for information on Partly Sane Skies.

### Usage

``/pss``: Displays a help message informing you of the uses of all commands, along with information about the mod.

``/pss <conf/c/config>``: Opens the configuration GUI. (Alias for ``/pssconfig``)

</details>

<!-- /pssconfig command -->
<details>
  <summary> <code> /pssconfig </code> </summary>

## ``/pssconfig`` 

### Aliases
These commands also work instead of ``/pssconfig``:

``/pssconf``

``/pssc``

Along with the following aliases under the ``/pss`` command:

``/pss config``

``/pss c``

``/pss conf``

### Description
Opens the configuration menu to edit all settings inside Partly Sane Skies.

### Usage

``/pssconfig``: Opens the configuration menu. You can also use the keybind, which by default is ``F7``.

</details>

<!-- /pssdiscord command -->
<details>
  <summary> <code> /pssdiscord </code> </summary>

## ``/pssdiscord`` 

### Aliases

These commands also work instead of ``/pssdiscord``:

``/pssdisc``

``/psdisc``

### Description
Sends a link to join the discord in chat.

### Usage

``/pssdiscord``: Sends the link to join the discord in chat.

</details>

<!-- /skillup command -->
<details>
  <summary> <code> /skillup </code> </summary>

## ``/skillup`` 

### Aliases

These commands also work instead of ``/skillup``:

``/skillu``

``/su``

### Description
Gives you recomendations as to the most important skills to upgrade.

### Usage

``/skillup [username]``: Recommends the most important skills to upgrade for specific ``username``.

``/skillup``: Recommends the most important skills to upgrade for the player running the command.

</details>


<!-- /partymanager command -->
<details>
  <summary> <code> /partymanager </code> </summary>

## ``/partymanager`` 

### Aliases

These commands also work instead of ``/partymanager``:

``/partym``

``/pm``

### Description
Opens the Party Manager menu. You can also use the keybind, which by default is ``M``.

### Usage

``/partymanager``: Opens the Party Manager Menu.

</details>

<!-- /permparty command -->
<details>
  <summary> <code> /permparty </code> </summary>

## ``/permparty`` 

### Aliases

These commands also work instead of ``/permparty``:

``/permp``

``/pp``

### Description
Allows you to save, party, add and remove players from a permanent dungeon party.

### Usage

``/permparty``: Sends information about the ``permparty`` command.

``/permparty [partyid]``: Parties everyone in the party with the id matching ``partyid``

``/permparty new [partyid]``: Creates a new party with the ID ``partyid``

``/permparty new [partyid] <partymembers>``: Creates a new party with the ID ``partyid`` and with the members ``partymembers``.

``/permparty delete [partyid]``: Deletes a perm party. (Note: There is no way to undo this action). 

``/permparty add [partyid] [partymember]``: Adds player ``partymember`` to the party ``partyid``.

``/permparty remove [partyid] [partymember]``: Removes the player ``partymember`` from the party ``partyid``.

</details>

<!-- /friendparty command -->
<details>
  <summary> <code> /friendparty </code> </summary>

## ``/friendparty`` 

### Aliases

These commands also work instead of ``/friendparty``:

``/fp``

``/pf``

### Description
Parties friends that are online in your friends list.

### Usage

``/friendparty``: Parties all friends that are online.

</details>

<!-- /chatalert command -->
<details>
  <summary> <code> /chatalerts </code> </summary>

## ``/chatalerts`` 

### Aliases

These commands also work instead of ``/chatalerts``:

``/chatalerts``

``/chal``

``/ca``

### Description
Allows certain messages to be highlighted in chat.

### Usage

``/chatalerts list``: Lists all chat alerts with their associated number.

``/chatalerts add [alert]``: Creates a new alert that will notify the user when ``alert`` is spotted in chat.

``/chatalerts remove [number]``: Removes the alert with the given ``number`` in the list. Numbers are show with ``/chatalerts list``.

</details>

<!-- //farmnotfier command -->
<details>
  <summary> <code> //farmnotfier </code> </summary>

## ``//farmnotfier`` 

### Aliases

These commands also work instead of ``//farmnotfier``:

``//farmnotif``

``//fn``

### Description
Allows you to create areas where you will be notfied you have reached the end of your farm.

### Usage

``//farmnotifier list``: Lists all of the locations where you have a farm notification, and their given number, and gives instructions on how to create a new farm notification.

``//farmnotifier remove [number]``: Removes a farm notifications from the list, given a valid ``number``. Numbers can be seen with ``/farmnotifier list``.

<br>

### To create a new farm notification:


1. Set the first corner of your notification area:

    ``//pos1``: Sets one corner of the farm notifcation (Like using WorldEdit).

2. Set the opposite corner of your notifcation area:

    ``//pos2``: Sets the opposite corner of the farm notification (Like using WorldEdit).

3. Create the notification area

    ``//create``: Creates a new farm notifier with the positions created with ``//pos1`` and ``//pos2``.

</details>

