package me.partlysanestudios.partlysaneskies.config.features;

import cc.polyfrost.oneconfig.config.annotations.Switch;

public class ChatConfig {
    //    WordEditor
    @Switch(
            name = "Word Editor Main Toggle",
            category = "Chat",
            subcategory = "Word Editor",
            description = "Allows you to edit words in chat. Can be configured with /wordeditor"
    )
    public boolean wordEditor = true;

    //  Chat Alerts
    @Switch(
            name = "Send System Notification",
            category = "Chat",
            subcategory = "Chat Alerts",
            description = "Sends a system notification when a message triggered by the Chat Alert was send."
    )
    public boolean chatAlertSendSystemNotification = false;

    //Chat Colors
    @Switch(
            name = "Color Private Messages",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Private messages pink to make them more visible in busy lobbies."
    )
    public boolean colorPrivateMessages = false;

    @Switch(
            name = "Color Nons Messages",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the non-ranked players to white to make them more visible in busy lobbies."
    )
    public boolean colorNonMessages = false;

    @Switch(
            name = "Color Party Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the party chat blue to make them more visible in busy lobbies."
    )
    public boolean colorPartyChat = false;

    @Switch(
            name = "Color Guild Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the guild chat green to make them more visible in busy lobbies."
    )
    public boolean colorGuildChat = false;

    @Switch(
            name = "Color Guild Officer Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the guild officer chat aqua to make them more visible in busy lobbies."
    )
    public boolean colorOfficerChat = false;

    @Switch(
            name = "SkyBlock Co-op Chat",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Color messages from the SkyBlock coop chat aqua to make them more visible in busy lobbies."
    )
    public boolean colorCoopChat = false;

    @Switch(
            name = "Visible Colors",
            category = "Chat",
            subcategory = "Chat Color",
            description = "Converts the custom colors mentioned above to more visible colors. Dark Green -> Light Green and Blue -> Gold. (Recommended)"
    )
    public boolean visibleColors = false;

    //Fun
    @Switch(
            name = "OwO Language toggle",
            category = "Chat",
            subcategory = "Fun",
            description = "Replaces all chat messages with OwO language.\nThis feature basically breaks the whole chat, so please be warned"
    )
    public boolean owoLanguage = false;
}
