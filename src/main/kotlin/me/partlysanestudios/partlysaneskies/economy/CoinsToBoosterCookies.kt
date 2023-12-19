/*
 * A Kotlin class written by Erymanthus[#5074] | (u/)RayDeeUx
 * and Su386 for Su386 and FlagMaster's Partly Sane Skies mod.
 * See LICENSE for copyright and license notices.
 *
 * KOTLIN ON TOP BABYYYYYYYY
 *
 * Wikipedium — Today at 4:08 PM
 * Using booster cookies not irl trading lmao
 *
*/

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
import me.partlysanestudios.partlysaneskies.utils.ChatUtils
import me.partlysanestudios.partlysaneskies.utils.MathUtils.floor
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.getJsonFromPath
import net.minecraft.command.ICommandSender
import kotlin.math.abs
import kotlin.math.ceil

object CoinsToBoosterCookieConversion {

    private val skyCryptProfileURL: String = ("https://sky.shiiyu.moe/api/v2/profile/")
    private val playerName: String by lazy { PartlySaneSkies.minecraft.thePlayer.name }
    private val boosterCookieItemId: String = "BOOSTER_COOKIE"
    private val boosterCookiePath: String = "constants/booster_cookie_price.json"
    private val configCurr get(): Int = PartlySaneSkies.config.prefCurr
    private val orderOfCurrency = arrayOf("AUD", "BRL", "CAD", "DKK", "EUR", "KPW", "NOK", "NZD", "PLN", "GBP", "SEK", "USD")


