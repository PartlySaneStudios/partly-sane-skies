//
// Written by J10a1n15.
// See LICENSE for copyright and license notices.
// Hard inspired by NotEnoughUpdates - https://github.com/NotEnoughUpdates/NotEnoughUpdates
//

package me.partlysanestudios.partlysaneskies.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.Collectors;

public class TabListUtils {

    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(TabListUtils::comparePlayers);

    @SideOnly(Side.CLIENT)
    public static List<String> getTabList() {
        List<NetworkPlayerInfo> players = Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().stream()
                .sorted(playerOrdering)
                .collect(Collectors.toList());

        return players.stream()
                .map(info -> Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info))
                .collect(Collectors.toList());
    }

    private static int comparePlayers(NetworkPlayerInfo overlay1, NetworkPlayerInfo overlay2) {
        ScorePlayerTeam team1 = overlay1.getPlayerTeam();
        ScorePlayerTeam team2 = overlay2.getPlayerTeam();

        return ComparisonChain.start()
                .compare(team1 != null ? team1.getRegisteredName() : "",
                        team2 != null ? team2.getRegisteredName() : "")
                .compare(overlay1.getGameProfile().getName(), overlay2.getGameProfile().getName())
                .result();
    }
}