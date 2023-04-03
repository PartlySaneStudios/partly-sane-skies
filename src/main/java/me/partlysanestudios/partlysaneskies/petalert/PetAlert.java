/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.partlysanestudios.partlysaneskies.petalert;

import java.util.ArrayList;
import java.util.List;

import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.auctionhouse.AhGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.event.ClickEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.event.ClickEvent.Action;

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
        String petName = "";
        if (usersPet != null) {
            petName = parsePetNameFromEntity(usersPet.getName());
        
        }
        
        String selectedPetName = PartlySaneSkies.config.selectectedPet;
        
        if (petName.equalsIgnoreCase(selectedPetName)) {
            return;
        }

        if (onCooldown(lastMuteTime, (long) (PartlySaneSkies.config.petAlertMuteTime * 60l * 1000l))) {
            return;
        }
        
        if (!onCooldown(lastSoundTime, 750)) {
            PartlySaneSkies.minecraft.getSoundHandler()
                .playSound(
                    PositionedSoundRecord
                    .create(new ResourceLocation("partlysaneskies", "bell"))
                );
                if (PartlySaneSkies.config.incorrectPetForMinionAlertSiren) {
                    PartlySaneSkies.minecraft.getSoundHandler().playSound(
                            PositionedSoundRecord.create(new ResourceLocation("partlysaneskies", "airraidsiren")));
                }
            lastSoundTime = Minecraft.getSystemTime();
        }
        if (!onCooldown(lastMessageSendTime,3000)) {
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&cYOU CURRENTLY HAVE " + petName + " SELECTED AS YOUR PET. YOU WANTED TO UPGRADE " + selectedPetName + "." +
            "\n&dClick this message or run /mutepetalert to mute the alert for " + PartlySaneSkies.config.petAlertMuteTime + " minutes."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mutepetalert"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            lastMessageSendTime = Minecraft.getSystemTime();
        }
        
    }

    public static boolean onCooldown(long lastTime, long length) {
        if (Minecraft.getSystemTime() > lastTime + length) {
            return false;
        }
        return true;
    }

    public static void favouritePet() {
        if (!PartlySaneSkies.isSkyblock()) {
            return;
        }
        ItemStack item;
        if (!isPetGui()) {
            return;
        }
        if (PartlySaneSkies.minecraft.currentScreen instanceof AhGui) {
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

        if (Utils.getItemId(item).equals("")) {
            return;
        }
        String petName = parsePetNameFromItem(item.getDisplayName());
        PartlySaneSkies.config.selectectedPet = petName;
        Utils.sendClientMessage("Set " + petName + " as your favourite pet.");
        PartlySaneSkies.config.writeData();
    }

    // Parses a pet's name from the armor stand string. Ex: "[Lv100] Su386's *Black Cat*"
    public static String parsePetNameFromEntity(String name) {
        name = StringUtils.removeColorCodes(name);
        int petNameStartIndex = name.indexOf("'s ") + 3; // Finds the start of the pet name. Ex: "[Lv100] Su386's *Black Cat"
        return name.substring(petNameStartIndex);
    }

    public static String parsePetNameFromItem(String name) {
        name = StringUtils.removeColorCodes(name);
        int petNameStartIndex = name.indexOf("] ") + 2; // Finds the start of the pet name. Ex: "[Lv100] Su386's *Black Cat"
        return name.substring(petNameStartIndex);
    }

    public static boolean isPetGui() {
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0];
        return StringUtils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Pets");
    }


    public static boolean isMinionGui() {
        if (!(PartlySaneSkies.minecraft.currentScreen instanceof GuiChest)) {
            return false;
        }

        IInventory upper = PartlySaneSkies.getSeparateUpperLowerInventories(PartlySaneSkies.minecraft.currentScreen)[0];
        return StringUtils.removeColorCodes(upper.getDisplayName().getFormattedText()).contains("Minion");
    }


    // Using that list of pets, check to see if it's owned by a specific player
    public static Entity getUsersPet(String name) {
        List<Entity> petEntities = getAllPets();
        // If the pet says Ex: "[Lv100] *Su386*'s Black Cat" return that entity
        for (Entity entity : petEntities) {
            if (StringUtils.removeColorCodes(entity.getName()).toLowerCase().contains(name.toLowerCase())) {
                return entity;
            }
        }
        return null;
    }

    // Gets all the pets current loaded by the game
    private static List<Entity> getAllPets() {
        List<Entity> petEntities = new ArrayList<Entity>();
        List<Entity> armorStandEntities = getAllArmorStands();
        
        // For every armor stand in the game, check if its a pet by looking for the level tag in front of the name. Ex: "*[Lv*100] Su386's Black Cat"
        for (Entity entity : armorStandEntities) {
            if (StringUtils.removeColorCodes(entity.getName()).contains("[Lv")) {
                petEntities.add(entity); // If so, add it to the list
            }
        }

        return petEntities;
    }

    // Gets all of the armor stands currently loaded by the game
    private static List<Entity> getAllArmorStands() {
        List<Entity> armorStandEntities = new ArrayList<Entity>();
        List<Entity> allEntities = getAllEntitesInWorld();

        // For every entity in the world, check if its instance of an armor stand
        for (Entity entity : allEntities) {
            if (entity instanceof EntityArmorStand) {
                armorStandEntities.add(entity); // If so, add it to the list
            }
        }

        return armorStandEntities;
    }
    
    // Returns a list of all loaded entities in the world
    private static List<Entity> getAllEntitesInWorld() {
        return PartlySaneSkies.minecraft.theWorld.getLoadedEntityList();
    }
}
