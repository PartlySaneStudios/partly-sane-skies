package me.partlysanestudios.partlysaneskies.modschecker;

import me.partlysanestudios.partlysaneskies.utils.Utils;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.List;

public class LaunchWrapperTweaker implements ITweaker {

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\nTHIS IS A TEST \n\n\n\n\n\n\n\n\n\n\n");
        ModsFolderChecker.INSTANCE.openWindow();
        System.out.println(ModsFolderChecker.INSTANCE.generateString());
    }

    @Override
    public String getLaunchTarget() {
        return null;
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
