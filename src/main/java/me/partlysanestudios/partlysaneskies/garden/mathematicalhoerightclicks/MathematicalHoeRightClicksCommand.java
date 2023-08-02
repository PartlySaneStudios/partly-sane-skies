package me.partlysanestudios.partlysaneskies.garden.mathematicalhoerightclicks;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;

public class MathematicalHoeRightClicksCommand implements ICommand {

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "allowhoerightclick";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ahrc", "allowhoerightclicks", "allowrightclicks", "allowrightclick");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        allowRightClicks();
    }

    public static void allowRightClicks() {

        boolean canRightClickHoe = Utils.onCooldown(MathematicalHoeRightClicks.lastAllowHoeRightClickTime, (long) (PartlySaneSkies.config.allowRightClickTime * 60L * 1000L));

        if(canRightClickHoe){
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&dThe ability to right-click with a hoe has been &cdisabled&d again.\n&dClick this message or run /allowhoerightclick to allow right-clicks for " + PartlySaneSkies.config.allowRightClickTime + " again."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0;
        } else {
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&dThe ability to right-click with a hoe has been &aenabled&d for " + PartlySaneSkies.config.allowRightClickTime + " minutes.\n&dClick this message or run /allowhoerightclick to disable right-clicks again."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.getTime();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
    
}
