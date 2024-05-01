//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.commands

import me.partlysanestudios.partlysaneskies.commands.CommandManager.commandList
import me.partlysanestudios.partlysaneskies.commands.CommandManager.registerCommand
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender

class PSSCommand(val name: String) {
    var aliases: MutableList<String> = ArrayList<String>()
    var description: String = ""
    var runnable = PSSCommandRunnable { _: ICommandSender?, _: Array<String> -> }
    var iCommand: ICommand? = null
    var isRegistered = false

    constructor(
        name: String,
        aliases: MutableList<String> ,
        description: String,
        runnable: PSSCommandRunnable,
    ) : this(name, aliases, description) {
        this.runnable = runnable
    }

    constructor(name: String, aliases: MutableList<String> , description: String) : this(name, aliases) {
        this.description = description
    }

    constructor(name: String, aliases: MutableList<String> ) : this(name) {
        this.aliases = aliases
    }

    fun addAlias(alias: String): PSSCommand {
        aliases.add(alias)
        return this
    }

    fun setRunnable(runnable: PSSCommandRunnable): PSSCommand {
        this.runnable = runnable
        return this
    }

    fun setDescription(description: String): PSSCommand {
        this.description = description
        return this
    }

    fun runRunnable(sender: ICommandSender?, args: Array<String>) {
        runnable.run(sender, args)
    }

    fun register(): ICommand? {
        return registerCommand(this)
    }

    fun setICommand(iCommand: ICommand?): PSSCommand {
        this.iCommand = iCommand
        return this
    }
}
