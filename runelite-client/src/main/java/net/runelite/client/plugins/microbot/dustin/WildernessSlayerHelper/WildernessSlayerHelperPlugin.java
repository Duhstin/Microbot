package net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = "[D] Wilderness Slayer Helper",
        description = "Drinks prayer potions, refills/fixes cannon, and teleports from players if <= 30 wilderness.",
        tags = {"Wilderness", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class WildernessSlayerHelperPlugin extends Plugin {
    @Inject
    private WildernessSlayerHelperConfig config;
    @Provides
    WildernessSlayerHelperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(WildernessSlayerHelperConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private WildernessSlayerHelperOverlay WildernessSlayerHelperOverlay;

    @Inject
    net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.scripts.Cannon CannonScript;
    @Inject
    net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.scripts.DrinkPrayer DrinkPrayerScript;
    @Inject
    net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.scripts.PlayerDetection PlayerDetectionScript;

    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(WildernessSlayerHelperOverlay);
        }

        CannonScript.run(config);
        DrinkPrayerScript.run(config);
        PlayerDetectionScript.run(config);
    }

    protected void shutDown() {
        CannonScript.shutdown();
        DrinkPrayerScript.shutdown();
        PlayerDetectionScript.shutdown();
        overlayManager.remove(WildernessSlayerHelperOverlay);
    }






    int ticks = 10;
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        //System.out.println(getName().chars().mapToObj(i -> (char)(i + 3)).map(String::valueOf).collect(Collectors.joining()));

        if (ticks > 0) {
            ticks--;
        } else {
            ticks = 10;
        }

    }

}
