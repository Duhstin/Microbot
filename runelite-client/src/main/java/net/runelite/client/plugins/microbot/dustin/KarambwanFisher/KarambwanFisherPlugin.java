package net.runelite.client.plugins.microbot.dustin.KarambwanFisher;

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
        name = "[D] Karambwan Fisher",
        description = "Fishes for Karambwan",
        tags = {"Fish", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class KarambwanFisherPlugin extends Plugin {
    @Inject
    private KarambwanFisherConfig config;
    @Provides
    KarambwanFisherConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(KarambwanFisherConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private KarambwanFisherOverlay KarambwanFisherOverlay;

    @Inject
    KarambwanFisherScript KarambwanFisherScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(KarambwanFisherOverlay);
        }
        KarambwanFisherScript.run(config);
    }

    protected void shutDown() {
        KarambwanFisherScript.shutdown();
        overlayManager.remove(KarambwanFisherOverlay);
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
