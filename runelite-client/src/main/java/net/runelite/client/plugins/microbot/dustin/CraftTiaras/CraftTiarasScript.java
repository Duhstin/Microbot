package net.runelite.client.plugins.microbot.dustin.CraftTiaras;

import net.runelite.api.AnimationID;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.accountselector.AutoLoginScript;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class CraftTiarasScript extends Script {

    public long lastAnimationTime = 0;
    public int lastAnimationID = 0;
    public static boolean test = false;
    public boolean run(CraftTiarasConfig config) {
        Microbot.enableAutoRunOn = false;
        Rs2Antiban.resetAntibanSettings();
        Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();

        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {

                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

                if (Rs2Player.getAnimation() != AnimationID.IDLE) {
                    lastAnimationTime = System.currentTimeMillis();
                    lastAnimationID = Rs2Player.getAnimation();
                }


                if(!Rs2Inventory.contains("Silver bar")) {
                        if (Rs2Bank.isOpen() && Rs2Bank.count("Silver bar") > 0) {
                        Rs2Bank.depositAllExcept("Tiara mould");
                        sleep((int)(Math.random() * (1500 - 600)) + 600);
                        Rs2Bank.withdrawAll("Silver bar");
                        sleep((int)(Math.random() * (1500 - 600)) + 600);
                        Rs2Bank.closeBank();
                    } else if(Rs2Bank.isOpen() && Rs2Bank.count("Silver bar") == 0) {
                            disableAutoLogin("Silver bars not found in bank.");
                    }else{
                            int randomNum = (int) (Math.random() * (30000 - 8000)) + 8000;

                            if(lastAnimationTime == 0 || (System.currentTimeMillis() - lastAnimationTime) > randomNum) {
                                System.out.println("afk'd for " + randomNum/1000 + " seconds before banking.");
                                Rs2Bank.openBank();
                            }
                    }

                }else{
                    int randomNum = (int) (Math.random() * (15000 - 6000)) + 6000;
                    if(Rs2Inventory.count("Silver bar") == 27 || lastAnimationTime == 0 || (System.currentTimeMillis() - lastAnimationTime) > randomNum && Rs2Inventory.contains("Silver bar")) {
                        if (Rs2GameObject.interact("Furnace", "Smelt")) {
                            sleepUntilOnClientThread(() -> Rs2Widget.getWidget(6, 28) != null);
                            sleep((int)(Math.random() * (1500 - 700)) + 700);
                            if (Rs2Widget.clickWidget(6, 28)) {
                                Rs2Player.waitForAnimation();
                                lastAnimationTime = System.currentTimeMillis();
                            }
                            System.out.println("Using silver bars on furnace to create tiaras.");
                        } else {
                            System.out.println("ummm?");
                        }
                    }
                }




            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
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