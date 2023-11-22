//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.garden.endoffarmnotifer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.system.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.system.PSSBanner
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommandRunnable
import me.partlysanestudios.partlysaneskies.utils.*
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes

import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.command.ICommandSender
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.max
import kotlin.math.min


class EndOfFarmNotifier {

    fun run() {
        if (!MathUtils.onCooldown(
                rangeToHighlightSetTime,
                (PartlySaneSkies.config.farmHightlightTime * 1000).toLong()
            )
        ) {
            rangeToHighlight = null
        }
        if (!playerInRange()) {
            displayString = ""
            return
        }
        if (MathUtils.onCooldown(
                lastChimeTime,
                (PartlySaneSkies.config.farmnotifierChimeTime * 1000).toLong()
            )
        ) {
            return
        }
        PartlySaneSkies.minecraft.soundHandler
            .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))
        displayString = "END OF FARM"
        lastChimeTime = PartlySaneSkies.getTime()
        renderNewBanner(
            PSSBanner(
                displayString,
                1000,
                TEXT_SCALE.toFloat(),
                Color.red
            )
        )
    }

    private fun createNewRange(name: String): Range3d? {
        if (selectedPos1.isEmpty()|| selectedPos2.isEmpty()) {
            return null
        }
        val smallY: Double =
            min(selectedPos1[1].toDouble(), selectedPos2[1].toDouble())
        val bigY: Double =
            max(selectedPos1[1].toDouble(), selectedPos2[1].toDouble())
        val range = Range3d(
            selectedPos1[0].toDouble(),
            smallY,
            selectedPos1[2].toDouble(),
            selectedPos2[0].toDouble(),
            bigY,
            selectedPos2[2].toDouble()
        )
        range.rangeName = name
        selectedPos1 = IntArray(0)
        selectedPos2 = IntArray(0)
        ranges.add(range)
        try {
            save()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return range
    }

    private fun playerInRange(): Boolean {
        if (PartlySaneSkies.minecraft.thePlayer == null) {
            return false
        }
        if (!inGarden()) {
            return false
        }
        if (PartlySaneSkies.minecraft.thePlayer.position == null) {
            return false
        }
        for (range in ranges) {
            if (range.isInRange(
                    PartlySaneSkies.minecraft.thePlayer.posX,
                    PartlySaneSkies.minecraft.thePlayer.posY,
                    PartlySaneSkies.minecraft.thePlayer.posZ
                )
            ) { //Pos with decimals
                return true
            }
        }
        return false
    }

    @SubscribeEvent
    fun onRightClick(event: PlayerInteractEvent) {
        if (!wandActive) {
            return
        }
        if (!inGarden()) {
            return
        }
        if (MinecraftUtils.getCurrentlyHoldingItem() == null) {
            return
        }
        if (!MinecraftUtils.getCurrentlyHoldingItem().hasDisplayName()) {
            return
        }
        if (!MinecraftUtils.getCurrentlyHoldingItem().getDisplayName().contains("SkyBlock Menu")) {
            return
        }
        if (event.pos == null) {
            return
        }
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return
        }

        // Gets the coordinates of the block
        val x = event.pos.x
        val y = event.pos.y
        val z = event.pos.z
        ChatUtils.sendClientMessage("§7Set §bpositon $pos§7 to §b($x, $y, $z)§7")
        if (pos == 1) {
            selectedPos1 = intArrayOf(x, y, z)
            pos = 2
        } else {
            selectedPos2 = intArrayOf(x, y, z)
            pos = 1
        }
        event.setCanceled(true)
    }

    /*
        EndOfFarmNotifier Save/Load
     */

    @Throws(IOException::class)
    fun save() {
        // Declares the file
        val file = File("./config/partly-sane-skies/endOfFarmRanges.json")
        file.createNewFile()
        // Creates a new Gson object to save the data
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .serializeSpecialFloatingPointValues()
            .create()
        // Saves teh data to the file
        val json = gson.toJson(ranges)
        val writer = FileWriter(file)
        writer.write(json)
        writer.close()
    }

    @Throws(IOException::class)
    fun load() {
        // Declares file location
        val file = File("./config/partly-sane-skies/endOfFarmRanges.json")
        file.setWritable(true)
        // If the file had to be created, fil it with an empty list to prevent null pointer error
        if (file.createNewFile()) {
            val writer = FileWriter(file)
            writer.write(Gson().toJson(java.util.ArrayList<Range3d>()))
            writer.close()
        }

        // Creates a new file reader
        val reader: Reader = Files.newBufferedReader(Paths.get(file.path))
        val array = JsonParser().parse(reader).getAsJsonArray()
        val list = java.util.ArrayList<Range3d>()
        for (en in array) {
            val range = en.getAsJsonObject()
            val smallCoordinate = IntArray(3)
            var i = 0
            for (element in range.getAsJsonArray("smallCoordinate")) {
                smallCoordinate[i] = element.asInt
                i++
            }
            val largeCoordinate = IntArray(3)
            i = 0
            for (element in range.getAsJsonArray("largeCoordinate")) {
                largeCoordinate[i] = element.asInt
                i++
            }
            val loadRange = Range3d(
                smallCoordinate[0].toDouble(),
                smallCoordinate[1].toDouble(),
                smallCoordinate[2].toDouble(),
                largeCoordinate[0].toDouble(),
                largeCoordinate[1].toDouble(),
                largeCoordinate[2].toDouble()
            )
            loadRange.rangeName = range["rangeName"].asString
            list.add(loadRange)
        }
        ranges = list
    }

    /*
        EndOfFarmNotifier Commands
     */
    fun registerPos1Command() {
        PSSCommand("/pos1")
            .setDescription("Sets one corner of the End of Farm Notifier: //pos1 [x] [y] [z]")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                if (a.size >= 3 && a[0].isNotEmpty() && a[1].isNotEmpty() && a[2].isNotEmpty()) {
                    try {
                        selectedPos1 = intArrayOf(a[0].toInt(), a[1].toInt(), a[2].toInt())
                        ChatUtils.sendClientMessage("§7Set §bpositon 1§7 to §b(" + selectedPos1[0] + ", " + selectedPos1[1] + ", " + selectedPos1[2] + ")§7")
                    } catch (e: NumberFormatException) {
                        ChatUtils.sendClientMessage("§cPlease enter a valid number and try again.")
                    }
                } else {
                    selectedPos1 = intArrayOf(s.position.x - 1, s.position.y, s.position.z - 1)
                    ChatUtils.sendClientMessage("§7Set §bpositon 1§7 to §b(" + selectedPos1[0] + ", " + selectedPos1[1] + ", " + selectedPos1[2] + ")§7")
                }
            }
            .register()
    }

    fun registerPos2Command() {
        PSSCommand("/pos2")
            .setDescription("Sets one corner of the End of Farm Notifier: //pos2 [x] [y] [z]")
            .setRunnable { s: ICommandSender, a: Array<String> ->
                if (a.size >= 3 && a[0].isNotEmpty() && a[1].isNotEmpty() && a[2].isNotEmpty()) {
                    try {
                        selectedPos2 = intArrayOf(a[0].toInt(), a[1].toInt(), a[2].toInt())
                        ChatUtils.sendClientMessage("§7Set §bpositon 2§7 to §b(" + selectedPos2[0] + ", " + selectedPos2[1] + ", " + selectedPos2[2] + ")§7")
                    } catch (e: java.lang.NumberFormatException) {
                        ChatUtils.sendClientMessage("§cPlease enter a valid number and try again.")
                    }
                } else {
                    selectedPos2 = intArrayOf(s.position.x - 1, s.position.y, s.position.z - 1)
                    ChatUtils.sendClientMessage("§7Set §bpositon 2§7 to §b(" + selectedPos2[0] + ", " + selectedPos2[1] + ", " + selectedPos2[2] + ")§7")
                }
            }
            .register()
    }

    fun registerCreateRangeCommand() {
        PSSCommand("/create")
            .setDescription("Creates the range from two positions: //create [name]")
            .setRunnable { s: ICommandSender?, a: Array<String?> ->
                var name: String? = ""
                if (a.isNotEmpty()) {
                    name = a[0]
                }
                if (name?.let { createNewRange(it) } == null) {
                    ChatUtils.sendClientMessage("§cUnable to create a new farm notifier. Make sure both §b//pos1§c and §b//pos2§c have been selected.")
                    return@setRunnable
                }
                ChatUtils.sendClientMessage("§aCreated new Farm Notifier.")
                selectedPos1 = IntArray(0)
                selectedPos2 = IntArray(0)
            }
            .register()
    }

    fun registerFarmNotifierCommand() {
        PSSCommand("/farmnotifier")
            .addAlias("/fn")
            .addAlias("/farmnotif")
            .addAlias("farmnotifier")
            .addAlias("fn")
            .addAlias("farmnotif")
            .setDescription("Operates the Farm Notifier feature: //fn [list/remove/highlight/show]")
            .setRunnable(PSSCommandRunnable { s: ICommandSender?, a: Array<String> ->
                if (a.isEmpty() || a[0].equals("list", ignoreCase = true)) {
                    ChatUtils.sendClientMessage("§7To create a new farm notifier, run §b//pos1§7 at one end of your selection, then run §b//pos2§7 at the other end of your farm. Once the area has been selected, run §b//create§7.\n\n§b//farmnotifier§7 command:\n§b//fn remove <index>:§7 remove a given index from the list.\n§b//fn list:§7 lists all of the farm notifiers and their indexes")
                    listRanges()
                    return@PSSCommandRunnable
                }
                if (a[0].equals("remove", ignoreCase = true)) {
                    if (a.size == 1) {
                        ChatUtils.sendClientMessage("§cError: Must provide an index to remove")
                        return@PSSCommandRunnable
                    }
                    val i: Int = try {
                        a[1].toInt()
                    } catch (e: java.lang.NumberFormatException) {
                        ChatUtils.sendClientMessage("§cPlease enter a valid index and try again.")
                        return@PSSCommandRunnable
                    }
                    if (i > ranges.size || i < 1) {
                        ChatUtils.sendClientMessage("§cPlease select a valid index and try again.")
                        return@PSSCommandRunnable
                    }
                    ChatUtils.sendClientMessage("§aRemoving: §b" + ranges[i - 1].toString())
                    ranges.removeAt(i - 1)

                    try {
                        save()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (a[0].equals("highlight", ignoreCase = true) || a[0].equals("show", ignoreCase = true)) {
                    if (a.size == 1) {
                        ChatUtils.sendClientMessage("§cError: Must provide an index to highlight")
                        return@PSSCommandRunnable
                    }

                    val i: Int = try {
                        a[1].toInt()
                    } catch (e: java.lang.NumberFormatException) {
                        ChatUtils.sendClientMessage("§cPlease enter a valid number index and try again.")
                        return@PSSCommandRunnable
                    }
                    if (i > ranges.size || i < 1) {
                        ChatUtils.sendClientMessage("§cPlease select a valid index and try again.")
                        return@PSSCommandRunnable
                    }
                    rangeToHighlight = ranges[i - 1]
                    rangeToHighlightSetTime = PartlySaneSkies.getTime()
                }
            })
            .register()
    }

    fun registerWandCommand() {
        PSSCommand("/wand")
            .addAlias("/psswand")
            .addAlias("/partlysaneskieswand")
            .addAlias("wand")
            .addAlias("psswand")
            .addAlias("partlysaneskieswand")
            .setDescription("Toggles the wand for the End of Farm Notifier: /wand")
            .setRunnable { s: ICommandSender?, a: Array<String?>? ->
                wandActive = !wandActive
                ChatUtils.sendClientMessage(
                    "§7The wand is now " +
                            if (wandActive) "§aactive§7. Use your §aSkyBlock menu §7to select a range using §bright click§7 as §3pos1§7 and then §3pos2§7. This is a §crepeating cycle§7. To disable the wand, run §b/wand§7 again."
                            else "§cinactive§7."
                )
            }
            .register()
    }


    /*
        EndOfFarmNotifier Utils
     */
    // Lists all the ranges to the chat
    private fun listRanges() {
        // Creates header message
        val message = StringBuilder(
            """
         §d§m-----------------------------------------------------
         §bEnd of Farms:
         §d§m-----------------------------------------------------
         
         """.trimIndent()
        )

        // Creates the index number on the left of the message
        var i = 1

        // For each alert, format it so its ##. [range]
        for (range in ranges) {
            message.append("§6")
                .append(i)
                .append("§7: ")
                .append(range.toString())
                .append("\n")
            i++
        }

        // Sends a message to the client
        ChatUtils.sendClientMessage(message.toString())
    }

    companion object {
        var ranges = ArrayList<Range3d>()
        lateinit var selectedPos1: IntArray
        lateinit var selectedPos2: IntArray
        private var lastChimeTime: Long = 0

        var color: Color? = null
        private var displayString = ""
        private const val TEXT_SCALE = 7

        var rangeToHighlight: Range3d? = null
        private var rangeToHighlightSetTime: Long = 0
        private var wandActive = false
        private var pos = 1 // 1 is pos1, 2 is pos2
        fun inGarden(): Boolean {
            var location = HypixelUtils.getRegionName()
            location = location.removeColorCodes()
            location = StringUtils.stripLeading(location)
            location = StringUtils.stripTrailing(location)
            location = location.replace(
                "\\P{Print}".toRegex(),
                ""
            )
            return location.startsWith("The Garden") || location.startsWith("Plot: ")
        }
    }
}