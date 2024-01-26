package me.partlysanestudios.partlysaneskies.features.farming

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.events.minecraft.player.PlayerBreakBlockEvent

object WrongToolCropWarning {
    @SubscribePSSEvent
    fun onBlockBreak(event: PlayerBreakBlockEvent) {
        val block = minecraft.theWorld.getBlockState(event.point.toBlockPos())?.block
        val unlocalizedName = block?.unlocalizedName ?: ""

        val crop = getCrop(unlocalizedName)
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
            val requireReplenish: Boolean
        )
    }
}