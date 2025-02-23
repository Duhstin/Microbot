package net.runelite.client.plugins.microbot.nateplugins.skilling.natefishing;

import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.nateplugins.skilling.natefishing.enums.Fish;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.antiban.Rs2AntibanSettings;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.camera.Rs2Camera;
import net.runelite.client.plugins.microbot.util.equipment.Rs2Equipment;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.runelite.client.plugins.microbot.util.npc.Rs2Npc.validateInteractable;

enum State {
    FISHING,
    RESETTING,
}

public class AutoFishingScript extends Script {

    public static String version = "1.5.0";
    State state;

    public boolean run(AutoFishConfig config) {
        initialPlayerLocation = null;
        List<String> itemNames = Arrays.stream(config.itemsToBank().split(",")).map(String::toLowerCase).collect(Collectors.toList());
        state = State.FISHING;
        Fish fish = config.fish();
        Rs2Antiban.resetAntibanSettings();
        Rs2Antiban.antibanSetupTemplates.applyFishingSetup();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!super.run()) return;
                if (!Microbot.isLoggedIn()) return;
                if (Rs2AntibanSettings.actionCooldownActive) return;

                if (initialPlayerLocation == null) {
                    initialPlayerLocation = Rs2Player.getWorldLocation();
                }

                if (!hasRequiredItems(fish,config.useEchoHarpoon())) {
                    Microbot.showMessage("You are missing the required tools to catch this fish");
                    shutdown();
                    return;
                }

                if (Rs2Player.isMoving() || Rs2Antiban.getCategory().isBusy() || Microbot.pauseAllScripts) return;

                switch (state) {
                    case FISHING:
                        NPC fishingSpot = getFishingSpot(fish);
                        if (fishingSpot == null || Rs2Inventory.isFull()) {
                            state = State.RESETTING;
                            return;
                        }

                        if (!Rs2Camera.isTileOnScreen(fishingSpot.getLocalLocation())) {
                            validateInteractable(fishingSpot);
                        }
                        if (Rs2Npc.interact(fishingSpot, fish.getAction())) {
                            Rs2Antiban.actionCooldown();
                            Rs2Antiban.takeMicroBreakByChance();
                        }
                        break;
                    case RESETTING:
                        if (config.useBank()) {
                            if (Rs2Bank.walkToBankAndUseBank()){
                                for (String itemName : itemNames) {
                                    Rs2Bank.depositAll(itemName,false);
                                    //Rs2Bank.depositAll(x -> x.name.toLowerCase().contains(itemName));
                                }
                                Rs2Bank.emptyFishBarrel();

                                Rs2Walker.walkTo(initialPlayerLocation);
                            }
                        } else {
                            Rs2Inventory.dropAllExcept(false, config.getDropOrder(), "rod", "net", "pot", "harpoon", "feather", "bait", "vessel", "candle", "lantern");
                        }
                        state = State.FISHING;
                        break;
                }
            } catch (Exception ex) {
                Microbot.log(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    private boolean hasRequiredItems(Fish fish, boolean echoHarpoon) {
        if(echoHarpoon){
            return Rs2Inventory.hasItem("Echo harpoon") || Rs2Equipment.hasEquipped(ItemID.ECHO_HARPOON);
        }
        switch (fish) {
            case MONKFISH:
            case KARAMBWANJI:
            case SHRIMP:
                return Rs2Inventory.hasItem("small fishing net");
            case SARDINE:
            case PIKE:
                return Rs2Inventory.hasItem("fishing rod") && Rs2Inventory.hasItem("bait");
            case MACKEREL:
                return Rs2Inventory.hasItem("big fishing net");
            case TROUT:
                return Rs2Inventory.hasItem("fly fishing rod") && Rs2Inventory.hasItem("feather");
            case TUNA:
            case SHARK:
                return Rs2Inventory.hasItem("harpoon") || Rs2Equipment.hasEquipped(ItemID.DRAGON_HARPOON) ||  Rs2Equipment.hasEquipped(ItemID.DRAGON_HARPOON_OR);
            case LOBSTER:
                return Rs2Inventory.hasItem("lobster pot");
            case LAVA_EEL:
                return Rs2Inventory.hasItem("oily fishing rod") && Rs2Inventory.hasItem("fishing bait");
            case CAVE_EEL:
                return Rs2Inventory.hasItem("fishing rod") && Rs2Inventory.hasItem("fishing bait");
            case ANGLERFISH:
                return Rs2Inventory.hasItem("fishing rod") && Rs2Inventory.hasItem("sandworms");
                default:
                return false;
        }
    }

    private NPC getFishingSpot(Fish fish) {
        NPC fishingspot;
        for (int fishingSpotId : fish.getFishingSpot()) {
            fishingspot = Rs2Npc.getNpc(fishingSpotId);
            if (fishingspot != null) {
                return fishingspot;
            }
        }
        return null;
    }
    
    @Override
    public void shutdown(){
        super.shutdown();
        Rs2Antiban.resetAntibanSettings();
    }
}
