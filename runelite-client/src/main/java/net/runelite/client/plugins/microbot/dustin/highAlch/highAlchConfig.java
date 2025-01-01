package net.runelite.client.plugins.microbot.dustin.highAlch;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface highAlchConfig extends Config {
    @ConfigItem(
            keyName = "item",
            name = "Item To Alch",
            description = "Item to alch"

    )
    default String item()
    {
        return "Maple longbow";
    }


}
