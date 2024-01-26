//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.farming

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.time
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent
import me.partlysanestudios.partlysaneskies.render.gui.hud.BannerRenderer.renderNewBanner
import me.partlysanestudios.partlysaneskies.render.gui.hud.PSSBanner
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getHypixelEnchants
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getCurrentlyHoldingItem
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation

object WrongToolCropWarning {
    private var lastWarnTime = -1L
    @SubscribePSSEvent
    fun onBlockBreak(event: PlayerBreakBlockEvent) {
        if (!config.wrongToolForCropEnabled) {
            return
        }
        if (onCooldown(lastWarnTime, (config.wrongToolForCropCooldown*1000).toLong())) {
            return
        }
        val block = minecraft.theWorld.getBlockState(event.point.toBlockPos())?.block
        val unlocalizedName = block?.unlocalizedName ?: ""

        val crop = getCrop(unlocalizedName) ?: return

        val id = getCurrentlyHoldingItem()?.getItemId() ?: ""
        val nbt = NBTTagCompound()
        getCurrentlyHoldingItem()?.writeToNBT(nbt)
        val minecraftName = nbt.getString("id") ?: ""
        val enchants = getCurrentlyHoldingItem()?.getHypixelEnchants() ?: HashMap()

        var validTool = if (config.mathematicalHoeValid && crop.mathematicalHoeIds.contains(id)) { // if mathematical hoes are valid and the tool is a valid math hoe
            true
        } else if (config.otherSkyblockToolsValid && crop.otherSkyblockHoes.contains(id)) { // if other skyblock tools are valid and the tool is a valid skyblock tool
            true
        } else if (config.vanillaToolsValid && crop.otherMinecraftHoes.contains(minecraftName)) { // if vanilla tools are valid and the tool is a valid vanilla tool
            true
        } else {
            false
        }

        if (config.requireReplenish && crop.requireReplenish && !(enchants.containsKey("replenish") && enchants["replenish"] != 0)) { // if the config setting is on, the crop requires replenish, the tool has replenish, and the replenish level is not equal to 0
            validTool = false
        }
        if (validTool) {
            return
        }

        lastWarnTime = time
        renderNewBanner(PSSBanner("Wrong Tool Used", (config.wrongToolForCropBannerTime * 1000).toLong()))

        minecraft.soundHandler
            .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "bell")))

        if (config.wrongToolForCropAirRaid) {
            minecraft.soundHandler
                .playSound(PositionedSoundRecord.create(ResourceLocation("partlysaneskies", "airraidsiren")))
        }
    }

    private fun getCrop(unlocalizedName: String): CropToolData.Crop? {
        return if (!CropToolData.jsonObject.has(unlocalizedName)) {
            null
        } else {
            CropToolData.serializeCrop(
                unlocalizedName,
                CropToolData.jsonObject[unlocalizedName]?.asJsonObject ?: JsonObject()
            )
        }
    }

    private fun getAllCrops(): List<CropToolData.Crop> {
        val list = ArrayList<CropToolData.Crop>()

        for (entry in CropToolData.jsonObject.entrySet()) {
            val crop = CropToolData.serializeCrop(entry?.key ?: "", entry?.value?.asJsonObject ?: JsonObject())
            list.add(crop)
        }

        return list
    }

    internal object CropToolData {
        var jsonObject = JsonObject()
        @SubscribePSSEvent
        fun loadData(event: LoadPublicDataEvent) {
            jsonObject = JsonParser().parse(PublicDataManager.getFile("constants/crop_tools.json")).asJsonObject ?: JsonObject()
        }

        fun serializeCrop(cropUnlocalizedName: String, cropObject: JsonObject): Crop {
            return Crop(
                unlocalizedName = cropUnlocalizedName,
                mathematicalHoeIds = cropObject["math"]?.asJsonArray?.map { it.asString } ?: ArrayList(),
                otherSkyblockHoes = cropObject["other_skyblock"]?.asJsonArray?.map { it.asString } ?: ArrayList(),
                otherMinecraftHoes = cropObject["other_minecraft"]?.asJsonArray?.map { it.asString } ?: ArrayList(),
                requireReplenish = cropObject["require_replenish"]?.asBoolean ?: false
            )
        }

        internal class Crop(
            val unlocalizedName: String,
            val mathematicalHoeIds: List<String>,
            val otherSkyblockHoes: List<String>,
            val otherMinecraftHoes: List<String>,
            val requireReplenish: Boolean, // TODO: Replenish detection

        )
    }
}