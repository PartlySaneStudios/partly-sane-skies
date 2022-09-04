package me.partlysanestudios.partlysaneskies.keybind;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public final class KeyInit {
    

    public static KeyBinding debugKey;
    public static KeyBinding configKey;
    public static KeyBinding partyManagerKey;

    public static void init() {
        debugKey = registerKey("Debug", "Partly Sane Skies", Keyboard.KEY_F4);
        configKey = registerKey("Config", "Partly Sane Skies", Keyboard.KEY_F7);
        partyManagerKey = registerKey("Party Manager", "Partly Sane Skies", Keyboard.KEY_P);
    }


    private static KeyBinding registerKey(String name, String category, int keycode) {
        final KeyBinding key = new KeyBinding(name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
