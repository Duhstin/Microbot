package net.runelite.client.plugins.microbot.dustin.CraftTiaras;

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
        name = "[D] Tiara Maker",
        description = "Simple tiara crafter",
        tags = {"Tiara Crafter", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class CraftTiarasPlugin extends Plugin {
    @Inject
    private CraftTiarasConfig config;
    @Provides
    CraftTiarasConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CraftTiarasConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private CraftTiarasOverlay exampleOverlay;

    @Inject
    CraftTiarasScript CraftTiaras;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(exampleOverlay);
        }


        CraftTiaras.run(config);
    }

    protected void shutDown() {
        CraftTiaras.shutdown();
        overlayManager.remove(exampleOverlay);
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