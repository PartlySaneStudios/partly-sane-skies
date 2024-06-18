package me.partlysanestudios.partlysaneskies.features.gui.hud.rngdropbanner

import cc.polyfrost.oneconfig.utils.gui.GuiUtils
import me.partlysanestudios.partlysaneskies.commands.PSSCommand

object RareDropGUIManager {

    fun registerCommand() {
        PSSCommand("raredrop")
            .addAlias("rd")
            .setDescription("Opens the Rare Drop GUI")
            .setRunnable { _, _ ->
                openGui()
            }
            .register()
    }

    private fun openGui() {
        RareDropGUI.update()
        GuiUtils.displayScreen(RareDropGUI)
    }

    val presets = listOf(
        RareDropPreset(
            "Dungeons",
            listOf(
                "Conjuring",
                "Silent Death",
                "Dreadlord Sword",
                "Zombie Soldier Cutlass",
                "Earth Shard",
                "Zombie Commander Whip",
                "Zombie Knight Sword",
                "Soulstealer Bow",
                "Sniper Bow",
                "Machine Gun Shortbow",
                "Bouncy Helmet",
                "Bouncy Chestplate",
                "Bouncy Leggings",
                "Bouncy Boots",
                "Heavy Helmet",
                "Heavy Chestplate",
                "Heavy Leggings",
                "Heavy Boots",
                "Rotten Helmet",
                "Rotten Chestplate",
                "Rotten Leggings",
                "Rotten Boots",
                "Sniper Helmet",
                "Skeleton Grunt Helmet",
                "Skeleton Grunt Chestplate",
                "Skeleton Grunt Leggings",
                "Skeleton Grunt Boots",
                "Skeleton Lord Helmet",
                "Skeleton Lord Chestplate",
                "Skeleton Lord Leggings",
                "Skeleton Lord Boots",
                "Skeleton Master Helmet",
                "Skeleton Master Chestplate",
                "Skeleton Master Leggings",
                "Skeleton Master Boots",
                "Skeleton Soldier Helmet",
                "Skeleton Soldier Chestplate",
                "Skeleton Soldier Leggings",
                "Skeleton Soldier Boots",
                "Skeletor Helmet",
                "Skeletor Chestplate",
                "Skeletor Leggings",
                "Skeletor Boots",
                "Super Heavy Helmet",
                "Super Heavy Chestplate",
                "Super Heavy Leggings",
                "Super Heavy Boots",
                "Zombie Commander Helmet",
                "Zombie Commander Chestplate",
                "Zombie Commander Leggings",
                "Zombie Commander Boots",
                "Zombie Knight Helmet",
                "Zombie Knight Chestplate",
                "Zombie Knight Leggings",
                "Zombie Knight Boots",
                "Zombie Lord Helmet",
                "Zombie Lord Chestplate",
                "Zombie Lord Leggings",
                "Zombie Lord Boots",
                "Zombie Soldier Helmet",
                "Zombie Soldier Chestplate",
                "Zombie Soldier Leggings",
                "Zombie Soldier Boots",
            ),
        ),
        RareDropPreset(
            "Useless End Drops",
            listOf(
                "Ender Helmet",
                "Ender Chestplate",
                "Ender Leggings",
                "Ender Boots",
                "Ender Belt",
                "Ender Cloak",
                "Ender Gauntlet",
                "Ender Necklace",
                "Enchanted Ender Pearl",
                "End Stone Bow",
                "Ender Monocle",
                "Enchanted Eye of Ender",
                "Enchanted End Stone",
                "Enchanted Obsidian",
            )
        )
    )
}