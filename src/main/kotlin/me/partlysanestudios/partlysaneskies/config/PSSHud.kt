package me.partlysanestudios.partlysaneskies.config

import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.ISound.AttenuationType
import net.minecraft.util.ResourceLocation

abstract class PSSHud(
    protected var enabled: Boolean = true,
    protected var x: Float = 0.0F,
    protected var y: Float = 0.0F,
    var positionAlignment: Int = 0,
    var scale: Float = 1.0f
) {
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

    fun getOneConfigHud(): Hud {
        return oneConfigHud
    }

    abstract fun getHeight(scale: Float, example: Boolean): Float
    abstract fun getWidth(scale: Float, example: Boolean): Float


}