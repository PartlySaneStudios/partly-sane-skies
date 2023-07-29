package me.partlysanestudios.partlysaneskies.garden;

import java.util.ArrayList;
import java.util.List;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;
import me.partlysanestudios.partlysaneskies.utils.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;

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
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        boolean canRightClickHoe = Utils.onCooldown(MathematicalHoeRightClicks.lastAllowHoeRightClickTime, (long) (PartlySaneSkies.config.allowRightClickTime * 60L * 1000L));

        if(canRightClickHoe){
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&dThe ability to right-click with a hoe has been &cdisabled&d again.\n&dClick this message or run /allowhoerightclick to allow right-clicks for " + PartlySaneSkies.config.allowRightClickTime + " again."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0;
            return;
        } else {
            IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&dThe ability to right-click with a hoe has been &aenabled&d for " + PartlySaneSkies.config.allowRightClickTime + " minutes.\n&dClick this message or run /allowhoerightclick to disable right-clicks again."));
            message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick"));
            PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
            MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.getTime();
            return;
        }

        // if(args.length > 0 && args[0].equalsIgnoreCase("disallow")){
        //     Utils.sendClientMessage("&dThe ability to right-click with a hoe has been disabled.");
        //     MathematicalHoeRightClicks.lastAllowHoeRightClickTime = 0;
        //     return;
        // }

        // if(args.length == 0 || args[0].equalsIgnoreCase("allow")){
        //     IChatComponent message = new ChatComponentText(PartlySaneSkies.CHAT_PREFIX + StringUtils.colorCodes("&dThe ability to right-click with a hoe has been enabled for " + PartlySaneSkies.config.allowRightClickTime + " minutes.\n&dClick this message or run \"/allowhoerightclick disallow\" to disable right again."));
        //     message.getChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/allowhoerightclick disallow"));
        //     PartlySaneSkies.minecraft.ingameGUI.getChatGUI().printChatMessage(message);
        //     MathematicalHoeRightClicks.lastAllowHoeRightClickTime = PartlySaneSkies.getTime();
        //     return;
        // }
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
