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

    fun getCommand(commandName: String): PSSCommand? {
        return commandList[commandName]
    }

    fun registerCommand(pssCommand: PSSCommand): ICommand? {
        if (pssCommand.isRegistered) {
            return null
        }
        val iCommand: ICommand = object : ICommand {
            override fun getCommandName(): String {
                return pssCommand.name
            }

            override fun getCommandUsage(sender: ICommandSender): String {
                return pssCommand.getDescription()
            }

            override fun getCommandAliases(): List<String> {
                return pssCommand.getAliases()
            }

            @Throws(CommandException::class)
            override fun processCommand(sender: ICommandSender, args: Array<String>) {
                pssCommand.runRunnable(sender, args)
            }

            override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean {
                return true
            }

            override fun addTabCompletionOptions(
                sender: ICommandSender,
                args: Array<String>,
                pos: BlockPos
            ): List<String> {
                return ArrayList()
            }

            override fun isUsernameIndex(args: Array<String>, index: Int): Boolean {
                return false
            }

            override fun compareTo(o: ICommand): Int {
                return this.commandName.compareTo(o.commandName)
            }
        }
        pssCommand.setICommand(iCommand)
        ClientCommandHandler.instance.registerCommand(iCommand)
        commandList[pssCommand.name] = pssCommand
        return iCommand
    }

}
