package net.runelite.client.plugins.microbot.dustin.highAlch;

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
        name = "[D] High Alcher",
        description = "Simple high alcher",
        tags = {"High alcher", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class highAlchPlugin extends Plugin {
    @Inject
    private highAlchConfig config;
    @Provides
    highAlchConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(highAlchConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private highAlchOverlay exampleOverlay;

    @Inject
    highAlchScript highAlch;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(exampleOverlay);
        }


        highAlch.run(config);
    }

    protected void shutDown() {
        highAlch.shutdown();
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
