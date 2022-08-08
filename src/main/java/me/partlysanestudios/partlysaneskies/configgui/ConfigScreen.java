package me.partlysanestudios.partlysaneskies.configgui;

import java.io.File;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

public class ConfigScreen extends Vigilant{

    @Property(
        type = PropertyType.TEXT,
        protectedText = true,
        name = "API Key",
        category = "General",
        subcategory = "API",
        description = "Do /api new to automatically set your API Key. Do not show your API key to anyone unless you know what you're doing."
    )
    public String apiKey = "";
    

    @Property(
        type = PropertyType.SWITCH,
        name = "Rare Drop Banner",
        subcategory = "General",
        description = "On rare drop, get a Pumpkin Dicer like banner.",
        category = "Skyblock"
    )
    public boolean rareDropBanner = false;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        minF = 1,
        maxF = 7,
        subcategory = "General",
        name = "Rare Drop Banner Time",
        description = "The amount of time the rare drop banner stays on",
        category = "Skyblock"
    )
    public float rareDropBannerTime = 3.5f;
    
    @Property(
        type = PropertyType.SWITCH,
        name = "Custom Rare Drop Sound",
        subcategory = "General",
        description = "Plays a custom sound when you get a rare drop.",
        category = "Skyblock"
    )
    public boolean rareDropBannerSound = false;


    public ConfigScreen() {
        super(new File("./config/pss.toml"));
        this.initialize();

        
    }

    
}
