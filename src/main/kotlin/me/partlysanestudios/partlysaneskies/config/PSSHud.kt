//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.config

import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack

/**
 * An abstraction of [cc.polyfrost.oneconfig.hud.Hud].
 * This is necessary because OneConfig attempts to deserialize to Json the entire object, including any properties saved.
 *
 * @see me.partlysanestudios.partlysaneskies.features.debug.ExampleHud for example
 * @sample me.partlysanestudios.partlysaneskies.features.debug.ExampleHud
 *
 * @param enabled Whether the HUD is enabled by default or not (Default true)
 * @param x The default x coordinate of the hud on a 1920x1080p display (Default 0)
 * @param y The default Y coordinate of the hud on a 1920x1080p display (Default 0)
 * @param positionAlignment I genuinely have no idea what this is. It's not documented anywhere in OneConfig except "[positionAlignment] - Alignment of the hud" (Default 0)
 * @param scale The default scale of the hud (Default 1)
 */
abstract class PSSHud(
    protected var enabled: Boolean = true,
    protected var x: Float = 0.0F,
    protected var y: Float = 0.0F,
    var positionAlignment: Int = 0,
    var scale: Float = 1.0f
) {
    /**
     * Whether the current rendering cycle is an example (when the user is configuring the main menu)
     */
    protected var example = false

    private val oneConfigHud = object : Hud(enabled, x, y, positionAlignment, scale) {
        override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
            this@PSSHud.x = x
            this@PSSHud.y = y
            this@PSSHud.scale = scale
            this@PSSHud.example = example
        }

        override fun getWidth(scale: Float, example: Boolean): Float {
            return try {
                this@PSSHud.getWidth(scale, example)
            } catch (e: Exception) {
                e.printStackTrace()
                0.0F
            }
        }

        override fun getHeight(scale: Float, example: Boolean): Float {
            return try {
                this@PSSHud.getHeight(scale, example)
            } catch (e: Exception) {
                e.printStackTrace()
                0.0F
            }
        }
    }

    /**
     * If you want to have other options in the Hud, override this to return a custom OneConfig Hud
     *
     * @return The OneConfig hud
     */
    open fun getOneConfigHud(): Hud {
        return oneConfigHud
    }

    /**
     * @return Height of the Hud's bounding box in pixels
     *
     * @param scale the current scale
     * @param example whether it is an example or not
     */
    abstract fun getHeight(scale: Float, example: Boolean): Float

    /**
     * @return Width of the Hud's bounding box in pixels
     *
     * @param scale the current scale
     * @param example whether it is an example or not
     */
    abstract fun getWidth(scale: Float, example: Boolean): Float


}