package me.partlysanestudios.partlysaneskies.features.mining.events

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config

enum class MiningEvent(
    val event: String,
    val color: String,
    val triggeredEvent: (String) -> Boolean,
    val config: () -> Boolean,
) {
    POWDER(
        "2x Powder",
        "§b⚑",
        { it.contains("§l2X POWDER STARTED!") },
        { config.mining2xPowderSound }
    ),
    POWDER20(
        "2x Powder Event in 20s!",
        "§b⚑",
        { it.contains("The §b2x Powder §eevent starts in §a20 §eseconds!") },
        { config.mining2xPowderSound && config.miningWarn20sBeforeEvent }
    ),
    WIND(
        "Gone with the Wind",
        "§9⚑",
        { it.contains("§r§9§lGONE WITH THE WIND STARTED!") },
        { config.miningGoneWithTheWindSound }
    ),
    WIND20(
        "Gone with the Wind Event in 20s!",
        "§9⚑",
        { it.contains("The §9Gone with the Wind §eevent starts in §a20 §eseconds!") },
        { config.miningGoneWithTheWindSound && config.miningWarn20sBeforeEvent }
    ),
    BETTER_TOGETHER(
        "Better Together",
        "§d⚑",
        { it.contains("§r§d§lBETTER TOGETHER STARTED!") },
        { config.miningBetterTogetherSound }
    ),
    BETTER_TOGETHER20(
        "Better Together Event in 20s!",
        "§d⚑",
        { it.contains("The §dBetter Together §eevent starts in §a20 §eseconds!") },
        { config.miningBetterTogetherSound && config.miningWarn20sBeforeEvent }
    ),
    RAID(
        "Goblin Raid",
        "§c⚑",
        { it.contains("§r§c§lGOBLIN RAID STARTED!") },
        { config.miningGoblinRaidSound }
    ),
    RAID20(
        "Goblin Raid Event in 20s!",
        "§c⚑",
        { it.contains("The §cGoblin Raid §eevent starts in §a20 §eseconds!") },
        { config.miningGoblinRaidSound && config.miningWarn20sBeforeEvent }
    ),
    RAFFLE(
        "Raffle",
        "§6⚑",
        { it.contains("§r§6§lRAFFLE STARTED!") },
        { config.miningRaffleSound }
    ),
    RAFFLE20(
        "Raffle Event in 20s!",
        "§6⚑",
        { it.contains("The §6Raffle §eevent starts in §a20 §eseconds!") },
        { config.miningRaffleSound && config.miningWarn20sBeforeEvent }
    ),
    GOURMAND(
        "Mithril Gourmand",
        "§b⚑",
        { it.contains("§r§b§lMITHRIL GOURMAND STARTED!") },
        { config.miningMithrilGourmandSound }
    ),
    GOURMAND20(
        "Mithril Gourmand Event in 20s!",
        "§b⚑",
        { it.contains("The §bMithril Gourmand §eevent starts in §a20 §eseconds!") },
        { config.miningMithrilGourmandSound && config.miningWarn20sBeforeEvent }
    ),

    POWDER_GHAST(
        "Powder Ghast",
        "§r§6",
        { it.contains("§r§6§lPOWDER GHAST!") },
        { config.miningPowderGhastSound }
    ),
    FALLEN_STAR(
        "Fallen Star",
        "§r§5",
        { it.contains("§r§5§l✯ §r§eA §r§5Fallen Star §r§ehas crashed at ") },
        { config.miningFallenStarSound }
    )
}

/* ALL THE MINING EVENTS RELATED MESSAGES

    **MAJOR EVENTS**

    2x POWDER
     §b⚑ §eThe §b2x Powder §eevent starts in §a20 §eseconds!
     §eThis is a passive event! §bIt's happening everywhere in the §bCrystal Hollows!§r

    §r§r§r                          §r§b§l2X POWDER STARTED!§r


    WIND
     §9⚑ §eThe §9Gone with the Wind §eevent starts in §a20 §eseconds!
     §eThis is a passive event! §bIt's happening everywhere in the §bCrystal Hollows!§r

    §r§r§r                   §r§9§lGONE WITH THE WIND STARTED!§r


    BETTER TOGETHER
     §d⚑ §eThe §dBetter Together §eevent starts in §a20 §eseconds!
     §eThis is a passive event! §bIt's happening everywhere in the §bCrystal Hollows!§r

    §r§r§r                    §r§d§lBETTER TOGETHER STARTED!§r


    RAID
     §c⚑ §eThe §cGoblin Raid §eevent starts in §a20 §eseconds!
     §aClick here §eto teleport to §bGarry §eand prepare!§r

    §r§r§r                        §r§c§lGOBLIN RAID STARTED!§r


    RAFFLE
     §6⚑ §eThe §6Raffle §eevent starts in §a20 §eseconds!
     §aClick here §eto teleport to §bGarry §eand prepare!§r

     §r§r§r                            §r§6§lRAFFLE STARTED!§r


    GOURMAND
     §b⚑ §eThe §bMithril Gourmand §eevent starts in §a20 §eseconds!
     §aClick here §eto teleport to §bGarry §eand prepare!§r

    §r§r§r                    §r§b§lMITHRIL GOURMAND STARTED!§r


    **MINOR EVENTS**

    POWDER GHAST
    §r§6The sound of pickaxes clashing against the rock has attracted the attention of the §r§6§lPOWDER GHAST!§r
    §r§eFind the §r§6Powder Ghast§r§e near the §r§bCliffside Veins§r§e!§r


    FALLEN STAR
    §r§5§l✯ §r§eA §r§5Fallen Star §r§ehas crashed at §r§bRoyal Mines§r§e! Nearby ore and Powder drops are amplified!§r

 */