package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class ThemesConfig {
    //    Themes
    @Dropdown(
            name = "Selected Theme",
            category = "Themes",
            subcategory = "Theme",
            description = "Pick from one of our 9 custom designed themes",
            options = {
                    "Classic (Dark)",
                    "Royal Dark (Dark)",
                    "Midnight Forest (Dark)",

                    "Moonless Night (Very Dark)",
                    "Stormy Night (Very Dark)",
                    "The Void (Very Dark)",

                    "Classic (Light)",
                    "Royal Light (Light)",
                    "Partly Cloudy (Light)",

                    "Waterfall (Colorful)",
                    "Jungle (Colorful)",
                    "Dunes (Colorful)"
            }
    )
    public int themeIndex = 0;

    //    Accent Color
    @Switch(
            name = "Use default accent color",
            description = "Uses the default Partly Sane Skies accent color",
            category = "Themes",
            subcategory = "Accent Color"
    )
    public boolean useDefaultAccentColor = true;

    @Color(
            name = "Custom Accent Color",
            description = "Choose a custom accent color for your game",
            category = "Themes",
            subcategory = "Accent Color"
    )
    public OneColor accentColor = new OneColor(1, 255, 255, 255);

    //    Custom Themes
    @Switch(
            name = "Create your own Theme",
            description = "Enable to be able to create your own custom themes",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public boolean customTheme = false;

    @Color(
            name = "Custom Primary Color",
            description = "Choose a custom primary color for your game",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public OneColor primaryColor = new OneColor(42, 43, 46, 255);

    @Color(
            name = "Custom Secondary Color",
            description = "Choose a custom secondary color for your game",
            category = "Themes",
            subcategory = "Custom Themes"
    )
    public OneColor secondaryColor = new OneColor(42, 43, 46, 255);


    //    Resource Packs
    @Switch(
            name = "Disable themes to use resource packs",
            description = "Disable themes to be able to use resource packs to modify Partly Sane Skies menus",
            category = "Themes",
            subcategory = "Resource Packs"
    )
    public boolean disableThemes = false;
}
