//
// Written by DerGruenkohl
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.features.chat

import cc.polyfrost.oneconfig.events.event.ChatSendEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config


//Currently only using to owoify send chat messages (meow)
object ChatTransformer {
    var lastmsg = ""

    @Subscribe
    fun onChat(event: ChatSendEvent) {
        //dont mess with chats that dont want to
        if (!doChatTransform()) {
            return
        }

        event.isCancelled = true
        val msg = event.message
        //Do not go into recursion (bad)
        if (lastmsg == msg) {
            event.isCancelled = false
            return
        }
        //dont break commands :)
        if (msg.startsWith("/")){
            return
        }

        val player = PartlySaneSkies.minecraft.thePlayer
        var transformedmsg = ""

        if (config.transformOWO) {
            transformedmsg = OwO.owoify(msg)
        }
        if (transformedmsg.isEmpty()){
            return
        }
        // Putting this in a thread is needed so the recursive call doesn't wipe/duplicate the message
        Thread {
            lastmsg = transformedmsg
            player.sendChatMessage(transformedmsg)
            lastmsg = ""
        }.start()

    }
    private fun doChatTransform(): Boolean {
        return config.transformOWO
    }
}