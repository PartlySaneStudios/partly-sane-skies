//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.system.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class PSSCommand {
    private String name;
    private List<String> aliases;
    private String description;
    private PSSCommandRunnable runnable;
    private ICommand command;
    private boolean registered;
    public PSSCommand(String name, List<String> aliases, String description, PSSCommandRunnable runnable) {
        this(name, aliases, description);
        this.runnable = runnable;
    }
    public PSSCommand(String name, List<String> aliases, String description) {
        this(name, aliases);
        this.description = description;
    }
    public PSSCommand(String name, List<String> aliases) {
        this(name);
        this.aliases = aliases;
    }

    public PSSCommand(String name) {
        this.name = name;
        this.aliases = new ArrayList<>();
        this.runnable = (s, a) -> {};
        this.registered = false;
    }



    public PSSCommand addAlias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public PSSCommand setRunnable(PSSCommandRunnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public PSSCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public void runRunnable(ICommandSender sender, String[] args) {
        this.runnable.run(sender, args);
    }

    public ICommand register() {
        return CommandManager.registerCommand(this);
    }
    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public PSSCommandRunnable getRunnable() {
        return runnable;
    }

    public void setRegistered() {
        this.registered = true;
    }
    public boolean isRegistered() {
        return CommandManager.commandList.containsKey(name);
    }
    public ICommand getICommand() {
        return this.command;
    }
    public PSSCommand setICommand(ICommand iCommand) {
        this.command = iCommand;
        return this;
    }
}
