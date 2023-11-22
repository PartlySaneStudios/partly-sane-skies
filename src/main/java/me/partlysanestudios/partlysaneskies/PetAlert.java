//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.UIComponent;
import gg.essential.elementa.components.UIRoundedRectangle;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.components.Window;
import gg.essential.elementa.constraints.CenterConstraint;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import me.partlysanestudios.partlysaneskies.auctionhouse.menu.AuctionHouseGui;
import me.partlysanestudios.partlysaneskies.system.ThemeManager;
import me.partlysanestudios.partlysaneskies.system.commands.PSSCommand;
import me.partlysanestudios.partlysaneskies.utils.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetAlert {

    public static long lastMessageSendTime = 0;
    public static long lastSoundTime = 0;
    public static long lastMuteTime = 0;
    public static void runPetAlert() {
        if (!PartlySaneSkies.config.incorrectPetForMinionAlert) {
            return;
        }

        if (!isMinionGui()) {
            return;
        }
        Entity usersPet = getUsersPet(PartlySaneSkies.minecraft.thePlayer.getName());
        String petName = ("§8(Unknown)");
        if (usersPet != null) {
            petName = parsePetNameFromEntity(usersPet.getName());
        
        }
        
        String selectedPetName = PartlySaneSkies.config.selectedPet;
        
        if (petName.equalsIgnoreCase(selectedPetName)) {
            return;
        }

        if (MathUtils.INSTANCE.onCooldown(lastMuteTime, (long) (PartlySaneSkies.config.petAlertMuteTime * 60L * 1000L))) {
            return;
        }
        
        if (!MathUtils.INSTANCE.onCooldown(lastSoundTime, 750)) {
            PartlySaneSkies.minecraft.getSoundHandler()
                .playSound(
                    PositionedSoundRecord
                    .create(new ResourceLocation("partlysaneskies", "bell"))
                );
                if (PartlySaneSkies.config.incorrectPetForMinionAlertSiren) {
                    PartlySaneSkies.minecraft.getSoundHandler().playSound(
                            PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
                }
            lastSoundTime = PartlySaneSkies.getTime();
        }
        if (!MathUtils.INSTANCE.onCooldown(lastMessageSendTime,3000)) {
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + ("§cYOU CURRENTLY HAVE " + petName + "§c SELECTED AS YOUR PET. YOU WANTED TO UPGRADE " + selectedPetName + "." +
            "\n§dClick this message or run /mutepetalert to mute the alert for " + PartlySaneSkies.config.petAlertMuteTime + " minutes."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mutepetalert"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            lastMessageSendTime = PartlySaneSkies.getTime();
        }
        
    }

    public static void favouritePet() {
        if (!HypixelUtils.INSTANCE.isSkyblock()) {
            return;
        }
        ItemStack item;
        if (!isPetGui()) {
            return;
        }
        if (PartlySaneSkies.minecraft.currentScreen instanceof AuctionHouseGui) {
            return;
        }

        GuiContainer container = (GuiContainer) PartlySaneSkies.minecraft.currentScreen;
        Slot slot = container.getSlotUnderMouse();
        if (slot == null)
            return;
        item = slot.getStack();

        if (item == null) {
            return;
        }

        if (HypixelUtils.INSTANCE.getItemId(item).isEmpty()) {
            return;
        }
        String petName = parsePetNameFromItem(item.getDisplayName());
        PartlySaneSkies.config.selectedPet = petName;
        ChatUtils.INSTANCE.sendClientMessage("Set " + petName + " as your favorite pet.");
        PartlySaneSkies.config.save();
    }

    // Parses a pet's name from the armor stand string. Ex: "[Lv100] Su386's *Black Cat*"
    public static String parsePetNameFromEntity(String name) {
        name = StringUtils.INSTANCE.removeColorCodes(name);
        int petNameStartIndex = name.indexOf("'s ") + 3; // Finds the start of the pet name. Ex: "[Lv100] Su386's *Black Cat"
        return name.substring(petNameStartIndex);
    }

    public static void registerCommand() {
        new PSSCommand("mutepetalert")
                .setDescription("Mutes the pet alert for a set amount of minutes")
                .setRunnable((s, a) -> {
                    ChatUtils.INSTANCE.sendClientMessage("§bPet alert has been muted for " +  PartlySaneSkies.config.petAlertMuteTime + " minutes.");
                    PetAlert.lastMuteTime = PartlySaneSkies.getTime();
                }).register();
    }

    public static String parsePetNameFromItem(String name) {
        name = StringUtils.INSTANCE.removeColorCodes(name);
        int petNameStartIndex = name.indexOf("] ") + 2; // Finds the start of the pet name. Ex: "[Lv100] Su386's *Black Cat"
        return name.substring(petNameStartIndex);
    }

    public static boolean isPetGui() {
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = Objects.requireNonNull(MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0];
        return StringUtils.INSTANCE.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Pets");
    }


    public static boolean isMinionGui() {
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = Objects.requireNonNull(MinecraftUtils.INSTANCE.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen))[0];
        boolean inventoryNameMatches = StringUtils.INSTANCE.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Minion");

        if (!inventoryNameMatches) {
            return false;
        }

//        Gets the slot where the minion head is supposed to be
        ItemStack minionHeadSlot = upper.getStackInSlot(4);

        if (minionHeadSlot == null) {
            return false;
        }
        String displayName = StringUtils.INSTANCE.removeColorCodes(minionHeadSlot.getDisplayName());

        return displayName.contains("Minion");
    }


    // Using that list of pets, check to see if it's owned by a specific player
    public static Entity getUsersPet(String name) {
        List<Entity> petEntities = getAllPets();
        // If the pet says Ex: "[Lv100] *Su386*'s Black Cat" return that entity
        for (Entity entity : petEntities) {
            if (StringUtils.INSTANCE.removeColorCodes(entity.getName()).toLowerCase().contains(name.toLowerCase())) {
                return entity;
            }
        }
        return null;
    }

    // Gets all the pets current loaded by the game
    private static List<Entity> getAllPets() {
        List<Entity> petEntities = new ArrayList<>();
        List<Entity> armorStandEntities = getAllArmorStands();
        
        // For every armor stand in the game, check if it's pet by looking for the level tag in front of the name.
        // Ex: "*[Lv*100] Su386's Black Cat"
        for (Entity entity : armorStandEntities) {
            if (StringUtils.INSTANCE.removeColorCodes(entity.getName()).contains("[Lv")) {
                petEntities.add(entity); // If so, add it to the list
            }
        }

        return petEntities;
    }

    // Gets all the armor stands currently loaded by the game
    private static List<Entity> getAllArmorStands() {
        List<Entity> armorStandEntities = new ArrayList<>();
        List<Entity> allEntities = getAllEntitiesInWorld();

        // For every entity in the world, check if its instance of an armor stand
        for (Entity entity : allEntities) {
            if (entity instanceof EntityArmorStand) {
                armorStandEntities.add(entity); // If so, add it to the list
            }
        }

        return armorStandEntities;
    }
    
    // Returns a list of all loaded entities in the world
    private static List<Entity> getAllEntitiesInWorld() {
        return PartlySaneSkies.minecraft.theWorld.getLoadedEntityList();
    }

    static Window window = new Window(ElementaVersion.V2);

    UIComponent box = new UIRoundedRectangle(widthScaledConstraint(5).getValue())
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(window);

    UIComponent image = ThemeManager.getCurrentBackgroundUIImage()
            .setChildOf(box);

    UIWrappedText textComponent = (UIWrappedText) new UIWrappedText("", true, null, true)
            .setChildOf(box);

    @SubscribeEvent
    public void renderInformation(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!isMinionGui()) {
            box.hide();
            return;
        }
        if (!PartlySaneSkies.config.selectedPetInformation) {
            return;
        }

        box.unhide(true);
        box.setX(widthScaledConstraint(633))
                .setY(new CenterConstraint())
                .setWidth(widthScaledConstraint(125))
                .setHeight(widthScaledConstraint(100));

        image.setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(box.getWidth()))
                .setHeight(new PixelConstraint(box.getHeight()));

        textComponent.setX(new CenterConstraint())
                .setTextScale(widthScaledConstraint(1f))
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(box.getWidth()));

        Entity currentlySelectedPet = getUsersPet(PartlySaneSkies.minecraft.thePlayer.getName());

        String currentlySelectedPetName = "";
        if (currentlySelectedPet != null) {
            currentlySelectedPetName = parsePetNameFromEntity(currentlySelectedPet.getName());
        }

        if (currentlySelectedPetName.isEmpty()) {
            currentlySelectedPetName = ("§8(Unknown)");
        }

        String petColorCode;

        if (PartlySaneSkies.config.selectedPet.isEmpty()) {
            petColorCode = "§d";
        }
        else if (currentlySelectedPetName.equalsIgnoreCase(PartlySaneSkies.config.selectedPet)) {
            petColorCode = "§a";

        }
        else {
            petColorCode = "§c";
        }


        String textString = "§eCurrently Selected Pet:\n" +
                petColorCode + currentlySelectedPetName + "\n\n" +
                "§eDesired Pet:\n" +
                "§d" + PartlySaneSkies.config.selectedPet;
        textComponent.setText(textString);
        window.draw(new UMatrixStack());
    }

    private static float getWidthScaleFactor() {
        return window.getWidth() / 1097f;
    }

    private static PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor());
    }
}