    private fun currencyFormatting(money: String): String {
//        Formats the currency to be the right preferred symbol
        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
        val prefCurr: String = orderOfCurrency[configCurr]
        val prefCurrSymbol: String = boosterCookieData["currencysymbols"].asJsonObject.get(prefCurr).asString
        val prefCurrSymbolPlacementPrecede: Boolean = boosterCookieData["currencysymbolprecedes"].asJsonObject.get(prefCurr).asBoolean
        return if (!prefCurrSymbolPlacementPrecede) {
            "$money$prefCurrSymbol"
        } else {
            "$prefCurrSymbol$money"
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
            .setRunnable { s: ICommandSender, a: Array<String> ->
                ChatUtils.sendClientMessage("Loading...")

//                Creates a new thread so we don't pause the entirety of the game to perform a request that won't work because a game tick needs to pass to be able to run
                Thread() {
                    if (a.size == 1 && a[0].toDoubleOrNull() != null) {
//                        Gets the public data json
                        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
//                        Preferred currency
                        val prefCurr: String = orderOfCurrency[configCurr]
//                        The cost of the smallest skyblock gem package in preferred currency
                        val sSGPInPreferredCurrency = boosterCookieData["storehypixelnet"].asJsonObject.get(prefCurr).asDouble
//                        Gets the amount of cookies
                        val cookieQuantity = convertCoinsToBoosterCookies(a[0].toDouble())
//                        Gets the amount of gem packages
                        val gemPackages: Double = ceil(convertCoinsToGemPackages(a[0].toDouble()))
//                        Cost in irl money
                        val dollars: Double = (gemPackages * sSGPInPreferredCurrency).floor(2)

//                        Sends message
                        ChatUtils.sendClientMessage("§6${abs(a[0].toDouble()).formatNumber()} coins §etoday is equivalent to §6${cookieQuantity.round(3).formatNumber()} Booster Cookies, or §2${currencyFormatting(money = (dollars.formatNumber()))} §e(excluding sales taxes and other fees).")
                        ChatUtils.sendClientMessage("§7(For reference, Booster Cookies today are worth ${ceil(SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()).round(1).formatNumber()} coins. Note that the developers of Partly Sane Skies do not support IRL trading; the /c2c command is intended for educational purposes.)", true)
                        if (PartlySaneSkies.config.debugMode) { // Optional debug message
                            ChatUtils.sendClientMessage("§eIf the currency symbol doesn't look right, please report this to us via §9/discord §eso we can find a replacement symbol that Minecraft 1.8.9 can render.", true)
                        }
                    } else if (a.isEmpty() || a.size == 1) {
                        runNetworthToCoins(this.playerName)
                    } else if (a[0].contentEquals("networth", true) || a[0].contentEquals("nw", true)) {
                        if (a.size == 1) {
                            ChatUtils.sendClientMessage("Correct Usage: /coins2cookies networth {username}")
                            return@Thread
                        }
                        runNetworthToCoins(a[1])
                    } else {
                        ChatUtils.sendClientMessage("§cPlease enter a valid number for your §6coins to cookies §cconversion and try again.")
                        return@Thread
                    }
                }.start()
            }.register()
    }

    private fun convertCoinsToBoosterCookies(coins: Double): Double {
        //        Gets the value of one booster cookie
        val boosterCookieBuyPrice = SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()
        return abs(coins) / boosterCookieBuyPrice
    }


//    Returns the amount of gem packages a given number of coins is worth
//    Can be fractional
    private fun convertCoinsToGemPackages(coins: Double): Double {
//        Gets the value of one booster cookie
        val boosterCookieBuyPrice = SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()

//    Gets the data for booster cookies
        val boosterCookieDataJsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
//    Gets how many gems one booster cookie is worth
        val boosterCookieInGems = boosterCookieDataJsonObject.getJsonFromPath("ingame/onecookiegem")?.asDouble ?: 325.0
//    Gets the samllest amount of
        val smallestSkyblockGemsPackage = boosterCookieDataJsonObject.getJsonFromPath("storehypixelnet/smallestgembundle")?.asDouble ?: 675.0

        return ((convertCoinsToBoosterCookies(coins) * boosterCookieInGems) / smallestSkyblockGemsPackage) //math adapted from NEU: https://github.com/NotEnoughUpdates/NotEnoughUpdates/blob/master/src/main/java/io/github/moulberry/notenoughupdates/profileviewer/BasicPage.java#L342


    }

    private fun runNetworthToCoins(username: String = playerName) {
        RequestsManager.newRequest(
            Request((skyCryptProfileURL + username),
                RequestRunnable { r: Request ->
                    if (!r.hasSucceeded()) {
                        ChatUtils.sendClientMessage("§ePSS is having trouble contacting SkyCrypt's API. Please try again; if this continues please report this to us via §9/discord§e.")
                        return@RequestRunnable
                    }
                    if (PartlySaneSkies.config.debugMode) ChatUtils.sendClientMessage("§eSuccessfully contacted SkyCrypt's API.")
                    val jsonObject = (JsonParser().parse(r.getResponse()) as JsonObject)
                    val profileData = (jsonObject["profiles"] as JsonObject)
                    if (PartlySaneSkies.config.debugMode) ChatUtils.sendClientMessage("§eProfiles obtained.")
                    if (profileData == null) {
                        ChatUtils.sendClientMessage("§ePSS is having trouble accessing your profile data over SkyCrypt. Please try again; if this continues please report this to us via §9/discord§e.")
                        return@RequestRunnable
                    }
                    var networth: Double = -2.0
                    if (PartlySaneSkies.config.debugMode) ChatUtils.sendClientMessage("§eFinding current profile...")
                    for (profile: Map.Entry<String,JsonElement> in profileData.entrySet())
                    {
                        val theProfile = profile.value.getAsJsonObject()
                        if (theProfile.get("current").asBoolean) {
                            networth = theProfile.getJsonFromPath("data/networth/networth")?.asDouble ?: -1.0
                            break
                        }
                    }
                    if (PartlySaneSkies.config.debugMode) ChatUtils.sendClientMessage("§eCurrent profile and its networth found.")
                    if (networth >= 0.0) {
                        val boosterCookieData: JsonObject = JsonParser().parse(PublicDataManager.getFile(boosterCookiePath)).getAsJsonObject()
                        val prefCurr: String = orderOfCurrency[configCurr]
                        val sSGPInPreferredCurrency = boosterCookieData["storehypixelnet"].asJsonObject.get(prefCurr).asDouble
                        val cookieValue: Double = ceil(convertCoinsToGemPackages(networth))
                        val dollars: Double =(cookieValue * sSGPInPreferredCurrency).round(2)
                        var namePlaceholder = "$username's"
                        if (username == PartlySaneSkies.minecraft.thePlayer.name) namePlaceholder = "Your"
                        ChatUtils.sendClientMessage("§e$namePlaceholder total networth (both soulbound and unsoulbound) of §6${networth.round(2).formatNumber()} coins §etoday is equivalent to §6${cookieValue.formatNumber()} Booster Cookies, or §2${currencyFormatting(money = (dollars.formatNumber()))} §e(excluding sales taxes and other fees).")
                        ChatUtils.sendClientMessage("§7(For reference, Booster Cookies today are worth ${ceil(SkyblockDataManager.getItem(boosterCookieItemId).getBuyPrice()).round(2).formatNumber()} coins. Note that the developers of Partly Sane Skies do not support IRL trading; the /c2c command is intended for educational purposes.)", true)
                        ChatUtils.sendClientMessage("§ePlease use NEU's §a/pv§e command for converting your unsoulbound networth.", true)
                        if (PartlySaneSkies.config.debugMode) ChatUtils.sendClientMessage("§eIf the currency symbol doesn't look right, please report this to us via §9/discord §eso we can find a replacement symbol that Minecraft 1.8.9 can render.", true)
                    } else {
                        ChatUtils.sendClientMessage("It seems like you don't have a networth at all... (this might be a wrong username)")
                    }
            })
        )
    }
}