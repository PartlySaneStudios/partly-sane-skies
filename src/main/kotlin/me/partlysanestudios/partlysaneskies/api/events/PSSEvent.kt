//
// Written by ThatGravyBoat.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.api.events

abstract class PSSEvent protected constructor() {

    var isCancelled = false
        private set

    fun post() {
        PSSEvents.post(this)
    }

    interface Cancellable {

        fun cancel() {
            (this as PSSEvent).isCancelled = true
        }
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Subscribe(
        val receiveCancelled: Boolean = false,
    )
}