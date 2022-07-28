package me.partlysanestudios.partlysaneskies.keybind;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public final class KeyInit {
    

    public static KeyBinding debugKey;

    public static void init() {
        debugKey = registerKey("Debug", "Partly Sane Skies", Keyboard.KEY_F4);
    }


    private static KeyBinding registerKey(String name, String category, int keycode) {
        final KeyBinding key = new KeyBinding(name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
