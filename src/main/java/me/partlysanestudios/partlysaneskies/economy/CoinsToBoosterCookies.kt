package me.partlysanestudios.partlysaneskies.economy

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.system.requests.Request
import me.partlysanestudios.partlysaneskies.system.requests.RequestRunnable
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.command.ICommandSender
import kotlin.math.abs
import kotlin.math.ceil

class CoinsToBoosterCookieConversion {

    private val skyCryptProfileURL: String = ("https://sky.shiiyu.moe/api/v2/profile/")
    private val playerName: String by lazy { PartlySaneSkies.minecraft.thePlayer.name }
    private val boosterCookieItemId: String = "BOOSTER_COOKIE"
    private val boosterCookiePath: String = "constants/booster_cookie_price.json"

    private fun currencyFormatting(money: String): String {
        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
        val prefCurr: String = boosterCookieData["storehypixelnet"].asJsonObject.get("order").asJsonArray[PartlySaneSkies.config.prefCurr].asString
        val prefCurrSymbol: String = boosterCookieData["currencysymbols"].asJsonObject.get(prefCurr).asString
        val prefCurrSymbolPlacementPrecede: Boolean = boosterCookieData["currencysymbolprecedes"].asJsonObject.get(prefCurr).asBoolean
        if (!prefCurrSymbolPlacementPrecede) {
            return "$money$prefCurrSymbol"
        } else {
            return "$prefCurrSymbol$money"
        }
    }
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
            .setDescription("Converts a given amount of coins to the IRL cost of booster cookies in your selected currency via §9/pssc§b.")
            .setRunnable{ s: ICommandSender, a: Array<String> ->
                    if (a.size == 1 && a[0].toDoubleOrNull() != null) {
                        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
                        val prefCurr: String = boosterCookieData["storehypixelnet"].asJsonObject.get("order").asJsonArray[PartlySaneSkies.config.prefCurr].asString
                        val sSGPInPreferredCurrency = boosterCookieData["storehypixelnet"].asJsonObject.get(prefCurr).asDouble
                        val cookieValue: Double = ceil(convertToCookies(a[0].toDouble()))
                        val dollars: Double = (Math.round((cookieValue * sSGPInPreferredCurrency) * 100)) / 100.0
                        Utils.sendClientMessage("§6${StringUtils.formatNumber(abs(a[0].toDouble()))} coins §etoday is equivalent to §6${StringUtils.formatNumber(cookieValue.toLong().toDouble())} Booster Cookies, or §2${currencyFormatting(money = (StringUtils.formatNumber(dollars)))} §e(excluding sales taxes and other fees).")
                        Utils.sendClientMessage("§7(For reference, Booster Cookies today are worth ${StringUtils.formatNumber(ceil(SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()).toLong().toDouble())} coins. Note that the developers of Partly Sane Skies do not support IRL trading; the /c2c command is intended for educational purposes.)", true)
                        if (PartlySaneSkies.isDebugMode) Utils.sendClientMessage("§eIf the currency symbol doesn't look right, please report this to us via §9/discord §eso we can find a replacement symbol that Minecraft 1.8.9 can render.", true)
                    } else {
                        if (a.isEmpty()) {
                            Utils.sendClientMessage("§cUsage: /coins2cookies [amountInCoins]")
                            return@setRunnable
                        }
                        val theOtherArg = a[0].trim().lowercase()
                        val networthMode = (theOtherArg == "networth" || theOtherArg == "nw")
                        if (a.size == 2 && networthMode && a[1] != playerName) {
                            grabNetworth(username = a[1])
                        } else if (a.size == 1 && networthMode) {
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
        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
        val boosterCookieInGems: Double = boosterCookieData["ingame"].asJsonObject.get("onecookiegem").asInt.toDouble()
        val smallestSkyblockGemsPackage: Double = boosterCookieData["storehypixelnet"].asJsonObject.get("smallestgembundle").asInt.toDouble()
        if (boosterCookieBuyPrice != -1.0) { //math adapted from NEU: https://github.com/NotEnoughUpdates/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/profileviewer/BasicPage.java#L342
            return (((abs(coins) / boosterCookieBuyPrice) * boosterCookieInGems) / smallestSkyblockGemsPackage)
        }
        return -1.0
    }

    private fun grabNetworth(username: String = playerName) {
        RequestsManager.newRequest(
            Request((skyCryptProfileURL + username),
                RequestRunnable { r: Request ->
                    if (!r.hasSucceeded()) {
                        Utils.sendClientMessage("§ePSS is having trouble contacting SkyCrypt's API. Please try again; if this continues please report this to us via §9/discord§e.")
                        return@RequestRunnable
                    }
                    if (PartlySaneSkies.isDebugMode) Utils.sendClientMessage("§eSuccessfully contacted SkyCrypt's API.")
                    val jsonObject = (JsonParser().parse(r.getResponse()) as JsonObject)
                    val profileData = (jsonObject["profiles"] as JsonObject)
                    if (PartlySaneSkies.isDebugMode) Utils.sendClientMessage("§eProfiles obtained.")
                    if (profileData == null) {
                        Utils.sendClientMessage("§ePSS is having trouble accessing your profile data over SkyCrypt. Please try again; if this continues please report this to us via §9/discord§e.")
                        return@RequestRunnable
                    }
                    var networth: Double = -2.0
                    if (PartlySaneSkies.isDebugMode) Utils.sendClientMessage("§eFinding current profile...")
                    for (profile: Map.Entry<String,JsonElement> in profileData.entrySet())
                    {
                        val theProfile = profile.value.getAsJsonObject()
                        if (theProfile.get("current").asBoolean) {
                            networth = Utils.getJsonFromPath(theProfile,"data/networth/networth").asDouble
                            break
                        }
                    }
                    if (PartlySaneSkies.isDebugMode) Utils.sendClientMessage("§eCurrent profile and its networth found.")
                    if (networth >= 0.0) {
                        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
                        val prefCurr: String = boosterCookieData["storehypixelnet"].asJsonObject.get("order").asJsonArray[PartlySaneSkies.config.prefCurr].asString
                        val sSGPInPreferredCurrency = boosterCookieData["storehypixelnet"].asJsonObject.get(prefCurr).asDouble
                        val cookieValue: Double = ceil(convertToCookies(networth))
                        val dollars: Double = (Math.round((cookieValue * sSGPInPreferredCurrency) * 100)) / 100.0
                        var namePlaceholder = "$username's"
                        if (username == PartlySaneSkies.minecraft.thePlayer.name) namePlaceholder = "Your"
                        Utils.sendClientMessage("§e$namePlaceholder total networth (both soulbound and unsoulbound) of §6${StringUtils.formatNumber(networth.toLong().toDouble())} coins §etoday is equivalent to §6${StringUtils.formatNumber(cookieValue.toDouble())} Booster Cookies, or §2${currencyFormatting(money = (StringUtils.formatNumber(dollars)))} §e(excluding sales taxes and other fees).")
                        Utils.sendClientMessage("§7(For reference, Booster Cookies today are worth ${StringUtils.formatNumber(ceil(SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()).toLong().toDouble())} coins. Note that the developers of Partly Sane Skies do not support IRL trading; the /c2c command is intended for educational purposes.)", true)
                        Utils.sendClientMessage("§ePlease use NEU's §a/pv§e command for converting your unsoulbound networth.", true)
                        if (PartlySaneSkies.isDebugMode) Utils.sendClientMessage("§eIf the currency symbol doesn't look right, please report this to us via §9/discord §eso we can find a replacement symbol that Minecraft 1.8.9 can render.", true)
                    } else {
                        Utils.sendClientMessage("It seems like you don't have a networth at all...")
                    }
            })
        )
    }
//  below code preserved for archival purposes
//    private fun grabNetworthOld() {
//            Thread {
//                try {
//                    val username = PartlySaneSkies.minecraft.thePlayer.name
//                    val url = URL(skyCryptProfileURL + username)
//                    val http: HttpURLConnection = (url.openConnection() as HttpURLConnection)
//                    http.setDoOutput(true)
//                    http.setDoInput(true)
//                    http.setRequestMethod("GET")
//                    http.setRequestProperty("User-Agent", "PartlySaneSkies")
//                    http.setRequestProperty("Accept", "application/json")
//                    http.connect()
//                    val responseCode: Int = http.responseCode
//                    if (responseCode == 200) {
//                        try {
//                            val data = JsonParser().parse(String(IOUtils.toByteArray(http.inputStream), StandardCharsets.UTF_8)).asJsonObject
//                            if (!data.has("profiles")) {
//                                Utils.sendClientMessage("§ePSS is having trouble accessing your profile data over SkyCrypt. Please try again; if this continues please report this to us via §9/discord§e.")
//                                return@Thread
//                            }
//                            val profiles: JsonObject = data["profiles"].getAsJsonObject()
//                            val profileSet: Set<Map.Entry<String, JsonElement>> = profiles.entrySet()
//                            var currentSbProfileNetWorth: Double = -2.0;
//
//                            for (profile: Map.Entry<String,JsonElement> in profileSet)
//                            {
//                                if (profile.value.getAsJsonObject().get("current").asBoolean) {
//                                    currentSbProfileNetWorth = profile.value.asJsonObject.get("data").asJsonObject.get("networth").asJsonObject.get("networth").asDouble
//                                }
//                            }
//
//                            if (currentSbProfileNetWorth >= 0.0) {
//                                val cookieValue: Long = Math.round(convertToCookies(currentSbProfileNetWorth))
//                                val usDollars: Double = (Math.round((cookieValue * sSGPInPreferredCurrency) * 100)) / 100.0
//                                Utils.sendClientMessage("§eYour networth of §6${StringUtils.formatNumber(currentSbProfileNetWorth.toLong().toDouble())} coins §etoday is equivalent to §6${StringUtils.formatNumber(cookieValue.toDouble())} Booster Cookies, or §2$${StringUtils.formatNumber(usDollars)} USD §e(excluding sales taxes and other fees).")
//                            } else {
//                                Utils.sendClientMessage("It seems like you don't have a networth at all...")
//                            }
//                        } catch (e: Exception) {
//                            errorMessage(e)
//                            return@Thread
//                        }
//                    } else {
//                        Utils.sendClientMessage("§ePSS is having trouble contacting SkyCrypt's API. Please try again; if this continues please report this to us via §9/discord§e.")
//                        return@Thread
//                    }
//                } catch (e: Exception) {
//                    errorMessage(e)
//                    return@Thread
//                }
//            }.start()
//        }
//
//        private fun errorMessage(e: Exception) {
//            Utils.sendClientMessage("§4Something went wrong with checking your networth for this conversion. Please see your logs, and try to report this error to the admins via §9/discord§4.")
//            e.printStackTrace()
//        }
//
}