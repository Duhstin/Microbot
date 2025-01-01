package net.runelite.client.plugins.microbot.dustin.highAlch;

import com.google.inject.Inject;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.accountselector.AutoLoginScript;
import net.runelite.client.plugins.microbot.breakhandler.BreakHandlerPlugin;
import net.runelite.client.plugins.microbot.breakhandler.BreakHandlerScript;
import net.runelite.client.plugins.microbot.globval.enums.InterfaceTab;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Item;
import net.runelite.client.plugins.microbot.util.magic.Rs2Magic;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.tabs.Rs2Tab;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class highAlchScript extends Script {
    @Inject
    public static Duration HowLongBreak;
    public long lastSleepTime = 0;
    public long nextRandomSleep = 0;
    int amountToBuy = 0;
    public static boolean test = false;
    public boolean run(highAlchConfig config) {
        int naturePrice = 102;
        int bowPrice = 290;

        Microbot.enableAutoRunOn = false;
        Rs2Antiban.resetAntibanSettings();
        Rs2Antiban.antibanSetupTemplates.applyGeneralBasicSetup();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {

                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;

                if(isBreakHandlerOn()) {
                    HowLongBreak = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(BreakHandlerScript.breakDuration));
                    long hours = BreakHandlerScript.duration.toHours();
                    long minutes = BreakHandlerScript.duration.toMinutes() % 60;
                    long seconds = BreakHandlerScript.duration.getSeconds() % 60;

                        if(hours == 0 & minutes == 0 & seconds < 15){
                            Widget chatbox = Rs2Widget.getWidget(162, 56);

                            if (chatbox != null) {
                                Rs2Widget.clickWidget(chatbox);
                                sleepUntil(() -> !Microbot.isLoggedIn(), 30000);
                                return;
                            }
                        }
                }



                Rs2Item nature = Rs2Inventory.get("Nature rune");
                Rs2Item item = Rs2Inventory.get(config.item());


                if (nature == null || item == null) {
                    if(!Rs2Inventory.contains("Coins")) {
                        disableAutoLogin("No coins, stopping script.");
                        return;
                    }

                        if (!Rs2GrandExchange.isOpen()) {
                            Rs2Item Coins = Rs2Inventory.get("Coins");
                            amountToBuy = Coins.quantity / (naturePrice + bowPrice);
                            Microbot.log("Out of stock, we can buy " + amountToBuy + " alchs.");
                            Microbot.log("Opening G.E.");
                            Rs2GrandExchange.openExchange();
                            return;
                        } else if (nature == null && !isBuyingItem("Nature rune")) {
                            Microbot.log("Buying natures.");
                            Rs2GrandExchange.buyItem("Nature rune", naturePrice, amountToBuy);
                            sleepUntil(() -> isBuyingItem("Nature rune"), 10000);
                            return;
                        } else if (item == null && !isBuyingItem(config.item())) {
                            Microbot.log("Buying bows.");
                            Rs2GrandExchange.buyItem(config.item(), bowPrice, amountToBuy);
                            sleepUntil(() -> isBuyingItem(config.item()), 10000);
                            return;
                        } else if (!Rs2GrandExchange.collectToInventory()) {
                            Microbot.log("Collecting to inventory");
                            sleep((int) (Math.random() * (20000 - 10000)) + 10000);
                            return;
                        } else if ((!Rs2Inventory.contains("Nature rune") && isBuyingItem("Nature rune")) || (!Rs2Inventory.contains(config.item()) && isBuyingItem(config.item()))) {
                           Rs2GrandExchange.closeExchange();
                           BreakHandlerScript.breakIn = 30;
                            return;
                        }

                    return;
                }

                if (Rs2GrandExchange.isOpen()) Rs2GrandExchange.closeExchange();

                Rs2Magic.alch(config.item(), 100, 400);


                if (lastSleepTime == 0) {
                    lastSleepTime = System.currentTimeMillis();
                    nextRandomSleep = System.currentTimeMillis() + (int) (Math.random() * (600000 - 120000)) + 120000;
                    Microbot.log("Time: " + new Timestamp(System.currentTimeMillis()) + " - Next time: " + new Timestamp(nextRandomSleep));
                } else if (System.currentTimeMillis() > nextRandomSleep) {
                    int sleepTime = (int) (Math.random() * (180000 - 30000)) + 30000;
                    Microbot.log("Random sleep. (" + sleepTime/1000 + ")");
                    sleep(sleepTime);
                    Microbot.log("Continuing.");
                    lastSleepTime = 0;
                    nextRandomSleep = 0;
                }



                sleepUntil(() -> Rs2Tab.getCurrentTab() == InterfaceTab.MAGIC, 7000);


            } catch (Exception ex) {
                System.out.println("ERR " + ex.getMessage());
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

    private boolean isBreakHandlerOn() {
        PluginManager pluginManager = Microbot.getPluginManager();

        for (Plugin plugin : pluginManager.getPlugins()) {
            if (plugin instanceof BreakHandlerPlugin) {
                if (pluginManager.isPluginEnabled(plugin)) {
                    return true;
                }
            }
        }
        return false;
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
}