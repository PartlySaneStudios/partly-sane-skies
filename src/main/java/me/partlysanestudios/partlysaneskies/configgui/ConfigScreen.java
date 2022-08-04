package me.partlysanestudios.partlysaneskies.configgui;

import java.io.File;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

public class ConfigScreen extends Vigilant{

    @Property(
        type = PropertyType.SWITCH,
        name = "Rare Drop Banner",
        description = "On rare drop, get a Pumpkin Dicer like banner.",
        category = "Skyblock"
    )
    public boolean rareDropBanner = false;
    
    
    public ConfigScreen() {
        super(new File("./config/pss.toml"));
        this.initialize();
        this.loadData();
    }

    
}
