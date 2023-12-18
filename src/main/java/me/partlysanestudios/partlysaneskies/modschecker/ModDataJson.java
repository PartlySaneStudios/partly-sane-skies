//
// Written by hannibal002.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.modschecker;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class ModDataJson {
    @Expose
    private Map<String, ModInfo> mods;

    public Map<String, ModInfo> getMods() {
        return mods;
    }

    public static class ModInfo {
        @Expose
        private String download;
        @Expose
        private final Map<String, String> versions;
        @Expose
        private final Map<String, String> betaVersions;

        public ModInfo() {
            versions = new HashMap<>();
            betaVersions = new HashMap<>();

        }

        public String getDownload() {
            return download;
        }

        public Map<String, String> getVersions() {
            return versions;
        }
        public Map<String, String> getBetaVersions() {
            return betaVersions;
        }
    }
}
