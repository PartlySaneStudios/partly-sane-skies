//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.commands

import net.minecraft.command.ICommandSender

fun interface PSSCommandRunnable {
    fun run(sender: ICommandSender, args: Array<String>)
}
