package me.partlysanestudios.partlysaneskies.economy


import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.command.ICommandSender
import org.apache.commons.io.IOUtils
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import kotlin.math.abs
import kotlin.math.ceil

class CoinsToBoosterCookieConversion {

    private val skyCryptProfileURL = ("https://sky.shiiyu.moe/api/v2/profile/")
    private val boosterCookieItemId = "BOOSTER_COOKIE"
    private val boosterCookieInGems: Double = 325.0 //see "wiki.hypixel.net/Booster_Cookie"
    private val smallestSkyblockGemsPackage: Double = 675.0 //see "store.hypixel.net/category/skyblock-gems"
    private val sSGPInUnitedStatesDollars: Double = 4.99 //see "store.hypixel.net/category/skyblock-gems"

    fun registerCommand() {
        PSSCommand("coins2cookies")
            .addAlias("/coins2cookies")
            .addAlias("/coinstocookies")
            .addAlias("/pssco2icok")
            .addAlias("/coi2cok")
            .addAlias("/c2c")
            .addAlias("c2c")
            .addAlias("coi2cok")
            .addAlias("psscoi2cok")
            .addAlias("coinstocookies")
            .setDescription("Converts a given amount of coins to the IRL cost of booster cookies.")
            .setRunnable{ s: ICommandSender, a: Array<String> ->
                    if (a.size == 1 && a[0].toDoubleOrNull() != null) {
                        val cookieValue: Double = ceil(convertToCookies(a[0].toDouble()))
                        val usDollars: Double = (Math.round((cookieValue * sSGPInUnitedStatesDollars) * 100)) / 100.0
                        Utils.sendClientMessage("§6${StringUtils.formatNumber(abs(a[0].toDouble()))} coins §etoday is equivalent to §6${StringUtils.formatNumber(cookieValue.toLong().toDouble())} Booster Cookies, or §2$${StringUtils.formatNumber(usDollars)} USD §e(excluding sales taxes and other fees).")
                    } else {
                        if (a.isEmpty()) {
                            Utils.sendClientMessage("§cUsage: /coins2cookies [amountInCoins]")
                            return@setRunnable
                        }
                        val theOtherArg = a[0].toString().trim().lowercase()
                        if (a.size == 1 && (theOtherArg == "networth" || theOtherArg == "nw")) {
                            grabNetworth()
                        } else {
                            Utils.sendClientMessage("§cPlease enter a valid number for your §6coins to cookies §cconversion and try again.")
                            return@setRunnable
                        }
                    }
                }
        .register()
    }

    private fun convertToCookies(coins: Double): Double {
        val boosterCookieBuyPrice = SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()
        if (boosterCookieBuyPrice != -1.0) { //math adapted from NEU: https://github.com/NotEnoughUpdates/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/profileviewer/BasicPage.java#L342
            Utils.sendClientMessage("§7(For reference, Booster Cookies today are worth ${StringUtils.formatNumber(ceil(boosterCookieBuyPrice).toLong().toDouble())} coins.)")
            return (((abs(coins) / boosterCookieBuyPrice) * boosterCookieInGems) / smallestSkyblockGemsPackage)
        }
        return -1.0
    }

//    private fun grabNetworth() {
//        val username = PartlySaneSkies.minecraft.thePlayer.name
//        val url = URL(skyCryptProfileURL + username)
//        RequestsManager.getRequest(Request(URL(skyCryptProfileURL + username), (r) -> {
//            if (!r.hasSucceded()) {
//                Utils.sendClientMessage("§ePSS is having trouble accessing your profile data over SkyCrypt. Please try again; if this continues please report this to us via §9/discord§e.")
//            }
//            val jsonObject = JsonParser.parse(r.getResponse)
//            val networthElement = Utils.getJsonFromPath("profiles/current/networth")
//            if (networthElement == null) {
//                Utils.sendClientMessage("§ePSS is having trouble accessing your profile data over SkyCrypt. Please try again; if this continues please report this to us via §9/discord§e.")
//            }
//
//            val networth = networthElement.asDouble
//
//            if (currentSbProfileNetWorth >= 0.0) {
//                val cookieValue: Long = Math.round(convertToCookies(networth))
//                val usDollars: Double = (Math.round((cookieValue * sSGPInUnitedStatesDollars) * 100)) / 100.0
//                Utils.sendClientMessage("§eYour networth of §6${StringUtils.formatNumber(netWorth.toLong().toDouble())} coins §etoday is equivalent to §6${StringUtils.formatNumber(cookieValue.toDouble())} Booster Cookies, or §2$${StringUtils.formatNumber(usDollars)} USD §e(excluding sales taxes and other fees).")
//            } else {
//                Utils.sendClientMessage("It seems like you don't have a networth at all...")
//            }
//        }))
//    }

