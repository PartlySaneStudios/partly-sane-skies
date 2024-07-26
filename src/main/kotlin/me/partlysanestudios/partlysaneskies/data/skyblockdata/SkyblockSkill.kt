//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.skyblockdata

class SkyblockSkill(
//    Returns the skill id
    val id: String, //    Gets the maximum level of the skill
    val maxLevel: Int,
    private val totalExpRequired: HashMap<Int, Float>,
) {
    //    Gets the total exp required to get to the current level
    fun getTotalExpRequired(level: Int): Float = totalExpRequired[level]!!

    //    Gets the total exp required to level up from the previous level
    fun getLevelUpExpRequired(level: Int): Float = getTotalExpRequired(level) - getTotalExpRequired(level - 1)

    fun getLevelFromExperience(experience: Float): Double {
        var level = 0f
        val experienceAtLevel = totalExpRequired.values.toList()
        if (experience >= experienceAtLevel[experienceAtLevel.size - 1]) {
            return maxLevel.toDouble()
        }
        for (i in experienceAtLevel.indices) {
            if (experience > experienceAtLevel[i]) {
                level = (i + 1).toFloat()
            }
        }
        if (level != 0.0F) {
            level +=
                (experience - experienceAtLevel[level.toInt() - 1]) /
                (experienceAtLevel[level.toInt()] - experienceAtLevel[level.toInt() - 1])
        }
        return level.toDouble()
    }
}
