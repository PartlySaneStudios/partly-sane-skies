//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.skyblockdata;

import java.util.HashMap;

public class SkyblockSkill {
    private final String id;
    private final int maxLevel;
    private final HashMap<Integer,Float> totalExpRequired;


    public SkyblockSkill(String id, int maxLevel, HashMap<Integer, Float> totalExpRequired) {
        this.id = id;
        this.maxLevel = maxLevel;
        this.totalExpRequired = totalExpRequired;
    }

//    Gets the total exp required to get to the current level
    public float getTotalExpRequired(int level) {
        return totalExpRequired.get(level);
    }

//    Gets the total exp required to level up from the previous level
    public float getLevelUpExpRequired(int level) {
        return getTotalExpRequired(level) - getTotalExpRequired(level - 1);
    }

//    Returns the skill id
    public String getId() {
        return id;
    }

//    Gets the maximum level of the skill
    public int getMaxLevel() {
        return maxLevel;
    }

    public float getLevelFromExperience(float experience) {
        float level = 0;
        Float[] experienceAtLevel = totalExpRequired.values().toArray(new Float[] {});

        if (experience >= experienceAtLevel[experienceAtLevel.length - 1]) {
            return maxLevel;
        }

        for (int i = 0; i < experienceAtLevel.length; i++) {
            if (experience > experienceAtLevel[i]) {
                level = i + 1;
            }
        }

        level += (experience - experienceAtLevel[(int) level - 1]) / (experienceAtLevel[(int) level] - experienceAtLevel[(int) level - 1]);

        return level;
    }
}
