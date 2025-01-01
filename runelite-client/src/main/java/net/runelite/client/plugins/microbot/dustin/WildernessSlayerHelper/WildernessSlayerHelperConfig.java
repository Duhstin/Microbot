package net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface WildernessSlayerHelperConfig extends Config {
    @ConfigItem(
            keyName = "cannon",
            name = "Cannon?",
            description = "This will repair and refill a cannon.",
            position = 0,
            section = "general"
    )

    default boolean useCannon() {
        return true;
    }

    @ConfigItem(
            keyName = "teleport",
            name = "Teleport?",
            description = "This will attempt to teleport you from a player within your combat bracket. You need a seed pod!",
            position = 1,
            section = "general"
    )
    default boolean useTeleport() {
        return true;
    }


    @ConfigItem(
            keyName = "restores",
            name = "Prayer?",
            description = "This will drink blighted super restore potions.",
            position = 2,
            section = "general"
    )
    default boolean drinkRestores() {
        return true;
    }

}
