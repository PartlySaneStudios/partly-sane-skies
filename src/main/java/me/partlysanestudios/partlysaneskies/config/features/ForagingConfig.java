package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.Switch;

public class ForagingConfig {
    //    Treecapitator Cooldown Indicator
    @Switch(
            subcategory = "Treecapitator Cooldown Indidcator",
            name = "Treecapitator Cooldown Indidcator Enabled",
            description = "Displays a cooldown indicator below your crosshair whenever your treecapitator is on cooldown",
            category = "Foraging"
    )
    public boolean treecapCooldown = false;

    @Switch(
            subcategory = "Treecapitator Cooldown Indidcator",
            name = "Use Monkey Pet",
            description = "Use the monkey pet to dynamically adjust the length of the cooldown",
            category = "Foraging"
    )
    public boolean treecapCooldownMonkeyPet = true;
}
