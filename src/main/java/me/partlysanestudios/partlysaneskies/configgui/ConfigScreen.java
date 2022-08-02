package me.partlysanestudios.partlysaneskies.configgui;

import java.io.File;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

public class ConfigScreen extends Vigilant{
    @Property(
        type = PropertyType.NUMBER,
        name = "Number",
        description = "test",
        category = "test2",
        min = 0,
        max = 10
    )
    int demoNumber = 0;
    public ConfigScreen() {
        super(new File("./config/example.toml"));
        
    }
    
}
