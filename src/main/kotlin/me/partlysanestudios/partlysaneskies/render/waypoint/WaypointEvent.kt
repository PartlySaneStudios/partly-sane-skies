//
// Written by Su386.
// See LICENSE for copyright and license notices.
//



package me.partlysanestudios.partlysaneskies.render.waypoint

/**
 * Any function with this annotation will get called with a WaypointRenderPipeline object.
 * Whenever the function is run, the function can add waypoints to the render pipeline, which will then be rendered.
 * These functions are called during the RenderWorldLastEvent
 *
 * Make sure to register the object with WaypointEvents.register(object)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class WaypointEvent() {

}
