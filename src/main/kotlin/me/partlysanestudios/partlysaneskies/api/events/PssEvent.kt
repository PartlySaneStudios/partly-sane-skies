package me.partlysanestudios.partlysaneskies.api.events

abstract class PssEvent protected constructor() {

    var isCancelled = false
        private set

    fun post() {
        PssEvents.post(this)
    }

    interface Cancellable {

        fun cancel() {
            (this as PssEvent).isCancelled = true
        }
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Subscribe(
        val receiveCancelled: Boolean = false,
    )
}
