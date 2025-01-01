package net.runelite.client.plugins.microbot.dustin.spiceGetter;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.accountselector.AutoLoginScript;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class spiceGetterScript extends Script {
    private static final String TARGET_CAT = "Hellcat";
    public long lastCatClick = 0;
    String[] itemsToCheck = {"Orange spice (1)", "Orange spice (2)", "Orange spice (3)", "Orange spice (4)"};
    public static boolean test = false;
    public boolean run(spiceGetterConfig config) {
        Microbot.enableAutoRunOn = false;
        Rs2Antiban.resetAntibanSettings();
        Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

                if(!anyItemsExist(itemsToCheck)){
                    chaseCat();
                }


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private void chaseCat() {
        int randomNum = (int) (Math.random() * (8000 - 4000)) + 4000;

        var catNpc = Rs2Npc.getNpc(TARGET_CAT);

        if (catNpc != null && Rs2Npc.canWalkTo(catNpc, 20) && catNpc.getWorldLocation().distanceTo(Rs2Player.getWorldLocation()) < 2) {//&& lastCatClick == 0 || (System.currentTimeMillis() - lastCatClick) > randomNum
            Rs2Npc.interact(catNpc, "Chase");
            Rs2Player.waitForAnimation();
           // lastCatClick = System.currentTimeMillis();
        } else {
            disableAutoLogin("Cat not found. Disabling Auto Login and logging out.");
        }
    }

    public boolean anyItemsExist(String[] itemNames) {
        for (String itemName : itemNames) {
            if (Rs2GroundItem.exists(itemName, 20)) {
                Rs2GroundItem.loot(itemName, 20);
                Rs2Player.waitForWalking();
                return true; // Found at least one item
            }
        }
        return false; // None of the items were found
    }


    private void disableAutoLogin(String message) {
        List<Script> runningScripts = Microbot.getActiveScripts();

        for (Script script : runningScripts) {
            if (script instanceof AutoLoginScript) {
                script.shutdown();
            }
        }
        Rs2Player.logout();
        Microbot.showMessage(message);
        shutdown();
    }

}