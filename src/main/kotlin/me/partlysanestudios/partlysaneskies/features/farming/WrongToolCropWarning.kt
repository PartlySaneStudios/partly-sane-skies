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
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getItemId
import me.partlysanestudios.partlysaneskies.utils.MathUtils.onCooldown
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getCurrentlyHoldingItem
import net.minecraft.client.audio.PositionedSoundRecord
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

        val id = getItemId(getCurrentlyHoldingItem())
        val minecraftName = getCurrentlyHoldingItem()?.serializeNBT()?.id ?: ""

        if (crop.mathematicalHoeIds.contains(id) && config.mathematicalHoeValid) {
            return
        } else if (crop.otherSkyblockHoes.contains(id) && config.otherSkyblockToolsValid) {
            return
        } else if (crop.otherMinecraftHoes.contains(minecraftName) && config.vanillaToolsValid) {
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