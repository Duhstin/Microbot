package net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.scripts;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.WildernessSlayerHelperConfig;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2Cannon;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.math.Random;
import net.runelite.client.plugins.microbot.util.player.Rs2Pvp;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;


public class Cannon extends Script {
    @Inject
    private Client client;

    public long nextRefill = 0;
    public boolean taskFinished = false;
    public boolean run(WildernessSlayerHelperConfig config) {
        Microbot.enableAutoRunOn = false;
       // Rs2Antiban.resetAntibanSettings();
       // Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();

        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!super.run() || !Microbot.isLoggedIn() || !config.useCannon()) return;

                if(!Rs2Pvp.isInWilderness()) {
                    if(taskFinished){
                        taskFinished = false;
                    }

                    return;
                }

                if (Rs2Cannon.repair()) return;

                Rs2Cannon.refill();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 3000, TimeUnit.MILLISECONDS);
        return true;
    }


    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }

        if(chatMessage.getMessage().contains("You have completed your task!")){
            taskFinished = true;
        }

        if(chatMessage.getMessage().contains("Your cannon is out of ammo")){
            Rs2Cannon.refill();
        }
    }

}