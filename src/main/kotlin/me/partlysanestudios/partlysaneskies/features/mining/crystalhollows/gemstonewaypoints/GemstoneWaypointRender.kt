//
// Written by Su386 and j10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.mining.crystalhollows.gemstonewaypoints

import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.api.events.PSSEvent
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.minecraft.render.RenderWaypointEvent
import me.partlysanestudios.partlysaneskies.render.waypoint.Waypoint
import me.partlysanestudios.partlysaneskies.utils.ImageUtils.applyOpacity
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point2d
import me.partlysanestudios.partlysaneskies.utils.geometry.vectors.Point3d
import java.awt.Color

object GemstoneWaypointRender {
    @PSSEvent.Subscribe
    fun onWaypointRenderEvent(event: RenderWaypointEvent) {
        if (!config.renderGemstoneWaypoints) {
            return
        }

        if (!IslandType.CRYSTAL_HOLLOWS.onIsland()) {
            return
        }

        val gemstonesTypesToShow = ArrayList<GemstoneData.GemstoneType>()

        if (config.topazWaypoints) {
            gemstonesTypesToShow.add(GemstoneData.GemstoneType.TOPAZ)
        }
        if (config.rubyWaypoints) {
            gemstonesTypesToShow.add(GemstoneData.GemstoneType.RUBY)
        }
        if (config.sapphireWaypoints) {
            gemstonesTypesToShow.add(GemstoneData.GemstoneType.SAPPHIRE)
        }
        if (config.amethystWaypoints) {
            gemstonesTypesToShow.add(GemstoneData.GemstoneType.AMETHYST)
        }
        if (config.amberWaypoints) {
            gemstonesTypesToShow.add(GemstoneData.GemstoneType.AMBER)
        }
        if (config.jadeWaypoints) {
            gemstonesTypesToShow.add(GemstoneData.GemstoneType.JADE)
        }

        val renderDistance = config.gemstoneWaypointRenderDistance

        val playerChunk = Point3d.atPlayer().toChunk()

        for (x in (playerChunk.x - renderDistance).toInt()..(playerChunk.x + renderDistance).toInt()) {
            for (y in (playerChunk.y - renderDistance).toInt()..(playerChunk.y + renderDistance).toInt()) {
                val chunk = Point2d(x.toDouble(), y.toDouble())
                if (chunk.distanceTo(playerChunk) > renderDistance) {
                    continue
                }

                val chunkData = GemstoneData.map[chunk] ?: continue

                for (type in gemstonesTypesToShow) {
                    for (gemstone in chunkData[type] ?: continue) {
                        if (gemstone.size < config.gemstoneMinSize) {
                            continue
                        }
                        val brightness = config.gemstoneBrightness
                        val originalColor = gemstone.type.color

                        val darkerColor =
                            Color(
                                (originalColor.red * brightness).toInt(),
                                (originalColor.green * brightness).toInt(),
                                (originalColor.blue * brightness).toInt(),
                            )

                        val waypoint =
                            Waypoint(
                                "${gemstone.type.displayName} Gemstone | Size: ${gemstone.size}",
                                gemstone.block.toBlockPos(),
                                outlineColor = darkerColor.applyOpacity(255),
                                fillColor = darkerColor.applyOpacity(100),
                                showBeam = config.showGemstoneBeam,
                            )
                        event.pipeline.add(waypoint)
                    }
                }
            }
        }
    }
}
