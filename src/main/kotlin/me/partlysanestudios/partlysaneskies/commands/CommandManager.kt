//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.commands

import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler

object CommandManager {
    var commandList = HashMap<String, PSSCommand>()

    fun getCommand(commandName: String): PSSCommand? = commandList[commandName]

    fun registerCommand(pssCommand: PSSCommand): ICommand? {
        if (pssCommand.isRegistered) return null
        val iCommand: ICommand =
            object : ICommand {
                override fun getCommandName(): String = pssCommand.name

                override fun getCommandUsage(sender: ICommandSender): String = pssCommand.description

                override fun getCommandAliases(): List<String> = pssCommand.aliases

                @Throws(CommandException::class)
                override fun processCommand(sender: ICommandSender, args: Array<String>) {
                    pssCommand.runRunnable(args)
                }

                override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean = true

                override fun addTabCompletionOptions(sender: ICommandSender, args: Array<String>, pos: BlockPos): List<String> = ArrayList()

                override fun isUsernameIndex(args: Array<String>, index: Int): Boolean = false

                override fun compareTo(other: ICommand): Int = commandName.compareTo(other.commandName)
            }
        pssCommand.iCommand = iCommand
        ClientCommandHandler.instance.registerCommand(iCommand)
        pssCommand.isRegistered = true
        commandList[pssCommand.name] = pssCommand
        return iCommand
    }
}
