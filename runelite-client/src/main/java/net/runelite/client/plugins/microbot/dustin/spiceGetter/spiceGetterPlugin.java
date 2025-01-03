package net.runelite.client.plugins.microbot.dustin.spiceGetter;

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
        name = "[D] Spice Getter",
        description = "Spice Getter",
        tags = {"Spice Getter", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class spiceGetterPlugin extends Plugin {
    @Inject
    private spiceGetterConfig config;
    @Provides
    spiceGetterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(spiceGetterConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private spiceGetterOverlay exampleOverlay;

    @Inject
    net.runelite.client.plugins.microbot.dustin.spiceGetter.spiceGetterScript spiceGetterScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(exampleOverlay);
        }
        spiceGetterScript.run(config);
    }

    protected void shutDown() {
        spiceGetterScript.shutdown();
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
