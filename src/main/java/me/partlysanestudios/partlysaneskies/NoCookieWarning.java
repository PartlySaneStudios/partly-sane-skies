//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import me.partlysanestudios.partlysaneskies.system.BannerRenderer;
import me.partlysanestudios.partlysaneskies.system.PSSBanner;
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.awt.*;
import java.lang.reflect.Field;

public class NoCookieWarning {
    private static final float TEXT_SCALE = 2.5f;

    private static String displayString = "";
    private static long lastWarnTime;

    public NoCookieWarning() {
        lastWarnTime = PartlySaneSkies.getTime();
    }

    public static IChatComponent getFooter() {
        GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList();
        Field footerField = ReflectionHelper.findField(GuiPlayerTabOverlay.class, "field_175255_h", "footer");

        IChatComponent footer;
        try {
            footer = (IChatComponent) footerField.get(tabList);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return footer;
    }

    // Returns 1 if it has a cookie, 0 if it doesn't and -1 if it cannot be
    // determined
    public static int hasBoosterCookie() {
        for (IChatComponent chatComponent : getFooter().getSiblings()) {
            if (StringUtils.INSTANCE.removeColorCodes(chatComponent.getFormattedText()).toLowerCase()
                    .contains("not active! obtain booster cookies")) {
                return 0;
            }
        }
        if (getFooter().getSiblings().isEmpty())
            return -1;
        return 1;
    }

    public static boolean hasLotsOfCoins() {
        if (HypixelUtils.INSTANCE.getCoins() > PartlySaneSkies.config.maxWithoutCookie) {
            return true;
        } else {
            return false;
        }
    }

    public static void warn() {
        lastWarnTime = PartlySaneSkies.getTime();
        Color color = Color.red;
        displayString = "No Booster Cookie. You will lose your coins on death";

        BannerRenderer.INSTANCE.renderNewBanner(new PSSBanner(displayString, (long) (PartlySaneSkies.config.noCookieWarnTime * 1000), TEXT_SCALE, color));
        PartlySaneSkies.minecraft.getSoundHandler()
                .playSound(PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "bell")));
    }

    public static long getTimeSinceLastWarn() {
        return PartlySaneSkies.getTime() - lastWarnTime;
    }

    public static boolean checkExpire() {
        return getTimeSinceLastWarn() > PartlySaneSkies.config.noCookieWarnTime * 1000;
    }

    public static boolean checkWarnAgain() {
        if (getTimeSinceLastWarn() > PartlySaneSkies.config.noCookieWarnCooldown * 1000) {
            return true;
        }

        else {
            return false;
        }
    }

    @SubscribeEvent
    public void checkCoinsTick(ClientTickEvent event) {
        if (!HypixelUtils.INSTANCE.isSkyblock()) {
            return;
        }
        if (!PartlySaneSkies.config.noCookieWarning) {
            return;
        }

        if (hasBoosterCookie() == 1) {
            return;
        }

        if (checkExpire())
            displayString = "";

        if (!checkWarnAgain()) {
            return;
        }

        if (!hasLotsOfCoins()) {
            return;
        }

        warn();
        lastWarnTime = PartlySaneSkies.getTime();
    }

}
