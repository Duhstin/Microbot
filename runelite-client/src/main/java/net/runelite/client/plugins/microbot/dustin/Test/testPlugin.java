package net.runelite.client.plugins.microbot.dustin.Test;

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
        name = "[D] Test",
        description = "Test",
        tags = {"Test", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class testPlugin extends Plugin {
    @Inject
    private testConfig config;
    @Provides
    testConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(testConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private testOverlay exampleOverlay;

    @Inject
    testScript testScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(exampleOverlay);
        }
        testScript.run(config);
    }

    protected void shutDown() {
        testScript.shutdown();
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
