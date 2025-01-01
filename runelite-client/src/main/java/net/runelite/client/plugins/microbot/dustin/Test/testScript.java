package net.runelite.client.plugins.microbot.dustin.Test;

import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.accountselector.AutoLoginScript;
import net.runelite.client.plugins.microbot.breakhandler.BreakHandlerScript;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Item;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;



import java.util.List;

import java.util.concurrent.TimeUnit;


public class testScript extends Script {
    int amountToBuy = 0;
    public boolean run(testConfig config) {
        Microbot.enableAutoRunOn = false;
        int naturePrice = 102;
        int bowPrice = 300;
        Rs2Antiban.resetAntibanSettings();
        Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

                Rs2Item nature = Rs2Inventory.get("Nature rune");
                Rs2Item item = Rs2Inventory.get("Maple longbow");


                if (nature == null || item == null) {
                    if(Rs2Inventory.contains("Coins")) {

                        if (!Rs2GrandExchange.isOpen()) {
                            Rs2Item Coins = Rs2Inventory.get("Coins");
                            amountToBuy = Coins.quantity / (naturePrice + bowPrice);
                            Microbot.log("Out of stock, we can buy " + amountToBuy + " alchs.");
                            Microbot.log("Opening G.E.");
                            Rs2GrandExchange.openExchange();

                        } else if (nature == null && !isBuyingItem("Nature rune")) {
                            Microbot.log("Buying natures.");
                            Rs2GrandExchange.buyItem("Nature rune", naturePrice, amountToBuy);
                            sleepUntil(() -> isBuyingItem("Nature rune"), 10000);

                        } else if (item == null && !isBuyingItem("Maple longbow")) {
                            Microbot.log("Buying bows.");
                            Rs2GrandExchange.buyItem("Maple longbow", bowPrice, amountToBuy);
                            sleepUntil(() -> isBuyingItem("Maple longbow"), 10000);

                        } else if (!Rs2GrandExchange.collectToInventory()) {
                            sleepUntil(() -> Rs2Inventory.waitForInventoryChanges(5000), 10000);
                            Microbot.log("Collecting to inventory");

                        } else if ((nature == null && isBuyingItem("Nature rune")) || (item == null && isBuyingItem("Maple longbow"))) {
                            Rs2GrandExchange.closeExchange();
                            BreakHandlerScript.breakIn = 1;

                        }
                    }else{
                        disableAutoLogin("No coins, stopping script.");
                    }
                    return;
                }



                Microbot.log("");


            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }



    private boolean isBuyingItem(String itemName) {
        int[] widgetChildIds = {7, 8, 9};

        for (int childId : widgetChildIds) {
            Widget widget = Rs2Widget.getWidget(465, childId); // Get the widget
            if (widget != null) {
                Widget[] subChildren = widget.getChildren(); // Get the sub-children
                if (subChildren != null) {
                    for (Widget subChild : subChildren) {
                        if (subChild != null && itemName.equals(subChild.getText())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void disableAutoLogin(String message) {
        List<Script> runningScripts = Microbot.getActiveScripts();

        for (Script script : runningScripts) {
            if (script instanceof AutoLoginScript) {
                script.shutdown();
            }
        }
        Rs2Player.logout();

    }


}