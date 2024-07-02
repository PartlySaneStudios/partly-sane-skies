//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.commands

import me.partlysanestudios.partlysaneskies.commands.CommandManager.registerCommand
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender

class PSSCommand(val name: String) {

    var aliases: MutableList<String> = mutableListOf()
        private set
    var description: String = ""
        private set
    private var runnable = PSSCommandRunnable{}
    var iCommand: ICommand? = null
    var isRegistered = false

    constructor(
        name: String,
        aliases: MutableList<String>,
        description: String,
        runnable: PSSCommandRunnable,
    ) : this(name, aliases, description) {
        this.runnable = runnable
    }

    constructor(name: String, aliases: MutableList<String>, description: String) : this(name, aliases) {
        this.description = description
    }

    constructor(name: String, aliases: MutableList<String>) : this(name) {
        this.aliases = aliases
    }


    fun addAlias(vararg aliases: String): PSSCommand {
        this.aliases.addAll(aliases)
        return this
    }

    // java classes don't like when it wants it to return Unit
    fun setRunnable(runnable: PSSCommandRunnable): PSSCommand {
        this.runnable = runnable
        return this
    }

    fun setRunnable(function: (Array<String>) -> Unit) = setRunnable(PSSCommandRunnable(function))

    fun setDescription(description: String): PSSCommand {
        this.description = description
        return this
    }

    fun runRunnable(sender: ICommandSender, args: Array<String>) = runnable.run(args)

    fun register(): ICommand? = registerCommand(this)
}
