package net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.scripts;


import net.runelite.api.Skill;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.dustin.WildernessSlayerHelper.WildernessSlayerHelperConfig;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Item;
import net.runelite.client.plugins.microbot.util.player.Rs2Pvp;


import java.util.List;
import java.util.concurrent.TimeUnit;


public class DrinkPrayer extends Script {

    public long nextSip = 0;

    public boolean run(WildernessSlayerHelperConfig config) {
        Microbot.enableAutoRunOn = false;
        //Rs2Antiban.resetAntibanSettings();
       // Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!super.run() || !Microbot.isLoggedIn() || !config.drinkRestores() || !Rs2Pvp.isInWilderness()) return;


                if (nextSip == 0) {
                    nextSip = (int) (Math.random() * (22 - 17)) + 17;
                    Microbot.log("Next sip at " + nextSip);
                } else if (Microbot.getClient().getBoostedSkillLevel(Skill.PRAYER) <= nextSip) {
                    drinkPotion();
                    nextSip = 0;
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }


    public void drinkPotion() {
        if (!Microbot.isLoggedIn()) return;
        List<Rs2Item> potions = Microbot.getClientThread().runOnClientThread(Rs2Inventory::getPotions);
        if (potions == null || potions.isEmpty()) {
            Microbot.log("NO POTIONS IN INVENTORY!!!");
            return;
        }
        for (Rs2Item potion : potions) {
            if (potion.name.toLowerCase().contains("prayer") || potion.name.toLowerCase().contains("super restore")) {
                Rs2Inventory.interact(potion, "drink");
                sleep(500, 1000); // Simulating wait for a realistic action delay
                break;
            }
        }
    }

}