    private fun grabNetworth() {
            Thread {
                try {
                    val username = PartlySaneSkies.minecraft.thePlayer.name
                    val url = URL(skyCryptProfileURL + username)
                    val http: HttpURLConnection = (url.openConnection() as HttpURLConnection)
                    http.setDoOutput(true)
                    http.setDoInput(true)
                    http.setRequestMethod("GET")
                    http.setRequestProperty("User-Agent", "PartlySaneSkies")
                    http.setRequestProperty("Accept", "application/json")
                    http.connect()
                    val responseCode: Int = http.responseCode
                    if (responseCode == 200) {
                        try {
                            val data = JsonParser().parse(String(IOUtils.toByteArray(http.inputStream), StandardCharsets.UTF_8)).asJsonObject
                            if (!data.has("profiles")) {
                                Utils.sendClientMessage("§ePSS is having trouble accessing your profile data over SkyCrypt. Please try again; if this continues please report this to us via §9/discord§e.")
                                return@Thread
                            }
                            val profiles: JsonObject = data["profiles"].getAsJsonObject()
                            val profileSet: Set<Map.Entry<String, JsonElement>> = profiles.entrySet()
                            var currentSbProfileNetWorth: Double = -2.0;

                            for (profile: Map.Entry<String,JsonElement> in profileSet)
                            {
                                if (profile.value.getAsJsonObject().get("current").asBoolean) {
                                    currentSbProfileNetWorth = profile.value.asJsonObject.get("data").asJsonObject.get("networth").asJsonObject.get("networth").asDouble
                                }
                            }

                            if (currentSbProfileNetWorth >= 0.0) {
                                val cookieValue: Long = Math.round(convertToCookies(currentSbProfileNetWorth))
                                val usDollars: Double = (Math.round((cookieValue * sSGPInUnitedStatesDollars) * 100)) / 100.0
                                Utils.sendClientMessage("§eYour networth of §6${StringUtils.formatNumber(currentSbProfileNetWorth.toLong().toDouble())} coins §etoday is equivalent to §6${StringUtils.formatNumber(cookieValue.toDouble())} Booster Cookies, or §2$${StringUtils.formatNumber(usDollars)} USD §e(excluding sales taxes and other fees).")
                            } else {
                                Utils.sendClientMessage("It seems like you don't have a networth at all...")
                            }
                        } catch (e: Exception) {
                            errorMessage(e)
                            return@Thread
                        }
                    } else {
                        Utils.sendClientMessage("§ePSS is having trouble contacting SkyCrypt's API. Please try again; if this continues please report this to us via §9/discord§e.")
                        return@Thread
                    }
                } catch (e: Exception) {
                    errorMessage(e)
                    return@Thread
                }
            }.start()
        }

        private fun errorMessage(e: Exception) {
            Utils.sendClientMessage("§4Something went wrong with checking your networth for this conversion. Please see your logs, and try to report this error to the admins via §9/discord§4.")
            e.printStackTrace()
        }
}